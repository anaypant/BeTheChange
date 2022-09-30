package com.example.btc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Map> commentsList;
    ImageButton backArrow, submitComment;
    EditText commentField;
    String tabName;
    CommentAdapter commentAdapter;
    int position;
    public CommentActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_comment);

        recyclerView = findViewById(R.id.CommentsRecyclerFragment);
        commentField = findViewById(R.id.addCommentTextField);
        backArrow = findViewById(R.id.commentsBackArrow);
        commentsList = new ArrayList<Map>();
        submitComment = findViewById(R.id.addCommentButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        commentAdapter = new CommentAdapter(CommentActivity.this, commentsList, tabName);
        recyclerView.setAdapter(commentAdapter);

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("yes");
                // check if field is empty
                if(commentField.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this, "Please enter a valid comment.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (commentField.getText().toString().length() > 144){
                    Toast.makeText(CommentActivity.this, "Please enter a shorter comment (144 char limit).", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    // add to references
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
                    HashMap<String, Object> h = new HashMap<>();
                    assert FirebaseAuth.getInstance().getCurrentUser() != null;
                    h.put("user",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    h.put("comment",commentField.getText().toString());
                    ref.child("comments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(h);
                }
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(CommentActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                },0);
            }
        });
        commentField.setMaxHeight(60);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             position = Integer.parseInt(extras.getString("position"));
             tabName = extras.getString("tabName");
            //The key argument here must match that used in the other activity
        }
        else{
            position = -1;
            tabName = "null";
            System.out.println("BIG BOY ERROR");
        }
        getCommentsFromDB();

    }

    private void getCommentsFromDB(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("articles").child(tabName).child(String.valueOf(position)).child("comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map mU = new HashMap();
                    mU.put("user", ((Map<String, Object>) Objects.requireNonNull(ds.getValue())).get("user"));
                    mU.put("comment", ((Map<String, Object>) ds.getValue()).get("comment"));

                    commentsList.add(mU);
                }
                System.out.println(commentsList);
                commentAdapter.setModelClasses(commentsList);
                commentAdapter.notifyItemChanged(position);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}