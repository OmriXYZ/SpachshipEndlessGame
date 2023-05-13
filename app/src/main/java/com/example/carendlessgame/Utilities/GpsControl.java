package com.example.carendlessgame.Utilities;

import android.content.Context;

public class GpsControl {

    private final GpsTracker gps;

    public GpsControl(Context context) {
        gps = new GpsTracker(context);
    }

    public double getLat () {
        double lat;
        if(gps.canGetLocation()){
            lat = gps.getLatitude();
            return lat;

        }else{
            SignalGenerator.getInstance().showToast("Cannot get location", 300);
            return 0.0;
        }
    }

    public double getLon () {
        double lon;
        if(gps.canGetLocation()){
            lon = gps.getLongitude();
            return lon;

        }else{
            return 0.0;
        }
    }

}
