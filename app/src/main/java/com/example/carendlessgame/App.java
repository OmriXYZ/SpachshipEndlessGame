package com.example.carendlessgame;

import android.app.Application;

import com.example.carendlessgame.Utilities.MySPv3;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MySPv3.init(this);
    }
}
