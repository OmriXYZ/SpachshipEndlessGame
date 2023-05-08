package com.example.carendlessgame.models;

public class Record {
    private int score;
    private String coordinate;

    public Record(int score, String coordinate) {
        this.score = score;
        this.coordinate = coordinate;
    }

    public int getScore() {
        return score;
    }

    public String getCoordinate() {
        return coordinate;
    }
}
