package com.example.projectwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//implements OnMapReadyCallback?
public class Play extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private static LatLng WHEREAMI;
    Button Back;
    private GoogleMap map;
    private LocationListener DOYOUHEARTHAT;
    private LocationManager HearingAids;
    private int intRadius;
    private double distance;
    private final long MIN_TIME = 1000; //1 second
    private final long MIN_DIST = 5; //5 meters
    FusedLocationProviderClient locationgetter;
    LocationRequest requestingLocation;
    LocationCallback locationCallBack;
    public LatLng RandomLocation;
    private double randomLongitude = 0;
    private double randomLatitude = 0;
    private double multiplier;
    private Marker marker;
    FirebaseAuth mAuth;
    private List<Marker> milkList = new ArrayList<Marker>();
    private Location location;

    //public Bitmap milkImage = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("milkcarton", "drawable", getPackageName()));
    //public Bitmap resizedMilk = Bitmap.createScaledBitmap(milkImage,82,68,false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        mAuth = FirebaseAuth.getInstance();

        requestingLocation = new LocationRequest();

        requestingLocation.setInterval(30000);
        requestingLocation.setFastestInterval(50000);
        requestingLocation.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                location = locationResult.getLastLocation();
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
                if (location != null) {
                    WHEREAMI = new LatLng(location.getLatitude(), location.getLongitude());
                    if (marker == null) {
                        marker = map.addMarker(new MarkerOptions().position(getWHEREAMI()).title("Milkman"));
                    } else {
                        marker.setPosition(WHEREAMI);

                    }
                    map.moveCamera(CameraUpdateFactory.newLatLng(getWHEREAMI()));
                }
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
        if (location != null) {
            WHEREAMI = new LatLng(location.getLatitude(), location.getLongitude());

            if (marker == null) {
                marker = map.addMarker(new MarkerOptions().position(getWHEREAMI()).title("Milkman"));
                spawnMilk();
            } else {
                marker.setPosition(WHEREAMI);
            }
            map.moveCamera(CameraUpdateFactory.newLatLng(getWHEREAMI()));
            if(milkList.size() > 0) {
                for (int i=0; i< milkList.size(); i++){
                    LatLng temp = milkList.get(i).getPosition();
                    distance = Math.sqrt(((temp.latitude - WHEREAMI.latitude)*(temp.latitude - WHEREAMI.latitude)) - ((temp.longitude-WHEREAMI.longitude)*(temp.longitude-WHEREAMI.longitude)));
                    System.out.println(distance);
                }
            }
        }
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

    //Padi's code
    /*public Location RandomLocation(){
        multiplier = 0.09009009*multiplier;
        randomLongitude = WHEREAMI.longitude + multiplier;
        randomLatitude = WHEREAMI.latitude + multiplier;
        Location RandomLocation = new Location("network");
        RandomLocation.setLongitude(randomLongitude);
        RandomLocation.setLatitude(randomLatitude);
        return RandomLocation;
    }

    public void randomWithRange(){
        int max = Settings.intRadius;
        int min = 1;
        int range = (max - min) + 1;
        multiplier =  (int)(Math.random() * range) + min;
        int temp = (int)(Math.random()*max)+1;
    }*/

    public static LatLng getWHEREAMI() {
        return WHEREAMI;
    }

    public void setWHEREAMI(LatLng WHEREAMI) {
        this.WHEREAMI = WHEREAMI;
    }

    public static double getLatitude() {
        return WHEREAMI.latitude;
    }

    public static double getLongitude() {
        return WHEREAMI.longitude;
    }

    private void spawnMilk() {
        //Bitmap milkImage = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("milkcarton", "drawable", getPackageName()));
        Bitmap milkImage = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("milkcarton", "drawable", getPackageName()));
        Bitmap resizedMilk = Bitmap.createScaledBitmap(milkImage, 82 * 2, 68 * 2, false);
        Bitmap milkImage1 = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("milkcarton1", "drawable", getPackageName()));
        Bitmap resizedMilk1 = Bitmap.createScaledBitmap(milkImage, 82 * 3, 68 * 3, false);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = String.valueOf(snapshot.child("radius").getValue());
                System.out.println("value in radius is " + value);
                intRadius = Integer.parseInt(value);
                RandomLocation rand = new RandomLocation();
                for(int i = 0; i <5; i++) {
                    milkList.add(map.addMarker(new MarkerOptions()
                            .position(rand.getRandomLocation(WHEREAMI, intRadius))
                            .title("Milk Carton")
                            .icon(BitmapDescriptorFactory.fromBitmap(resizedMilk))));
                }
                /*
                Marker carton = map.addMarker(new MarkerOptions()
                        .position(rand.getRandomLocation(WHEREAMI, intRadius))
                        .title("Milk Carton")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedMilk)));
                //rand = new RandomLocation();
                Marker carton2 = map.addMarker(new MarkerOptions()
                        .position(rand.getRandomLocation(WHEREAMI, intRadius))
                        .title("Milk Carton")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedMilk)));*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*for(int i = 0;i<5;i++) {
            RandomLocation rand = new RandomLocation();
            milkList.add(map.addMarker(new MarkerOptions()
                    .position(rand.getRandomLocation(WHEREAMI, Settings.intRadius))
                    .title("Milk Carton")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedMilk))));
        }*/
        /*for(int i = 0;i<5;i++) {
            RandomLocation rand = new RandomLocation();
            Marker carton = map.addMarker(new MarkerOptions()
                    .position(rand.getRandomLocation(WHEREAMI, Settings.intRadius))
                    .title("Milk Carton")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedMilk)));
            milkList.add(carton);
            //System.out.println("Bart is stinky");
        }*/

    }
}