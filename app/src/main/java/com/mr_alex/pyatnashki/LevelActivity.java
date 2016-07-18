package com.mr_alex.pyatnashki;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class LevelActivity extends Activity {

    private Sound sound;
    private int setButtonSound;
    private ImageButton bSound;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_level);

        AssetManager assetManager = getAssets();
        sound = new Sound(assetManager);
        setButtonSound = sound.loadSound("Schelchok1.mp3");
        sound.setCheckSound(true);

        Typeface digitalFont = Typeface.createFromAsset(this.getAssets(), "font.ttf");

        ImageButton bNewGame9 = (ImageButton) findViewById(R.id.bLevel9);
        bNewGame9.setOnClickListener(onClickListener);
        ImageButton bNewGame15 = (ImageButton) findViewById(R.id.bLevel24);
        bNewGame15.setOnClickListener(onClickListener);
        ImageButton bNewGame24 = (ImageButton) findViewById(R.id.bLevel15);
        bNewGame24.setOnClickListener(onClickListener);

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

        ImageButton bBackMenu = (ImageButton) this.findViewById(R.id.bBackMenu);
        bBackMenu.setOnClickListener(onClickListener);

        TextView tGame = (TextView) findViewById(R.id.tGame);
        tGame.setTypeface(digitalFont);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bLevel9:
                    newGame(3);
                    break;
                case R.id.bLevel15:
                    newGame(4);
                    break;
                case R.id.bLevel24:
                    newGame(5);
                    break;
                case R.id.bSoundOffOn:
                    soundOffOn();
                    break;
                case R.id.bBackMenu:
                    backMenu();
                    break;
                default:
                    break;
            }
            sound.playSound(setButtonSound);
        }
    };

    private void newGame(int level) {
        Intent intent = new Intent();
        switch (level) {
            case 3:
                intent = new Intent(LevelActivity.this, GameActivity9.class);
                break;
            case 4:
                intent = new Intent(LevelActivity.this, GameActivity.class);
                break;
            case 5:
                intent = new Intent(LevelActivity.this, GameActivity24.class);
                break;
        }

        intent.putExtra("checkSound", sound.getCheckSound());
        intent.putExtra("keyLevel", level);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void soundOffOn() {
        sound.setCheckSound(!sound.getCheckSound());
        if (sound.getCheckSound())
            bSound.setImageResource(R.drawable.soundon);
        else bSound.setImageResource(R.drawable.soundoff);
    }

    public void backMenu() {
        Intent intent = new Intent(LevelActivity.this, MenuActivity.class);
        intent.putExtra("checkSound", sound.getCheckSound());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
