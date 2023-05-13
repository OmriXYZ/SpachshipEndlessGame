package com.example.carendlessgame.Utilities;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.carendlessgame.R;

public class SoundControl {

    private final MediaPlayer knock_sound;
    private final MediaPlayer coin_sound;

    public SoundControl(Context context) {
        this.knock_sound =  MediaPlayer.create(context, R.raw.knock);
        this.coin_sound =  MediaPlayer.create(context, R.raw.coin);
    }

    public void playKnockSound() {
        knock_sound.start();
    }
    public void playCoinSound() {
        coin_sound.start();
    }

}
