package com.example.btc;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VotingUtils {
    public static void updateUpVotes(int position, String tabName){
        //System.out.println(tabName);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("articles").child(tabName).child(String.valueOf(position)).child("votes");
        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countUp = 0;

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getValue(String.class).equals("1")){
                        countUp+=1;
                    }
                }
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
                ref2.child("upvotect").setValue(String.valueOf(countUp));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countUp = 0;

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getValue(String.class).equals("-1")){
                        countUp+=1;
                    }
                }
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
                ref2.child("downvotect").setValue(String.valueOf(countUp));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public static void updateDownVotes(int position, String tabName){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("articles").child(tabName).child(String.valueOf(position)).child("votes");
        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("-1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countUp = 0;

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getValue(String.class).equals("-1")){
                        countUp+=1;
                    }
                }
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
                ref2.child("downvotect").setValue(String.valueOf(countUp));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countUp = 0;

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getValue(String.class).equals("1")){
                        countUp+=1;
                    }
                }
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
                ref2.child("upvotect").setValue(String.valueOf(countUp));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void goToComments(Context context, ModelClass c, int position, String tabName){
        System.out.println("Comment Button Hit at " + tabName + "  at " + String.valueOf(position));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("position",String.valueOf(position));
                intent.putExtra("tabName",tabName);
                context.startActivity(intent);
            }

        },0);
    }
}
