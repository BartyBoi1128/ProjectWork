package com.example.projectwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity {

    Button MainMenu;
    Button Radius;
    private EditText editText;
    Button TextInput;
    static String radius = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editText = findViewById(R.id.editbox);
        TextInput = findViewById(R.id.TextInput);

        editText.setVisibility(View.GONE);
        TextInput.setVisibility(View.GONE);


        Radius = findViewById(R.id.Radius);

        Radius.setOnClickListener(view -> {
                editText.setVisibility(View.VISIBLE);
                TextInput.setVisibility(View.VISIBLE);
        });

        TextInput.setOnClickListener(
                view -> {radius = editText.getText().toString();
                    editText.setVisibility(View.GONE);
                    TextInput.setVisibility(View.GONE);});

        MainMenu = findViewById(R.id.mainmenu);

        MainMenu.setOnClickListener(view ->{
            startActivity(new Intent(Settings.this, MainActivity.class));
        });
    }
    static int intRadius = Integer.parseInt(radius);
    public static int GetRadius(){
        return intRadius;
    }

    public static int SetRadius(){
        intRadius = intRadius;
        return intRadius;
    }
}