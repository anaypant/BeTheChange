package com.example.btc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class settings extends AppCompatActivity {
    FirebaseAuth auth;
    MaterialButton signOutButton;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        signOutButton = findViewById(R.id.SignOut);
        backButton = findViewById(R.id.backArrow);

        //implement sign out, notis off eventually



        auth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(settings.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                },0);
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sign user out

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(settings.this, "Signed out.", Toast.LENGTH_SHORT).show();
                //go to login page
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(settings.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                },0);
            }
        });


    }
}