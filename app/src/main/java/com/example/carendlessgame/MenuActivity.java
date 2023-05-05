package com.example.carendlessgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.carendlessgame.databinding.ActivityMenuBinding;
import com.google.android.material.imageview.ShapeableImageView;

public class MenuActivity extends AppCompatActivity {

    Button menu_BTN_fast, menu_BTN_slow, menu_BTN_sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        menu_BTN_slow.setOnClickListener(v -> openGameScreen(500));
        menu_BTN_fast.setOnClickListener(v -> openGameScreen(250));
        menu_BTN_sensor.setOnClickListener(v -> openSensorGameScreen());

    }
    private void findViews() {
        menu_BTN_fast = findViewById(R.id.menu_BTN_fast);
        menu_BTN_slow = findViewById(R.id.menu_BTN_slow);
        menu_BTN_sensor = findViewById(R.id.menu_BTN_sensor);
    }

    private void openGameScreen(int falling_delay) {
        Intent gameIntent = new Intent(this, MainActivity.class);
        gameIntent.putExtra("IsSensor", false);
        gameIntent.putExtra(MainActivity.KEY_FALL_ROCKS_DELAY_MS, falling_delay);
        gameIntent.putExtra(MainActivity.KEY_GENERATE_ROCKS_DELAY_MS, falling_delay*2);
        startActivity(gameIntent);
        finish();
    }
    private void openSensorGameScreen() {
        Intent gameIntent = new Intent(this, MainActivity.class);
        gameIntent.putExtra("IsSensor", true);
        startActivity(gameIntent);
        finish();
    }
}