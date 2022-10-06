package com.example.qrated.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qrated.Frames.ModelNews;
import com.example.qrated.Frames.ModelUser;
import com.example.qrated.Interfaces.FirebaseBoolCallback;
import com.example.qrated.Interfaces.FirebaseCommentCallback;
import com.example.qrated.Interfaces.FirebaseNewsCallback;
import com.example.qrated.Interfaces.FirebaseStringCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class firebaseUtils {
    public static void createUser(String user, String email, String pwd, Context context){
        FirebaseAuth auth = FirebaseAuth.getInstance();
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
                                            Toast.makeText(context, "Please check your email for verification (spam category)", Toast.LENGTH_LONG).show();
                                            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                                            u.sendEmailVerification()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(context, "Email Sent.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

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
                                                        }
                                                    });

                                        }

                                    }

                                });

                    }
                    else{
                        Toast.makeText(context, "Email is already in use.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static void checkIfEmailExists(String email, FirebaseBoolCallback firebaseBoolCallback){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                firebaseBoolCallback.onBoolCallback(task.getResult().getSignInMethods().size() != 0);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static void resetUserPassword(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {}
                    }
                });
    }
    private static boolean checkIfEmailVerified(){
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if(u == null){
            System.out.println("User not found");
            return false;
        }
        return u.isEmailVerified();
    }
    public static void logInUser(String email, String pwd, String user, Context context, FirebaseBoolCallback firebaseBoolCallback){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(checkIfEmailVerified()){
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            ModelUser mU = ds.getValue(ModelUser.class);
                                            assert mU != null;
                                            if (mU.getName().equals(user) && mU.getEmail().equals(email)) {
                                                firebaseBoolCallback.onBoolCallback(true);
                                            }

                                        }
                                        firebaseBoolCallback.onBoolCallback(false);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                            else{
                                Toast.makeText(context, "Please verify email first.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(context, "Authentication Error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public static void checkIfNewsNeedsUpdate(FirebaseBoolCallback firebaseBoolCallback, boolean DEBUG){
        String date = String.valueOf((int) Long.parseLong(String.valueOf(new Date().getTime()))/3.6e6);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("apiUpdate");
        int updateTime = 12;
        boolean[] updated = new boolean[1];

        ref.child("lastUpdated").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String lastUpdated = task.getResult().getValue(String.class);
                    assert lastUpdated != null;
                if(Math.abs(Double.parseDouble(date) - Double.parseDouble(lastUpdated)) >= updateTime || DEBUG){
                    ref.child("lastUpdated").setValue(date);
                    updated[0]=true;
                }
                firebaseBoolCallback.onBoolCallback(updated[0]);
            }

        });
    }
    public static void findNewsFromDB(String tabName, FirebaseNewsCallback firebaseNewsCallback){
        ArrayList<ModelNews> arr = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("articles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.child(tabName).getChildren()){
                    arr.add(ds.getValue(ModelNews.class));
                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    arr.sort(Comparator.comparingInt((ModelNews t) -> Integer.parseInt(t.getUpVoteCt())));
//                }
//                Collections.reverse(arr);

                firebaseNewsCallback.onDBCallback(arr);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void addPendingRequestForUid(String targetUid){
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if(u == null){
            System.out.println("User error: Firebase adding friend request");
            return;
        }
        String currentUid = u.getUid();
        Map<Object, String> h =new HashMap<>();
        h.put("target", targetUid);
        h.put("current",currentUid);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("friends").child("pending");
        getLastKeyFromPending(new FirebaseStringCallback() {
            @Override
            public void onStringCallback(String s) {
                String last = String.valueOf(Integer.parseInt(s) + 1);
                ref.child(last).setValue(h);
            }
        });
    }
    public static void getLastKeyFromComments(int position, String tabName, FirebaseStringCallback stringCallback){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("comments").child(tabName).child(String.valueOf(position));
        Query query = rootRef.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String last = "0";
                for(DataSnapshot ds: snapshot.getChildren()){
                    last = ds.getKey();
                }
                stringCallback.onStringCallback(last);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private static void getLastKeyFromPending(FirebaseStringCallback stringCallback){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("friends").child("pending");
        Query query = rootRef.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String last = "";
                for(DataSnapshot ds: snapshot.getChildren()){
                    last = ds.getKey();
                }
                stringCallback.onStringCallback(last);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void checkUserStatusItem(int position, String tabName, FirebaseStringCallback stringCallback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position)).child("votes");
        // check if user has liked a certain post or not
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getKey().equals(currentUid) && ds.getValue(String.class).equals("1")){
                        stringCallback.onStringCallback("like");
                        return;

                    }
                    else if (ds.getKey().equals(currentUid) && ds.getValue(String.class).equals("-1")){
                        stringCallback.onStringCallback("dislike");
                        return;

                    }
                }
                stringCallback.onStringCallback("neutral");
                return;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void upVoteAPost(int position, String tabName, FirebaseBoolCallback boolCallback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if(u == null){
            System.out.println("User Error: upVoteAPost");
            return;
        }
        ref.child("votes").child(u.getUid()).setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateVoteCounts(position, tabName, new FirebaseBoolCallback() {
                    @Override
                    public void onBoolCallback(boolean value) {
                        boolCallback.onBoolCallback(true);

                    }
                });

            }
        }); // setting vote to 1
    }
    public static void downVoteAPost(int position, String tabName, FirebaseBoolCallback boolCallback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if(u == null){
            System.out.println("User Error: downVoteAPost");
            return;
        }
        ref.child("votes").child(u.getUid()).setValue("-1").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateVoteCounts(position, tabName, new FirebaseBoolCallback() {
                    @Override
                    public void onBoolCallback(boolean value) {
                        boolCallback.onBoolCallback(true);
                    }
                });

            }
        }); // setting vote to 1
    }
    public static void findNewsFromDBatPosition(int position, String tabName, FirebaseNewsCallback newsCallback){
        ArrayList<ModelNews> arr = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("articles").child(tabName).child(String.valueOf(position));
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                arr.add(task.getResult().getValue(ModelNews.class));
                newsCallback.onDBCallback(arr);
            }
        });
    }
    public static void updateVoteCounts(int position, String tabName, FirebaseBoolCallback boolCallback){
        // count number of yes votes, number of no votes, set them to different ones
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
        ref.child("votes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numUpVotes = 0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getValue(String.class).equals("1")){
                        numUpVotes += 1;
                    }
                }
                // set up votes to numUpVotes and downVotes to numDownVotes
                ref.child("upVoteCt").setValue(String.valueOf(numUpVotes)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
                        ref.child("votes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int numDownVotes = 0;

                                for(DataSnapshot ds: snapshot.getChildren()){
                                    if(ds.getValue(String.class).equals("-1")){
                                        numDownVotes += 1;
                                    }
                                }
                                ref.child("downVoteCt").setValue(String.valueOf(numDownVotes)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        boolCallback.onBoolCallback(true);
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getCommentsFromDB(int position, String tabName, FirebaseCommentCallback commentCallback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("comments").child(tabName).child(String.valueOf(position));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Map> commentsList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map mU = new HashMap();
                    mU.put("user", ((Map<String, Object>) Objects.requireNonNull(ds.getValue())).get("user"));
                    mU.put("comment", ((Map<String, Object>) ds.getValue()).get("comment"));

                    commentsList.add(mU);
                }
                commentCallback.onCommentCallback(commentsList);
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void resetComments(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("comments").removeValue();
    }


}
