package com.example.carendlessgame;


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

    public void resetGame() {
        lives = MAX_LIVE;
        distance = 0;
    }

    public void changeDelaysByStepY(int stepY) {
        fall_rocks_delay_ms = 500 + stepY;
        generate_rocks_delay_ms = fall_rocks_delay_ms*2;
    }
}
