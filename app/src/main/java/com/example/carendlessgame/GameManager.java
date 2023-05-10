package com.example.carendlessgame;


import android.util.Log;

import com.example.carendlessgame.Utilities.MySPv3;
import com.example.carendlessgame.models.Record;
import com.example.carendlessgame.models.Records;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


public class GameManager {

    public static final int NUM_ROWS = 8;
    public static final int NUM_COLUMNS = 5;
    public static final int MAX_LIVE = 3;
    public static final int CENTER_POS = NUM_COLUMNS/2;
    private int lives;
    private int distance;
    private int fall_rocks_delay_ms;
    private int generate_rocks_delay_ms;
    public boolean isSensorGame;

    public GameManager(int fall_delay, int generate_delay) {
        fall_rocks_delay_ms = fall_delay;
        generate_rocks_delay_ms = generate_delay;
        lives = MAX_LIVE;
        distance = 0;
        isSensorGame = false;
    }
    public GameManager() {
        fall_rocks_delay_ms = 500;
        generate_rocks_delay_ms = 1000;
        lives = MAX_LIVE;
        distance = 0;
        isSensorGame = true;
    }

    public int getLives() {
        return lives;
    }

    public void decreaseLive() {
        lives--;
    }

    public int addOneDistance() {
        return distance++;
    }

    public int getFall_rocks_delay_ms() {
        return fall_rocks_delay_ms;
    }

    public int getGenerate_rocks_delay_ms() {
        return generate_rocks_delay_ms;
    }

    public void changeDelaysByStepY(int stepY) {
        fall_rocks_delay_ms = 500 + stepY;
        generate_rocks_delay_ms = fall_rocks_delay_ms*2;
    }

    public int getDistance() {
        return distance;
    }
    public void lose(double lon, double lat) {

        String fromSP = MySPv3.getInstance().getString("records","");
        Records recordsFromJson = new Gson().fromJson(fromSP, Records.class);
        if (recordsFromJson != null)
            recordsFromJson.add(new Record(distance, lon, lat));
        else {
            recordsFromJson = new Records();
            recordsFromJson.add(new Record(distance, lon, lat));
        }
        MySPv3.getInstance().putString("records", recordsFromJson.toJson());
        fromSP = MySPv3.getInstance().getString("records","");
        Log.d("Records from json:", fromSP);

    }

    public void earnCoin() {
        distance+=10;
    }
}
