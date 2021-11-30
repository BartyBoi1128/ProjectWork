package com.example.projectwork;

import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class RandomLocation extends AppCompatActivity {

    private LatLng WHEREAMI = Play.getWHEREAMI();
    public LatLng RandomLocation;
    private int intRadius = Settings.intRadius;
    private double multiplier;
    private double longitude = Play.getLongitude();
    private double latitude = Play.getLatitude();
    private double randomLongitude = 0;
    private double randomLatitude = 0;


    public Location RandomLocation(){
        multiplier = 0.09009009*multiplier;
        randomLongitude = longitude + multiplier;
        randomLatitude = latitude + multiplier;
        Location RandomLocation = new Location("network");
        RandomLocation.setLongitude(randomLongitude);
        RandomLocation.setLatitude(randomLatitude);
        return RandomLocation;
    }

    public void randomWithRange(){
        int max = intRadius;
        int min = 1;
        int range = (max - min) + 1;
        multiplier =  (int)(Math.random() * range) + min;
        int temp = (int)(Math.random()*5)+1;
        if(temp == 1) {
            multiplier = multiplier + 0.2;
        }if(temp == 2){
            multiplier = multiplier + 0.5;
        }if(temp == 3){
            multiplier = multiplier + 0.047;
        }if(temp == 4){
            multiplier = multiplier + 0.78;
        }else {
            multiplier = multiplier + 1.32;
        }
    }
}
