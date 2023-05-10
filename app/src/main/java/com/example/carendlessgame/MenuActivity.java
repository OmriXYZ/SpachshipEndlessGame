package com.example.carendlessgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    Button menu_BTN_fast, menu_BTN_slow, menu_BTN_sensor, menu_BTN_records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        menu_BTN_slow.setOnClickListener(v -> openGameScreen(500, false));
        menu_BTN_fast.setOnClickListener(v -> openGameScreen(250, true));
        menu_BTN_sensor.setOnClickListener(v -> openSensorGameScreen());
        menu_BTN_records.setOnClickListener(v -> openRecordsScreen());

    }
    private void findViews() {
        menu_BTN_fast = findViewById(R.id.menu_BTN_fast);
        menu_BTN_slow = findViewById(R.id.menu_BTN_slow);
        menu_BTN_sensor = findViewById(R.id.menu_BTN_sensor);
        menu_BTN_records = findViewById(R.id.menu_BTN_records);
    }

    private void openGameScreen(int falling_delay, boolean isFast) {
        Intent gameIntent = new Intent(this, MainActivity.class);
        gameIntent.putExtra("IsSensor", false);
        gameIntent.putExtra(MainActivity.KEY_FALL_ROCKS_DELAY_MS, falling_delay);
        gameIntent.putExtra(MainActivity.KEY_GENERATE_ROCKS_DELAY_MS, falling_delay*2);
        gameIntent.putExtra("IsFast", isFast);
        startActivity(gameIntent);
        finish();
    }
    private void openSensorGameScreen() {
        Intent gameIntent = new Intent(this, MainActivity.class);
        gameIntent.putExtra("IsSensor", true);
        startActivity(gameIntent);
        finish();
    }
    private void openRecordsScreen() {
        Intent gameIntent = new Intent(this, ScoreActivity.class);
        startActivity(gameIntent);
        finish();
    }
}