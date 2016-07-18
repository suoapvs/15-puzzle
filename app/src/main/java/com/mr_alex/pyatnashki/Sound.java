package com.mr_alex.pyatnashki;

import android.media.AudioManager;
import android.media.SoundPool;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import java.io.IOException;

public class Sound {

    private AssetManager mAssetManager;
    private SoundPool mSoundPool;
    private boolean checkSound = true;

    public Sound(AssetManager AM) {
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mAssetManager = AM;
    }

    public int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }

    public void playSound(int sound) {
        if ((sound > 0) && checkSound)
            mSoundPool.play(sound, 1, 1, 1, 0, 1);
    }

    public boolean getCheckSound() {
        return checkSound;
    }

    public void setCheckSound(boolean CS) {
        checkSound = CS;
    }
}
