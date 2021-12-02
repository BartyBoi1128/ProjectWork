package com.example.projectwork;

import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLocation extends AppCompatActivity {

    private LatLng WHEREAMI = Play.getWHEREAMI();
    public LatLng RandomLocation;
    private int intRadius = Settings.intRadius;
    private double multiplier = 1;
    private double longitude = Play.getLongitude();
    private double latitude = Play.getLatitude();
    private double randomLongitude = 1;
    private double randomLatitude = 1;


    public LatLng getRandomLocation(LatLng ourPosition, int radius) {
        //Location currentLocation = new Location("");
        //currentLocation.setLatitude(WHEREAMI.latitude);
        //currentLocation.setLongitude(WHEREAMI.longitude);

        double x0 = ourPosition.latitude;
        double y0 = ourPosition.longitude;

        Random random = new Random();

        double radiusInDegrees = radius*1000/111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees*Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        double nowX = x/Math.cos(y0);

        double foundLatitude = nowX + x0;
        double foundLongitude = y + y0;
        LatLng randomLatLng = new LatLng (foundLatitude, foundLongitude);
        return randomLatLng;
    }

}
