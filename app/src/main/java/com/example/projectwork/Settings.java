package com.example.projectwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity {

    Button MainMenu;
    Button Radius;
    private EditText inputtedRadius;
    Button changeRadius;
    static String radius = "0";

    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        inputtedRadius = findViewById(R.id.editbox);
        changeRadius = findViewById(R.id.OKBUTTON);

        inputtedRadius.setVisibility(View.GONE);
        changeRadius.setVisibility(View.GONE);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        mAuth = FirebaseAuth.getInstance();

        Radius = findViewById(R.id.Radius);

        Radius.setOnClickListener(view -> {
                inputtedRadius.setVisibility(View.VISIBLE);
                changeRadius.setVisibility(View.VISIBLE);
        });

        changeRadius.setOnClickListener(
                view -> {radius = inputtedRadius.getText().toString();
                    String BartIsNotATwat = inputtedRadius.getText().toString();
                    SetRadius(Integer.parseInt(BartIsNotATwat));
                    inputtedRadius.setVisibility(View.GONE);
                    changeRadius.setVisibility(View.GONE);});

        MainMenu = findViewById(R.id.mainmenu);

        MainMenu.setOnClickListener(view ->{
            startActivity(new Intent(Settings.this, MainActivity.class));
        });
        //SetRadius(User.getRadius());
    }
    static int intRadius = Integer.parseInt(radius);
    public static int GetRadius(){
        return intRadius;
    }

    public void SetRadius(int inRad){
        reference.child(mAuth.getCurrentUser().getUid()).child("radius").setValue(inRad);
    }
}