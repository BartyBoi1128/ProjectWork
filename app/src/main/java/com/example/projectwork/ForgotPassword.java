package com.example.projectwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText emailEditText;
    private TextView GOBACKKK;
    private Button resetPasswordButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText) findViewById(R.id.EmailForgotPassword);
        resetPasswordButton = (Button) findViewById(R.id.ResetPasswordButton);
        GOBACKKK = (TextView) findViewById(R.id.backbtnForgotPassword);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetPassword();
            }
        });
        GOBACKKK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPassword.this, LoginUser.class));
            }
        });
    }

    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();
        if(email.isEmpty()){
            emailEditText.setError("Write your email here");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Your real email please -_-");
            emailEditText.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()){
                Toast.makeText(ForgotPassword.this, "Check your email Milkman!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ForgotPassword.this, LoginUser.class));
            }
            else{
                Toast.makeText(ForgotPassword.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
            }
            }
        });
    }
}