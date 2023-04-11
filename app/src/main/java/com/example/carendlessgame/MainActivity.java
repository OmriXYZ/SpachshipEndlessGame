package com.example.carendlessgame;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView[] main_IMG_spaceships;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[][] main_IMG_rocks;
    private FloatingActionButton main_BTN_left;
    private FloatingActionButton main_BTN_right;
    private Random random = new Random();
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLUMNS = 3;
    private static final int LEFT_POS = 0, CENTER_POS = 1, RIGHT_POS = 2;
    private final int FALL_ROCKS_DELAY_MS = 500;
    private final int GENERATE_ROCKS_DELAY_MS = 1000;
    private int theNextRandomRock;
    private int[] rocksFirstRow = new int[] {0, 0, 0};
    private final Handler handler = new Handler();
    private Spaceship spaceship;
    private static final int MAX_LIVE = 3;
    private boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_IMG_rocks = new ShapeableImageView[6][3];
        findViews();
        spaceship = new Spaceship(MAX_LIVE, CENTER_POS);
        main_BTN_left.setOnClickListener(v -> MoveTheSpaceship(-1));
        main_BTN_right.setOnClickListener(v -> MoveTheSpaceship(1));
        handler.postDelayed(generateRocks, 0);
        handler.postDelayed(fallingRocks, 0);
    }

    protected void onResume() {
        super.onResume();
        if (isPause) {
            handler.postDelayed(generateRocks, 0);
            handler.postDelayed(fallingRocks, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(generateRocks);
        handler.removeCallbacks(fallingRocks);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(generateRocks);
        handler.removeCallbacks(fallingRocks);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }

    private Runnable fallingRocks = new Runnable() {
        @Override
        public void run() {
            for (int row = NUM_ROWS-1; row >= 0; row--) {
                for (int col = 0; col < NUM_COLUMNS; col++) {
                    ShapeableImageView checkRock = main_IMG_rocks[row][col];
                    if (checkRock.getVisibility() == VISIBLE && row == NUM_ROWS-1)
                        checkRock.setVisibility(INVISIBLE);
                    else if(checkRock.getVisibility() == VISIBLE) {
                        checkRock.setVisibility(INVISIBLE);
                        checkRock = main_IMG_rocks[row+1][col];
                        checkRock.setVisibility(VISIBLE);
                    }
                    if (rocksFirstRow[col] == 1 && row==0) {
                        main_IMG_rocks[0][col].setVisibility(VISIBLE);
                        rocksFirstRow[col] = 0;
                    }
                }
            }
            checkSpaceshipCollison();
            handler.postDelayed(this, FALL_ROCKS_DELAY_MS);
        }
    };

    private Runnable generateRocks = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(generateRocks, GENERATE_ROCKS_DELAY_MS);
            theNextRandomRock = random.nextInt(3);
            rocksFirstRow[theNextRandomRock] = 1;
        }
    };
    private void findViews() {
        main_IMG_spaceships = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_spaceship0),
                findViewById(R.id.main_IMG_spaceship1),
                findViewById(R.id.main_IMG_spaceship2)};
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);

        main_IMG_rocks[0][0] = findViewById(R.id.main_IMG_rock00);
        main_IMG_rocks[0][1] = findViewById(R.id.main_IMG_rock01);
        main_IMG_rocks[0][2] = findViewById(R.id.main_IMG_rock02);

        main_IMG_rocks[1][0] = findViewById(R.id.main_IMG_rock10);
        main_IMG_rocks[1][1] = findViewById(R.id.main_IMG_rock11);
        main_IMG_rocks[1][2] = findViewById(R.id.main_IMG_rock12);

        main_IMG_rocks[2][0] = findViewById(R.id.main_IMG_rock20);
        main_IMG_rocks[2][1] = findViewById(R.id.main_IMG_rock21);
        main_IMG_rocks[2][2] = findViewById(R.id.main_IMG_rock22);

        main_IMG_rocks[3][0] = findViewById(R.id.main_IMG_rock30);
        main_IMG_rocks[3][1] = findViewById(R.id.main_IMG_rock31);
        main_IMG_rocks[3][2] = findViewById(R.id.main_IMG_rock32);

        main_IMG_rocks[4][0] = findViewById(R.id.main_IMG_rock40);
        main_IMG_rocks[4][1] = findViewById(R.id.main_IMG_rock41);
        main_IMG_rocks[4][2] = findViewById(R.id.main_IMG_rock42);

        main_IMG_rocks[5][0] = findViewById(R.id.main_IMG_rock50);
        main_IMG_rocks[5][1] = findViewById(R.id.main_IMG_rock51);
        main_IMG_rocks[5][2] = findViewById(R.id.main_IMG_rock52);
    }

    private void MoveTheSpaceship(int positionIndex) {
        int newSpaceshipPosition = spaceship.spaceshipPosition + positionIndex;
        boolean checkOutOfGrid = newSpaceshipPosition > RIGHT_POS || newSpaceshipPosition < LEFT_POS;
        if (checkOutOfGrid) return;
        main_IMG_spaceships[spaceship.spaceshipPosition].setVisibility(INVISIBLE);
        spaceship.spaceshipPosition = newSpaceshipPosition;
        main_IMG_spaceships[spaceship.spaceshipPosition].setVisibility(VISIBLE);
        checkSpaceshipCollison();
    }

    public void checkSpaceshipCollison() {
        ShapeableImageView rock_toCheck = main_IMG_rocks[NUM_ROWS-1][spaceship.spaceshipPosition];
        if (rock_toCheck.getVisibility() == VISIBLE) {
            rock_toCheck.setVisibility(INVISIBLE);
            onSpaceshipHurt();
        }
    }

    public void onSpaceshipHurt() {
        fireToastAndVibrate();
        if (spaceship.lives - 1 <= 0)
            onSpaceshipCrash();
        else {
            spaceship.lives--;
            main_IMG_hearts[2- spaceship.lives].setVisibility(INVISIBLE);
        }
    }

    public void onSpaceshipCrash() {
        spaceship.lives = MAX_LIVE;
        main_IMG_spaceships[spaceship.spaceshipPosition].setVisibility(INVISIBLE);
        spaceship.spaceshipPosition = CENTER_POS;
        main_IMG_spaceships[CENTER_POS].setVisibility(VISIBLE);
        for (ShapeableImageView img_heart : main_IMG_hearts)
            img_heart.setVisibility(VISIBLE);
        for (ShapeableImageView[] rocks_inRow: main_IMG_rocks)
            for (ShapeableImageView rock: rocks_inRow) {
                rock.setVisibility(INVISIBLE);
            }

    }

    public class Spaceship {
        private int lives;
        private int spaceshipPosition;

        public Spaceship(int lives, int spaceshipPosition) {
            this.lives = lives;
            this.spaceshipPosition = spaceshipPosition;
        }
    }

    public void fireToastAndVibrate() {
        Toast.makeText(getApplicationContext(),"Crash!!",Toast.LENGTH_LONG).show();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
    }
}


