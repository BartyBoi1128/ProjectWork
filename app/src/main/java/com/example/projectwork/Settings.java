package com.example.projectwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    Button MainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MainMenu = findViewById(R.id.mainmenu);

        MainMenu.setOnClickListener(view ->{
            startActivity(new Intent(Settings.this, MainActivity.class));
        });
    }
}