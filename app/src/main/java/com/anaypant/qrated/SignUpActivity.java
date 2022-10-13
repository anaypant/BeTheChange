package com.anaypant.qrated;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.anaypant.qrated.Interfaces.FirebaseBoolCallback;
import com.anaypant.qrated.utils.baseUtils;
import com.anaypant.qrated.utils.firebaseUtils;
import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends AppCompatActivity {
    TextView usernameField, passwordField, emailField;
    MaterialButton loginButton, signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        usernameField = findViewById(R.id.userNameField);
        signUpButton = findViewById(R.id.SignUpBtn);
        loginButton = findViewById(R.id.loginButton);



        // Makes sure all entries are valid.
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String user = usernameField.getText().toString().trim();
                String pwd = passwordField.getText().toString().trim();

                if(baseUtils.passedNullChecks(email, user, pwd, SignUpActivity.this)){
                    firebaseUtils.createUser(user, email, pwd, SignUpActivity.this, new FirebaseBoolCallback() {
                        @Override
                        public void onBoolCallback(boolean value) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            },0);
                        }
                    });

                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                },0);
            }
        });

    }
}