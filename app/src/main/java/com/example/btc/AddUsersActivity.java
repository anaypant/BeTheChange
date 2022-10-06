package com.example.btc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btc.Adapters.AddUserAdapter;
import com.example.btc.Frames.ModelUser;
import com.example.btc.Interfaces.AdapterListeners.AddUserListener;
import com.example.btc.Interfaces.FirebaseUserCallback;
import com.example.btc.utils.baseUtils;
import com.example.btc.utils.firebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class AddUsersActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    AddUserAdapter adapter;
    List<ModelUser> users;
    ImageButton backArrow;
    EditText searchField;
    AddUserListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        users = new ArrayList<>();
        this.listener = new AddUserListener() {
            @Override
            public void onAddFriendClick(ModelUser u, int position) {
                // add modelUser uid to reference with current user as key
                Toast.makeText(AddUsersActivity.this, "Requesting " + u.getName() + " to be your friend", Toast.LENGTH_SHORT).show();
                firebaseUtils.addPendingRequestForUid(u.getUid());
            }
        };

        recyclerView = findViewById(R.id.addUsersRecyclerFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddUsersActivity.this));
        adapter = new AddUserAdapter(AddUsersActivity.this, users, listener);
        recyclerView.setAdapter(adapter);

        backArrow = findViewById(R.id.backArrow);
        searchField = findViewById(R.id.UserSearchBar);
        searchField.setMaxHeight(30);

        // adding listener for typing in search field
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {//searchField.getText().toString().trim())
                baseUtils.getAddableUsers(searchField.getText().toString().trim(), new FirebaseUserCallback() {
                    @Override
                    public void onUserCallback(List<ModelUser> arr) {
                        users = arr;
                        adapter = new AddUserAdapter(AddUsersActivity.this, users, listener);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        });

        // adding back button listener
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AddUsersActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                },0);
            }
        });


    }
}