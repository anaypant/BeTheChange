package com.example.btc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    DBHelper dbHelper;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    boolean ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView username = (TextView) findViewById(R.id.userNameField);
        TextView password = (TextView) findViewById(R.id.passwordField);
        TextView emailField = findViewById(R.id.emailField);
        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.loginButton);
        MaterialButton signUpBtn = (MaterialButton) findViewById(R.id.SignUpBtn);
        MaterialButton forgotPassword = findViewById(R.id.pwdButton);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String user = username.getText().toString();
                String pwd = password.getText().toString();
                if (email.equals("") || password.equals("") || user.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(email.contains("@") && email.contains("."))) {
                    Toast.makeText(LoginActivity.this, "Email is Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(LoginActivity.this, "Please check your email for verification (spam category)", Toast.LENGTH_LONG).show();
                        promptEmail();

                        Map<String, String> a = new HashMap<>();
                        a.put(email, user);

                        db.collection("id").document("bin").set(a, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("", "DocumentSnapshot successfully written!");
                            }
                        });
                    }

                });


            }

        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                if(emailExists(email)){
                    Toast.makeText(LoginActivity.this, "Please enter valid email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(email != null && !email.equals("") && !emailExists(email)){
                    pwdReset(email);
                    Toast.makeText(LoginActivity.this, "Please check email to reset password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public boolean emailExists(String email){
        ret = false;
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                Log.d("",""+task.getResult().getSignInMethods().size());
                if (task.getResult().getSignInMethods().size() == 0){
                    // email not existed
                    ret = false;
                }else {
                    // email existed
                    ret = true;
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        return ret;
    }
    public void promptEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    public void pwdReset(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "Email sent.");
                        }
                    }
                });
    }
}