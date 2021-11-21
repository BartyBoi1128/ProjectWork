package com.example.projectwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginUser extends AppCompatActivity {
    EditText etLoginEmail, etLoginPassword;
    TextView registerNow, forgotPassNow;
    Button btnLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        etLoginEmail = (EditText) findViewById(R.id.username1);
        etLoginPassword = findViewById(R.id.password1);
        registerNow = findViewById(R.id.register1);
        forgotPassNow = findViewById(R.id.forgotpass1);
        btnLogin = findViewById(R.id.loginbtn1);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });
        registerNow.setOnClickListener(view -> {
            startActivity(new Intent(LoginUser.this, RegisterUser.class));
        });
    }
    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginUser.this, "Log In Succesful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginUser.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(LoginUser.this, "Log in Incomplete", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
