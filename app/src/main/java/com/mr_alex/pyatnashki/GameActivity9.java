package com.mr_alex.pyatnashki;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.graphics.Typeface;

public class GameActivity9 extends Activity {

    private final int N = 3;
    Cards cards;
    private ImageButton[][] button;
    private final int BUTTON_ID[][] = {{R.id.b900, R.id.b901, R.id.b902},
            {R.id.b910, R.id.b911, R.id.b912},
            {R.id.b920, R.id.b921, R.id.b922}};
    private final int CADRS_ID[] = {R.drawable.card900, R.drawable.card901, R.drawable.card902,
            R.drawable.card903, R.drawable.card904, R.drawable.card905,
            R.drawable.card906, R.drawable.card907, R.drawable.card908};

    private TextView tScore;
    private int numbSteps;
    private TextView tBestScore;
    private int numbBestSteps;

    private Sound sound;
    private int clickSound;
    private int victorySound;
    private int setButtonSound;
    private ImageButton ibSound;;

    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game9);

        button = new ImageButton[N][N];
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                button[i][j] = (ImageButton) this.findViewById(BUTTON_ID[i][j]);
                button[i][j].setOnClickListener(onClickListener);
            }
        ImageButton bNewGame = (ImageButton) this.findViewById(R.id.bNewGame);
        bNewGame.setOnClickListener(onClickListener);
        ImageButton bBackMenu = (ImageButton) this.findViewById(R.id.bBackMenu);
        bBackMenu.setOnClickListener(onClickListener);
        ImageButton bAbout = (ImageButton) this.findViewById(R.id.bAboutGame);
        bAbout.setOnClickListener(onClickListener);
        ibSound = (ImageButton) this.findViewById(R.id.bSoundOffOn);
        ibSound.setOnClickListener(onClickListener);


        Typeface digitalFont = Typeface.createFromAsset(this.getAssets(), "font.ttf");
        TextView tPyatnashki = (TextView) this.findViewById(R.id.tPyatnashki);
        tPyatnashki.setTypeface(digitalFont);
        TextView tSScore = (TextView) this.findViewById(R.id.tSScore);
        tSScore.setTypeface(digitalFont);
        tScore = (TextView) this.findViewById(R.id.tScore);
        tScore.setTypeface(digitalFont);
        TextView tBestSScore = (TextView) this.findViewById(R.id.tBestSScore);
        tBestSScore.setTypeface(digitalFont);
        tBestScore = (TextView) this.findViewById(R.id.tBestScore);
        tBestScore.setTypeface(digitalFont);

        AssetManager AM = getAssets();
        sound = new Sound(AM);
        clickSound = sound.loadSound("Schelchok.mp3");
        victorySound = sound.loadSound("Victory.mp3");
        setButtonSound = sound.loadSound("Schelchok1.mp3");

        try {
            sound.setCheckSound(getIntent().getExtras().getBoolean("checkSound"));
        } catch(Exception e) {
            sound.setCheckSound(true);
        }

        cards = new Cards(N, N);
        try {
            if(getIntent().getExtras().getInt("keyGame") == 1) {
                continueGame();
                checkFinish();
            } else newGame();
        } catch (Exception e) {
            newGame();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!check)
                for(int i = 0; i < N; i++)
                    for(int j = 0; j < N; j++)
                        if(v.getId() == BUTTON_ID[i][j])
                            buttonFunction(i, j);

            switch(v.getId()) {
                case R.id.bNewGame:
                    sound.playSound(setButtonSound);
                    newGame();
                    break;
                case R.id.bBackMenu:
                    sound.playSound(setButtonSound);
                    backMenu();
                    break;
                case R.id.bAboutGame:
                    sound.playSound(setButtonSound);
                    aboutOnClick();
                    break;
                case R.id.bSoundOffOn:
                    soundOffOn();
                    sound.playSound(setButtonSound);
                    break;
                default:
                    break;
            }
        }
    };

    public void buttonFunction(int row, int columb) {
        cards.moveCards(row, columb);
        if(cards.resultMove()) {
            sound.playSound(clickSound);
            numbSteps++;
            showGame();
            checkFinish();
        }
    }

    public void newGame() {
        cards.getNewCards();
        numbSteps = 0;
        numbBestSteps = Integer.parseInt(readFile("fbs9"));
        tBestScore.setText(Integer.toString(numbBestSteps));
        showGame();
        check = false;
    }

    private void continueGame() {
        String text = getPreferences(MODE_PRIVATE).getString("savePyatnashki", "");
        int k = 0;
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                cards.setValueBoard(i, j,  Integer.parseInt("" + text.charAt(k) + text.charAt(k + 1)));
                k += 2;
            }

        numbSteps = Integer.parseInt(readFile("fileScore"));
        numbBestSteps = Integer.parseInt(readFile("fbs9"));
        tBestScore.setText(Integer.toString(numbBestSteps));

        showGame();
        check = false;
    }

    public void backMenu() {
        saveValueBoard();
        Intent intent = new Intent(GameActivity9.this, MenuActivity.class);
        intent.putExtra("checkSound", sound.getCheckSound());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void saveValueBoard() {
        String text = "";
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                if(cards.getValueBoard(i, j) < 10)
                    text += "0" + cards.getValueBoard(i, j);
                else text += cards.getValueBoard(i, j);
            }

        getPreferences(MODE_PRIVATE).edit().putString("savePyatnashki", text).commit();
        writeFile(Integer.toString(numbSteps), "fileScore");
        writeFile(Integer.toString(N), "fileN");
    }

    public void aboutOnClick() {
        startActivity(new Intent(GameActivity9.this, AboutActivity.class));
    }

    public void showGame() {
        tScore.setText(Integer.toString(numbSteps));

        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
                button[i][j].setImageResource(CADRS_ID[cards.getValueBoard(i, j)]);

        if (sound.getCheckSound())
            ibSound.setImageResource(R.drawable.soundon);
        else ibSound.setImageResource(R.drawable.soundoff);
    }

    public void checkFinish(){
        if(cards.finished(N, N)){
            showGame();
            sound.playSound(victorySound);
            Toast.makeText(GameActivity9.this, R.string.you_won, Toast.LENGTH_SHORT).show();
            if ((numbSteps < numbBestSteps) || (numbBestSteps == 0)) {
                writeFile(Integer.toString(numbSteps), "fbs9");
                tBestScore.setText(Integer.toString(numbSteps));
            }
            check = true;
        }
    }

    public void soundOffOn() {
        sound.setCheckSound(!sound.getCheckSound());
        showGame();
    }

    public void writeFile(String text, String file) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE)));
            bw.write(text);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(String file) {
        String text;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(file)));
            text = br.readLine();
        } catch(IOException e) {
            text = "0";
        }
        return text;
    }
}
