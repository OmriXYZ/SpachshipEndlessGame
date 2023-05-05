package com.example.carendlessgame;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carendlessgame.Fragments.ListFragment;
import com.example.carendlessgame.Fragments.MapFragment;
import com.example.carendlessgame.Interfaces.CallBack_SendClick;

public class ScoreActivity extends AppCompatActivity {

    private ListFragment listFragment;
    private MapFragment mapFragment;

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

        initFragments();
        beginTransactions();
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
}