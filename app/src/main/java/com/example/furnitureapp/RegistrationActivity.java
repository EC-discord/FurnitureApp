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

public class RegistrationActivity extends AppCompatActivity {
    EditText txtEmail, txtPass, txtConfirmPass;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        txtConfirmPass = findViewById(R.id.txtConfirmPass);
    }

    public void signup(View view) {
        //if the password fields do not match do not let the user register
        String sEmail = txtEmail.getText().toString();
        String sPass = txtPass.getText().toString();
        String sConfirm = txtConfirmPass.getText().toString();

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

        if (!sPass.equals(sConfirm)) {
            Toast.makeText(getApplicationContext(), "Password fields must match", Toast.LENGTH_LONG).show();
            return;
        }
        auth.createUserWithEmailAndPassword(sEmail, sPass)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void toLogin(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }
}