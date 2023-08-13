package com.example.furnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText txtEmail, txtPass;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        auth = FirebaseAuth.getInstance();
    }

    public void toRegistration(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    public void login(View view) {
        String sEmail = txtEmail.getText().toString();
        String sPass = txtPass.getText().toString();
        auth = FirebaseAuth.getInstance();
        if (sEmail.equals("")) {
            Toast.makeText(getApplicationContext(), "Please provide an email", Toast.LENGTH_LONG).show();
            return;
        }
        if (sPass.equals("")) {
            Toast.makeText(getApplicationContext(), "Please provide a password", Toast.LENGTH_LONG).show();
            return;
        }
        if (sPass.length() < 8) {
            Toast.makeText(getApplicationContext(), "Password too short", Toast.LENGTH_LONG).show();
            return;
        }
        auth.signInWithEmailAndPassword(sEmail, sPass)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}