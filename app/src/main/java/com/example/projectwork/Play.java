package com.example.projectwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

//implements OnMapReadyCallback?
public class Play extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    Button Back;
    private GoogleMap map;
    private LocationListener DOYOUHEARTHAT;
    private LocationManager HearingAids;
    private final long MIN_TIME = 1000; //1 second
    private final long MIN_DIST = 5; //5 meters
    private LatLng WHEREAMI;
    FusedLocationProviderClient locationgetter;
    LocationRequest requestingLocation;
    LocationCallback locationCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        requestingLocation = new LocationRequest();

        requestingLocation.setInterval(30000);
        requestingLocation.setFastestInterval(50000);
        requestingLocation.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
            }
        };


        Back = findViewById(R.id.BackButton);

        Back.setOnClickListener(view -> startActivity(new Intent(Play.this, MainActivity.class)));
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();

                } else {
                    Toast.makeText(this, "This app requires permission", Toast.LENGTH_SHORT).show();
                    finish();

                }
                break;
        }
    }

    private void updateGPS() {
        locationgetter = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationgetter.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateTheLocation(location);
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        DOYOUHEARTHAT = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                WHEREAMI = new LatLng(location.getLatitude(), location.getLongitude());
                map.addMarker(new MarkerOptions().position(WHEREAMI).title("Milkman"));
                map.moveCamera(CameraUpdateFactory.newLatLng(WHEREAMI));
            }
        };
        HearingAids = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            HearingAids.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, DOYOUHEARTHAT);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateTheLocation(Location location) {
        WHEREAMI = new LatLng(location.getLatitude(), location.getLongitude());
        map.addMarker(new MarkerOptions().position(WHEREAMI).title("Milkman"));
        map.moveCamera(CameraUpdateFactory.newLatLng(WHEREAMI));
    }

    private void locationUpdater() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationgetter.requestLocationUpdates(requestingLocation, locationCallBack, null);
        updateGPS();

    }
}