package com.example.carendlessgame.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.carendlessgame.Interfaces.StepCallback;

public class StepDetector {
    private final Sensor sensor;
    private final SensorManager sensorManager;
    private final StepCallback stepCallback;
    private boolean tiltedLeft = false;
    private boolean tiltedRight = false;
    private boolean tiltedUp = false;
    private boolean tiltedDown = false;
    private SensorEventListener sensorEventListener;

    public StepDetector(Context context, StepCallback stepCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallback = stepCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                onSensorChangedX(x);
                onSensorChangedY(y-7);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    public void onSensorChangedX(float x) {
        if (x < -5 && !tiltedLeft && !tiltedRight) {
            stepCallback.stepX(1);
            tiltedLeft = true;
        }
        // Check if the phone is tilted right
        else if (x > 5 && !tiltedRight && !tiltedLeft) {
            stepCallback.stepX(-1);
            tiltedRight = true;
        }
        // Check if the phone is back in the center
        else if (x > -5 && x < 5) {
            tiltedLeft = false;
            tiltedRight = false;
        }
    }
    public void onSensorChangedY(float y) {
        if (y < -4 && !tiltedDown && !tiltedUp) {
            stepCallback.stepY(-1);
            tiltedDown = true;
        }
        else if (y > 2 && !tiltedUp && !tiltedDown) {
            stepCallback.stepY(1);
            tiltedUp = true;
        }
        else if (y > -2 && y < 2) {
            tiltedDown = false;
            tiltedUp = false;
        }
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }

}
