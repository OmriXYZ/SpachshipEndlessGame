package com.example.carendlessgame.Utilities;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.carendlessgame.R;

import java.util.HashMap;

public class SoundPoolControl {
    private SoundPool mShortPlayer= null;
    private final HashMap mSounds = new HashMap();

    public SoundPoolControl(Context pContext)
    {
        this.mShortPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        mSounds.put(R.raw.knock, this.mShortPlayer.load(pContext, R.raw.knock, 1));
        mSounds.put(R.raw.coin, this.mShortPlayer.load(pContext, R.raw.coin, 1));
    }

    public void playShortResource(int piResource) {
        int iSoundId = (Integer) mSounds.get(piResource);
        this.mShortPlayer.play(iSoundId, 0.99f, 0.99f, 0, 0, 1);
    }

    public void release() {
        this.mShortPlayer.release();
        this.mShortPlayer = null;
    }
}
