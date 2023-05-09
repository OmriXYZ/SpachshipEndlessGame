package com.example.carendlessgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoseActivity extends AppCompatActivity {

    Button lose_BTN_menu, lose_BTN_records;
    TextView lose_TXT_meters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);
        findViews();
        lose_BTN_menu.setOnClickListener(v -> openMenuScreen());
        lose_BTN_records.setOnClickListener(v -> openRecordsScreen());
        Intent prevIntent = getIntent();
        int distance = prevIntent.getIntExtra("Distance", 0);
        lose_TXT_meters.setText("You Lose with " + distance + " meters");
    }
    private void findViews() {
        lose_BTN_menu = findViewById(R.id.lose_BTN_menu);
        lose_BTN_records = findViewById(R.id.lose_BTN_records);
        lose_TXT_meters = findViewById(R.id.lose_TXT_meters);
    }

    private void openRecordsScreen() {
        Intent gameIntent = new Intent(this, ScoreActivity.class);
        startActivity(gameIntent);
        finish();
    }
    private void openMenuScreen() {
        Intent gameIntent = new Intent(this, MenuActivity.class);
        startActivity(gameIntent);
        finish();
    }
}