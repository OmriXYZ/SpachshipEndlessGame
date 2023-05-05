package com.example.carendlessgame.models;

public class Spaceship {
    private int spaceshipPosition;

    public Spaceship(int spaceshipPosition) {
        this.spaceshipPosition = spaceshipPosition;
    }

    public int getSpaceshipPosition() {
        return spaceshipPosition;
    }

    public void setSpaceshipPosition(int spaceshipPosition) {
        this.spaceshipPosition = spaceshipPosition;
    }

    public void resetSpaceshipPosition(int pos) {
        spaceshipPosition = pos;
    }
}
