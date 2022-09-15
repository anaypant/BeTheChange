package com.example.btc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView username = (TextView) findViewById(R.id.userNameField);
        TextView password = (TextView) findViewById(R.id.passwordField);
        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.loginButton);
        MaterialButton signUpBtn = (MaterialButton) findViewById(R.id.SignUpBtn);
        
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pwd = password.getText().toString();
                if (user.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkUser = db.checkUser(user);
                    if (!checkUser){
                        Toast.makeText(LoginActivity.this, "Welcome " + user + "!", Toast.LENGTH_SHORT).show();
                        //handler
                        db.insertData(user, pwd);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },0);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Username already in use.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}