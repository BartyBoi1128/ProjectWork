package com.example.projectwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity{
    EditText etUsername, etPassword, etConfirmPassword, etEmail;
    TextView RegisterHere, btnBack;
    Button btnRegister;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        etUsername = (EditText) findViewById(R.id.fname);
        etPassword = (EditText) findViewById(R.id.password);
        etConfirmPassword = (EditText) findViewById(R.id.confirmpassword);
        etEmail = (EditText) findViewById(R.id.email);
        RegisterHere = findViewById(R.id.register);
        btnRegister = findViewById(R.id.registerbutt);
        btnBack = findViewById(R.id.backbtn);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view -> {
            createUser();
        });

        btnBack.setOnClickListener(view ->{
            startActivity(new Intent(RegisterUser.this, LoginUser.class));
        });
    }
    private void createUser(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String username = etUsername.getText().toString();

        if (TextUtils.isEmpty(username)){
            etUsername.setError("Please provide a Username");
            etUsername.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            etPassword.setError("Please provide a password");
            etPassword.requestFocus();
        }else if(TextUtils.isEmpty(confirmPassword)){
            etConfirmPassword.setError("Please confirm your password");
            etConfirmPassword.requestFocus();
        }
        else if(password.length()<6){
            etPassword.setError("Password must be over 6 characters long");
            etPassword.requestFocus();
        }
        else if (TextUtils.isEmpty(email)){
            etEmail.setError("Please set an email");
            etEmail.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please set a VALID email smh...");
            etEmail.requestFocus();
        }
        else if(password.equals(confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterUser.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        User user = new User(username,0, email);

                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                        startActivity(new Intent(RegisterUser.this, LoginUser.class));
                    } else {
                        Toast.makeText(RegisterUser.this, "User registration failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            etConfirmPassword.setError("Make sure the two passwords you entered match");
            etConfirmPassword.requestFocus();
        }
    }
}

