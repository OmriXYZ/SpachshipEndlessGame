package com.example.carendlessgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carendlessgame.Fragments.ListFragment;
import com.example.carendlessgame.Fragments.MapFragment;
import com.example.carendlessgame.Interfaces.CallBack_SendClick;

public class ScoreActivity extends AppCompatActivity {

    private ListFragment listFragment;
    private MapFragment mapFragment;

    private Button score_BTN_menu;

    private CallBack_SendClick callBack_sendClick = new CallBack_SendClick() {
        @Override
        public void userNameChosen(String name) {
            mapFragment.zoomOnRecord(name);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        findViews();
        initFragments();
        beginTransactions();

        score_BTN_menu.setOnClickListener(v -> openMenuScreen());

    }

    private void findViews() {
        score_BTN_menu = findViewById(R.id.score_BTN_menu);
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_map, mapFragment).commit();
    }

    private void initFragments() {
        listFragment = new ListFragment();
        listFragment.setCallBack_sendClick(callBack_sendClick);
        mapFragment = new MapFragment();
    }

    private void openMenuScreen() {
        Intent gameIntent = new Intent(this, MenuActivity.class);
        startActivity(gameIntent);
        finish();
    }
}