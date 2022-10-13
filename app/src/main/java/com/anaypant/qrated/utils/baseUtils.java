package com.anaypant.qrated.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.anaypant.qrated.Frames.ModelUser;
import com.anaypant.qrated.Interfaces.FirebaseUserCallback;
import com.anaypant.qrated.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class baseUtils {
    public static String getFancyTitle(){
        String[] headers = new String[]{"Howdy","What's poppin", "Hey","Look out","Heads up","Lookin' good"};
        String u = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        if (u == null || u.length() >= 10) {
            return headers[getRandomNumber(0, headers.length)] + "!";
        }
        return headers[getRandomNumber(0, headers.length)] + ", " + u + "!";
    }
    public static int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void getAddableUsers(String keyword, FirebaseUserCallback firebaseUserCallback){
        List<ModelUser> arr = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            System.out.println("Fatal Error: User Not Found in AddUser");
        }
        else{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ModelUser mU = ds.getValue(ModelUser.class);
                            if(!keyword.equals("")){
                                assert mU != null;
                                if (!mU.getUid().equals(user.getUid()) && mU.getName().contains(keyword)) {
                                    // make sure mU uid is not already in pending w user key

                                }
                            }
                        }
                        //firebaseUserCallback.onUserCallback(arr);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
    }

    public static boolean passedNullChecks(String email, String user, String pwd, Context context){
        // Makes sure all entries are valid.
        if (email.equals("") || pwd.equals("") || user.equals("")) {
            Toast.makeText(context, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(user.contains("%")){
            Toast.makeText(context, "Username cannot contain %.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(email.contains("@") && email.contains("."))) {
            Toast.makeText(context, "Email is Invalid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd.length() < 6){
            Toast.makeText(context, "Please enter min. 6 length password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

