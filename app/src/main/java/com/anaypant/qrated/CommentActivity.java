package com.anaypant.qrated;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.anaypant.qrated.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaypant.qrated.Adapters.CommentAdapter;
import com.anaypant.qrated.Interfaces.FirebaseCommentCallback;
import com.anaypant.qrated.Interfaces.FirebaseStringCallback;
import com.anaypant.qrated.utils.firebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        setContentView(R.layout.activity_comment);

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
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("comments").child(tabName).child(String.valueOf(position));
                    HashMap<String, Object> h = new HashMap<>();
                    assert FirebaseAuth.getInstance().getCurrentUser() != null;
                    h.put("user",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    h.put("comment",commentField.getText().toString());
                    firebaseUtils.getLastKeyFromComments(position, tabName, new FirebaseStringCallback() {
                        @Override
                        public void onStringCallback(String s) {
                            String newKey = String.valueOf(Integer.parseInt(s) + 1);
                            ref.child(newKey).setValue(h);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position));
                            ref.child("commentCt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    String s = Objects.requireNonNull(task.getResult().getValue(String.class));
                                    ref.child("commentCt").setValue(String.valueOf(Integer.parseInt(s) + 1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            firebaseUtils.getCommentsFromDB(position, tabName, new FirebaseCommentCallback() {
                                                @Override
                                                public void onCommentCallback(List<Map> comments) {
                                                    commentsList = comments;
                                                    commentAdapter.setModelClasses(commentsList);
                                                    commentAdapter.notifyDataSetChanged();
                                                    commentField.setText("");

                                                }
                                            });
                                        }
                                    });
                                }
                            });

                        }
                    });
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
                        intent.putExtra("tab",(tabName));
                        intent.putExtra("pos", position);
                        startActivity(intent);
                        finish();
                    }

                },0);
            }
        });
        commentField.setMaxHeight(60);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = Integer.parseInt(extras.getString("pos"));
            tabName = extras.getString("tab");
            //The key argument here must match that used in the other activity
        }
        else{
            position = -1;
            tabName = "null";
            System.out.println("BIG BOY ERROR");
        }
        firebaseUtils.getCommentsFromDB(position, tabName, new FirebaseCommentCallback() {
            @Override
            public void onCommentCallback(List<Map> comments) {
                commentsList = comments;
                commentAdapter.setModelClasses(commentsList);

                commentAdapter.notifyDataSetChanged();

            }
        });
    }
}