package com.anaypant.qrated;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anaypant.qrated.Interfaces.FirebaseStringCallback;
import com.anaypant.qrated.R;

import androidx.appcompat.app.AppCompatActivity;

import com.anaypant.qrated.Interfaces.FirebaseBoolCallback;
import com.anaypant.qrated.utils.baseUtils;
import com.anaypant.qrated.utils.firebaseUtils;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView usernameField, passwordField, emailField;
    MaterialButton loginButton, signUpButton, forgotPasswordButton;
    String email, user, pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Basic setting up view stuff
        passwordField = findViewById(R.id.passwordField);
        usernameField = findViewById(R.id.userNameField);

        signUpButton = findViewById(R.id.SignUpBtn);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordButton = findViewById(R.id.pwdButton);


        // If user is already logged in, send them to main instantly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            },0);
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // go to sign up btn
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(intent);
                        finish();
                    }

                },0);

            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                String user = usernameField.getText().toString().trim();
                if (user.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseUtils.getEmailFromDisplayName(user, new FirebaseStringCallback() {
                    @Override
                    public void onStringCallback(String s) {
                        if(s.equals("%null%")){
                            Toast.makeText(LoginActivity.this, "Couldn't find account for username " + user, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        email = s;
                        firebaseUtils.checkIfEmailExists(email, new FirebaseBoolCallback() {
                            @Override
                            public void onBoolCallback(boolean value) {
                                if(value){
                                    //email doesn't exist
                                    firebaseUtils.resetUserPassword(email);
                                    Toast.makeText(LoginActivity.this, "Check email for reset.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            

            @Override
            public void onClick(View view) {
                String user = usernameField.getText().toString().trim();
                String pwd = passwordField.getText().toString().trim();
                //need to check if email exists in database
                firebaseUtils.getEmailFromDisplayName(user, new FirebaseStringCallback() {
                    @Override
                    public void onStringCallback(String s) {
                        String email;
                        if(s.equals("%null%")){
                            Toast.makeText(LoginActivity.this, "No account with username " + user + " found.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            email = s;
                            if(baseUtils.passedNullChecks(email, user, pwd, LoginActivity.this)){
                                firebaseUtils.logInUser(email, pwd, user, LoginActivity.this, new FirebaseBoolCallback() {
                                    @Override
                                    public void onBoolCallback(boolean value) {
                                        if(value){
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
                                            Toast.makeText(LoginActivity.this, "Username and email don't match.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });


            }
        });

    }


}