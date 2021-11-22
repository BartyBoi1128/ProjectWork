package com.example.projectwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogOut;
    Button Settings;
    Button Play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

    btnLogOut = findViewById(R.id.buttonLogOut2);
    mAuth = FirebaseAuth.getInstance();

    btnLogOut.setOnClickListener(view ->{
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginUser.class));
    });
    Settings = findViewById(R.id.Settings);

    Settings.setOnClickListener(view ->{
        startActivity(new Intent(MainActivity.this, Settings.class));
    });

        Play = findViewById(R.id.Play);

        Play.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this, Play.class));
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginUser.class));
        }
    }
}