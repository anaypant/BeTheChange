package com.example.btc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnvironmentFragment extends Fragment {
    String api="2a2429ecaaa4496680cf6d23b9e8dc0a";
    ArrayList<ModelClass> modelClassArrayList;
    NewsViewAdapter adapter;
    String country = "us";
    String category = "science";
    private RecyclerView recyclerViewofEnvironment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.environment_layout, null);
        recyclerViewofEnvironment = v.findViewById(R.id.recycleviewofenvironment);
        modelClassArrayList = new ArrayList<>();
        recyclerViewofEnvironment.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsViewAdapter(getContext(), modelClassArrayList, "EnvironmentNews", new VoteInterface() {
            @Override
            public void upVoteOnClick(View w, int position) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("votes").child("EnvironmentNews").child(String.valueOf(position));
                HashMap<Object, String> h = new HashMap<>();
                h.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), "1");
                ref.setValue(h);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void downVoteOnClick(View w, int position) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("votes").child("EnvironmentNews").child(String.valueOf(position));
                HashMap<Object, String> h = new HashMap<>();
                h.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), "-1");
                ref.setValue(h);
                adapter.notifyDataSetChanged();

            }
        });
        recyclerViewofEnvironment.setAdapter(adapter);

        findNews();
        return v;
    }

    private void findNews() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("articles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.child("EnvironmentNews").getChildren()){
                    modelClassArrayList.add(ds.getValue(ModelClass.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
