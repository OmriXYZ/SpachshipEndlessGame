package com.example.carendlessgame.models;

public class Record {
    private int score;

    private double lon, lat;

    public Record(int score, double lon, double lat) {
        this.score = score;
        this.lon = lon;
        this.lat = lat;
    }

    public int getScore() {
        return score;
    }
    public double getLon() {
        return lon;
    }
    public double getLat() {
        return lat;
    }
}
