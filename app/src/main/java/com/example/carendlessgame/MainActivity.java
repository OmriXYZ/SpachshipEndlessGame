package com.example.carendlessgame;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.carendlessgame.GameManager.NUM_COLUMNS;
import static com.example.carendlessgame.GameManager.NUM_ROWS;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carendlessgame.Interfaces.StepCallback;
import com.example.carendlessgame.Utilities.GpsControl;
import com.example.carendlessgame.Utilities.SignalGenerator;
import com.example.carendlessgame.Utilities.SoundControl;
import com.example.carendlessgame.Utilities.StepDetector;
import com.example.carendlessgame.models.Spaceship;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView[] main_IMG_spaceships;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[][] main_IMG_rocks;
    private FloatingActionButton main_BTN_left;
    private FloatingActionButton main_BTN_right;
    private TextView main_TXT_odometer;
    private TextView main_TXT_speed;
    private final Random random = new Random();
    public static final String KEY_FALL_ROCKS_DELAY_MS = "KEY_FALL_DELAY";
    public static final String KEY_GENERATE_ROCKS_DELAY_MS = "KEY_GENERATE_DELAY";
    private final int[] rocksFirstRow = new int[]{0, 0, 0, 0, 0};
    private final Handler handler = new Handler();
    private Spaceship spaceship;
    private boolean isPause = false;
    private final String rock = "rock";
    private final String coin = "coin";
    private GameManager gameManager;
    private StepDetector stepDetector;
    private boolean moveToLoseScreen = true;
    private int lastSensorX = 0;
    private GpsControl gpsControl;
    private SoundControl soundControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        main_IMG_rocks = new ShapeableImageView[NUM_ROWS][NUM_COLUMNS];
        findViews();
        Intent prevIntent = getIntent();
        boolean isSensor = prevIntent.getBooleanExtra("IsSensor", false);
        if (!isSensor) {
            int fall_delay = prevIntent.getIntExtra(KEY_FALL_ROCKS_DELAY_MS, 0);
            int generate_delay = prevIntent.getIntExtra(KEY_GENERATE_ROCKS_DELAY_MS, 0);
            boolean isFast = prevIntent.getBooleanExtra("IsFast", true);
            if (isFast) {
                main_TXT_speed.setText("Speed: fast");
                main_TXT_speed.setTextColor(Color.rgb(181, 223, 255));
            } else {
                main_TXT_speed.setText("Speed: slow");
                main_TXT_speed.setTextColor(Color.rgb(255, 181, 181));
            }
            gameManager = new GameManager(fall_delay, generate_delay);
            main_BTN_left.setOnClickListener(v -> MoveTheSpaceship(-1));
            main_BTN_right.setOnClickListener(v -> MoveTheSpaceship(1));
        } else {
            initStepDetector();
            gameManager = new GameManager();
            main_BTN_left.setVisibility(INVISIBLE);
            main_BTN_right.setVisibility(INVISIBLE);
        }

        soundControl = new SoundControl(this);

        spaceship = new Spaceship(NUM_COLUMNS / 2);

        handler.postDelayed(generateRocks, 75);
        handler.postDelayed(fallingRocks, 75);

        gpsControl = new GpsControl(this);
    }

    private void initStepDetector() {
        stepDetector = new StepDetector(this, new StepCallback() {
            @Override
            public void stepX() {
                MoveTheSpaceshipBySensors(stepDetector.getX());
            }

            @Override
            public void stepY() {
                int stepY = stepDetector.getY() - 4;
                if (stepY >= -2 && stepY <= 2)
                    gameManager.changeDelaysByStepY((stepY) * 70);
                switch (stepY) {
                    case 0:
                        main_TXT_speed.setText("Speed: moderate");
                        main_TXT_speed.setTextColor(Color.rgb(255, 255, 255));
                        break;
                    case -1:
                        main_TXT_speed.setText("Speed: fast");
                        main_TXT_speed.setTextColor(Color.rgb(181, 223, 255));
                        break;
                    case -2:
                        main_TXT_speed.setText("Speed: very fast");
                        main_TXT_speed.setTextColor(Color.rgb(122, 198, 255));
                        break;
                    case 1:
                        main_TXT_speed.setText("Speed: slow");
                        main_TXT_speed.setTextColor(Color.rgb(255, 181, 181));
                        break;
                    case 2:
                        main_TXT_speed.setText("Speed: very slow");
                        main_TXT_speed.setTextColor(Color.rgb(255, 122, 122));
                        break;

                }
            }

            @Override
            public void stepZ() {
                // Pass
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if (gameManager.isSensorGame)
            stepDetector.start();
        if (isPause) {
            handler.postDelayed(generateRocks, 0);
            handler.postDelayed(fallingRocks, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        if (gameManager.isSensorGame)
            stepDetector.stop();
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

    private final Runnable fallingRocks = new Runnable() {
        @Override
        public void run() {
            main_TXT_odometer.setText("Distance counter: " + gameManager.addOneDistance());
            for (int row = NUM_ROWS - 1; row >= 0; row--) {
                for (int col = 0; col < NUM_COLUMNS; col++) {
                    ShapeableImageView checkRock = main_IMG_rocks[row][col];
                    if (checkRock.getVisibility() == VISIBLE && row == NUM_ROWS - 1)
                        checkRock.setVisibility(INVISIBLE);
                    else if (checkRock.getVisibility() == VISIBLE) {
                        checkRock.setVisibility(INVISIBLE);
                        checkRock = main_IMG_rocks[row + 1][col];
                        Drawable lastDraw = main_IMG_rocks[row][col].getDrawable();
                        String lastTag = (String) main_IMG_rocks[row][col].getTag();
                        checkRock.setImageDrawable(lastDraw);
                        checkRock.setTag(lastTag);
                        checkRock.setVisibility(VISIBLE);
                    }
                    if (rocksFirstRow[col] == 1 && row == 0) {
                        if (random.nextInt(5) == 0) {
                            main_IMG_rocks[0][col].setImageResource(R.drawable.coin);
                            checkRock.setTag(coin);
                        } else {
                            main_IMG_rocks[0][col].setImageResource(R.drawable.rock);
                            checkRock.setTag(rock);
                        }
                        main_IMG_rocks[0][col].setVisibility(VISIBLE);
                        rocksFirstRow[col] = 0;
                    }
                }
            }
            checkSpaceshipCollision();
            handler.postDelayed(this, gameManager.getFall_rocks_delay_ms());
        }
    };

    private final Runnable generateRocks = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(generateRocks, gameManager.getGenerate_rocks_delay_ms());
            int theNextRandomRock = random.nextInt(NUM_COLUMNS);
            rocksFirstRow[theNextRandomRock] = 1;
        }
    };

    private void findViews() {
        main_IMG_spaceships = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_spaceship0),
                findViewById(R.id.main_IMG_spaceship1),
                findViewById(R.id.main_IMG_spaceship2),
                findViewById(R.id.main_IMG_spaceship3),
                findViewById(R.id.main_IMG_spaceship4)};
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);

        main_IMG_rocks[0][0] = findViewById(R.id.main_IMG_rock00);
        main_IMG_rocks[0][1] = findViewById(R.id.main_IMG_rock01);
        main_IMG_rocks[0][2] = findViewById(R.id.main_IMG_rock02);
        main_IMG_rocks[0][3] = findViewById(R.id.main_IMG_rock03);
        main_IMG_rocks[0][4] = findViewById(R.id.main_IMG_rock04);

        main_IMG_rocks[1][0] = findViewById(R.id.main_IMG_rock10);
        main_IMG_rocks[1][1] = findViewById(R.id.main_IMG_rock11);
        main_IMG_rocks[1][2] = findViewById(R.id.main_IMG_rock12);
        main_IMG_rocks[1][3] = findViewById(R.id.main_IMG_rock13);
        main_IMG_rocks[1][4] = findViewById(R.id.main_IMG_rock14);

        main_IMG_rocks[2][0] = findViewById(R.id.main_IMG_rock20);
        main_IMG_rocks[2][1] = findViewById(R.id.main_IMG_rock21);
        main_IMG_rocks[2][2] = findViewById(R.id.main_IMG_rock22);
        main_IMG_rocks[2][3] = findViewById(R.id.main_IMG_rock23);
        main_IMG_rocks[2][4] = findViewById(R.id.main_IMG_rock24);

        main_IMG_rocks[3][0] = findViewById(R.id.main_IMG_rock30);
        main_IMG_rocks[3][1] = findViewById(R.id.main_IMG_rock31);
        main_IMG_rocks[3][2] = findViewById(R.id.main_IMG_rock32);
        main_IMG_rocks[3][3] = findViewById(R.id.main_IMG_rock33);
        main_IMG_rocks[3][4] = findViewById(R.id.main_IMG_rock34);

        main_IMG_rocks[4][0] = findViewById(R.id.main_IMG_rock40);
        main_IMG_rocks[4][1] = findViewById(R.id.main_IMG_rock41);
        main_IMG_rocks[4][2] = findViewById(R.id.main_IMG_rock42);
        main_IMG_rocks[4][3] = findViewById(R.id.main_IMG_rock43);
        main_IMG_rocks[4][4] = findViewById(R.id.main_IMG_rock44);

        main_IMG_rocks[5][0] = findViewById(R.id.main_IMG_rock50);
        main_IMG_rocks[5][1] = findViewById(R.id.main_IMG_rock51);
        main_IMG_rocks[5][2] = findViewById(R.id.main_IMG_rock52);
        main_IMG_rocks[5][3] = findViewById(R.id.main_IMG_rock53);
        main_IMG_rocks[5][4] = findViewById(R.id.main_IMG_rock54);

        main_IMG_rocks[6][0] = findViewById(R.id.main_IMG_rock60);
        main_IMG_rocks[6][1] = findViewById(R.id.main_IMG_rock61);
        main_IMG_rocks[6][2] = findViewById(R.id.main_IMG_rock62);
        main_IMG_rocks[6][3] = findViewById(R.id.main_IMG_rock63);
        main_IMG_rocks[6][4] = findViewById(R.id.main_IMG_rock64);

        main_IMG_rocks[7][0] = findViewById(R.id.main_IMG_rock70);
        main_IMG_rocks[7][1] = findViewById(R.id.main_IMG_rock71);
        main_IMG_rocks[7][2] = findViewById(R.id.main_IMG_rock72);
        main_IMG_rocks[7][3] = findViewById(R.id.main_IMG_rock73);
        main_IMG_rocks[7][4] = findViewById(R.id.main_IMG_rock74);

        main_TXT_odometer = findViewById(R.id.main_TXT_odemeter);
        main_TXT_speed = findViewById(R.id.main_TXT_speed);

    }

    private void MoveTheSpaceship(int positionIndex) {
        int newSpaceshipPosition = spaceship.getSpaceshipPosition() + positionIndex;
        boolean checkOutOfGrid = newSpaceshipPosition >= NUM_COLUMNS || newSpaceshipPosition < 0;
        if (checkOutOfGrid) return;
        main_IMG_spaceships[spaceship.getSpaceshipPosition()].setVisibility(INVISIBLE);
        spaceship.setSpaceshipPosition(newSpaceshipPosition);
        main_IMG_spaceships[spaceship.getSpaceshipPosition()].setVisibility(VISIBLE);
        checkSpaceshipCollision();
    }

    private void MoveTheSpaceshipBySensors(int sensorX) {
        if (sensorX == lastSensorX) return;
        else if (sensorX > 3)
            MoveTheSpaceship(1);
        else if (sensorX < -3)
            MoveTheSpaceship(-1);
        lastSensorX = sensorX;
    }

    public void checkSpaceshipCollision() {
        ShapeableImageView rock_toCheck = main_IMG_rocks[NUM_ROWS - 1][spaceship.getSpaceshipPosition()];
        if (rock_toCheck.getTag() != null && rock_toCheck.getVisibility() == VISIBLE) {
            rock_toCheck.setVisibility(INVISIBLE);
            if (rock_toCheck.getTag().equals(rock))
                onSpaceshipHurt();
            else if (rock_toCheck.getTag().equals(coin))
                onSpaceshipEarnCoin();

        }
    }

    private void onSpaceshipEarnCoin() {
        soundControl.playCoinSound();
        gameManager.earnCoin();
        main_TXT_odometer.setText("Distance counter: " + gameManager.getDistance());
        SignalGenerator.getInstance().showToast("Earn Coin!!", 500);
        SignalGenerator.getInstance().vibrate(50);
    }

    public void onSpaceshipHurt() {
        soundControl.playKnockSound();
        SignalGenerator.getInstance().showToast("Crash!!", 500);
        SignalGenerator.getInstance().vibrate(100);
        if (gameManager.getLives() - 1 <= 0)
            onSpaceshipCrash();
        else {
            gameManager.decreaseLive();
            main_IMG_hearts[2 - gameManager.getLives()].setVisibility(INVISIBLE);
        }
    }

    public void onSpaceshipCrash() {
        if (moveToLoseScreen) {
            gameManager.lose(gpsControl.getLon(), gpsControl.getLat());
            openLoseScreen(gameManager.getDistance());
            moveToLoseScreen = false;
        }
    }

    private void openLoseScreen(int distance) {
        Intent loseIntent = new Intent(this, LoseActivity.class);
        loseIntent.putExtra("Distance", distance);
        startActivity(loseIntent);
        finish();
    }

}


