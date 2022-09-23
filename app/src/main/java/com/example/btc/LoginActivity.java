package com.example.btc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    DBHelper dbHelper;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    boolean ret;
    Map<String, Object> dataReceiver;
    boolean flag;

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
        dataReceiver = null;
        flag = false;

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            },0);
        }







        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String user = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                if (email.equals("") || password.equals("") || user.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(email.contains("@") && email.contains("."))) {
                    Toast.makeText(LoginActivity.this, "Email is Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.length() < 6){
                    Toast.makeText(LoginActivity.this, "Please enter min. 6 length password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                flag = false;
                auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(task.isSuccessful()){
                            if(Objects.requireNonNull(task.getResult().getSignInMethods()).size() == 0){
                                auth.createUserWithEmailAndPassword(email, pwd).
                                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(LoginActivity.this, "Please check your email for verification (spam category)", Toast.LENGTH_LONG).show();
                                            promptEmail();

                                            FirebaseUser u = auth.getCurrentUser();
                                            HashMap<Object, String> h = new HashMap<>();
                                            h.put("email",email);
                                            h.put("name",user);
                                            h.put("uid",u.getUid());
                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
                                            db.child(u.getUid()).setValue(h);

                                            FirebaseUser z = auth.getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(user)
                                                    .build();

                                            z.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(LoginActivity.this, "Username updated.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }

                                    }

                                });

                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Email is already in use.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });









            }

        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(email.contains("@") && email.contains("."))) {
                    Toast.makeText(LoginActivity.this, "Email is Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isEmailValid(email)){
                    if(!emailExists(email)){
                        Toast.makeText(LoginActivity.this, "Email not found in database.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        pwdReset(email);
                        Toast.makeText(LoginActivity.this, "Please check email to reset password.", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(LoginActivity.this, "Please enter valid email.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
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

                auth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if(checkIfEmailVerified()){
                                        FirebaseUser u = auth.getCurrentUser();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                        ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot ds: snapshot.getChildren()){
                                                    ModelClassAddUsers mU = ds.getValue(ModelClassAddUsers.class);
                                                    if(mU.getName().equals(user) && mU.getEmail().equals(email)){
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                        },0);
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    // Sign in success, update UI with the signed-in user's information
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });

    }
    public boolean isEmailValid(String email){
        return !(!email.contains("@") || !email.contains("."));
    }
    public boolean emailExists(String email){
        ret = false;


        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
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
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(LoginActivity.this, "Email Sent.", Toast.LENGTH_SHORT).show();
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
    private boolean checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Toast.makeText(this, "Internal Error: Please contact support :(", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            return true;
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            return false;
            //restart this activity
        }
    }
    private Map<String,Object> retrieveUser(String email){
        dataReceiver = null;
        DocumentReference docRef = db.collection("id").document("bin");
        docRef.get(Source.DEFAULT).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        System.out.println("\n\n\n\n\n\n");
                        System.out.println("Doc Data:  "+document.getData());
                        System.out.println("\n\n\n\n\n\n");
                        dataReceiver = document.getData();


                    } else {
                        //Log.d(TAG, "No such document");
                        System.out.println("\n\n\n\n\n\n");
                        System.out.println("No such document");
                        System.out.println("\n\n\n\n\n\n");
                        dataReceiver = new HashMap<>();

                    }
                }
                else {
                    //Log.d(TAG, "get failed with ", task.getException());
                    System.out.println("\n\n\n\n\n\n");
                    System.out.println("Document Retrieve Error");
                    System.out.println("\n\n\n\n\n\n");
                    dataReceiver = new HashMap<>();
                }
            }
        });
        return dataReceiver;

    }

}