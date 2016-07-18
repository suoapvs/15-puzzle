package com.mr_alex.pyatnashki;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuActivity extends Activity {

    private Sound sound;
    private int setButtonSound;
    private ImageButton bSound;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        AssetManager assetManager = getAssets();
        sound = new Sound(assetManager);
        setButtonSound = sound.loadSound("Schelchok1.mp3");
        sound.setCheckSound(true);

        Typeface digitalFont = Typeface.createFromAsset(this.getAssets(), "font.ttf");

        Button bGame = (Button) findViewById(R.id.bNewGame);
        bGame.setTypeface(digitalFont);
        bGame.setOnClickListener(onClickListener);
        Button bСontinue = (Button) findViewById(R.id.bСontinue);
        bСontinue.setTypeface(digitalFont);
        bСontinue.setOnClickListener(onClickListener);
        Button bHelp = (Button) findViewById(R.id.bHelp);
        bHelp.setTypeface(digitalFont);
        bHelp.setOnClickListener(onClickListener);
        Button bExit = (Button) findViewById(R.id.bExit);
        bExit.setTypeface(digitalFont);
        bExit.setOnClickListener(onClickListener);

        try {
            sound.setCheckSound(getIntent().getExtras().getBoolean("checkSound"));
        } catch(Exception e) {
            sound.setCheckSound(true);
        }

        bSound = (ImageButton) this.findViewById(R.id.bSoundOffOn);
        bSound.setOnClickListener(onClickListener);
        if (sound.getCheckSound())
            bSound.setImageResource(R.drawable.soundon);
        else bSound.setImageResource(R.drawable.soundoff);


        TextView tGame = (TextView) findViewById(R.id.tGame);
        tGame.setTypeface(digitalFont);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bNewGame:
                    newGame();
                    break;
                case R.id.bСontinue:
                    continueGame();
                    break;
                case R.id.bHelp:
                    help();
                    break;
                case R.id.bExit:
                    exit();
                    break;
                case R.id.bSoundOffOn:
                    soundOffOn();
                    break;
                default:
                    break;
            }
            sound.playSound(setButtonSound);
        }
    };

    private void newGame() {
        Intent intent = new Intent(MenuActivity.this, LevelActivity.class);
        intent.putExtra("checkSound", sound.getCheckSound());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void continueGame() {
        try{
            int n = Integer.parseInt(readFile("fileN"));
            Intent intent = new Intent();
            switch (n) {
                case 3:
                    intent = new Intent(MenuActivity.this, GameActivity9.class);
                    break;
                case 4:
                    intent = new Intent(MenuActivity.this, GameActivity.class);
                    break;
                case 5:
                    intent = new Intent(MenuActivity.this, GameActivity24.class);
                    break;
            }
            intent.putExtra("checkSound", sound.getCheckSound());
            intent.putExtra("keyGame", 1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        } catch(Exception e) {
            Toast.makeText(MenuActivity.this, R.string.notSave, Toast.LENGTH_SHORT).show();
        }
    }

    private void help() {
        Intent intent = new Intent(MenuActivity.this, HelpActivity.class);
        intent.putExtra("checkSound", sound.getCheckSound());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void exit() {
        super.onDestroy();
        finish();
    }

    public void soundOffOn() {
        sound.setCheckSound(!sound.getCheckSound());
        if (sound.getCheckSound())
            bSound.setImageResource(R.drawable.soundon);
        else bSound.setImageResource(R.drawable.soundoff);
    }

    public String readFile(String FILENAME) {
        String text;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME)));
            text = br.readLine();
        } catch(IOException e) {
            text = "0";
        }
        return text;
    }
}