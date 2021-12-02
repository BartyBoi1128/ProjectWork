package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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


    btnLogOut = findViewById(R.id.buttonLogOut2);
    mAuth = FirebaseAuth.getInstance();

    btnLogOut.setOnClickListener(view ->{
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginUser.class));
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