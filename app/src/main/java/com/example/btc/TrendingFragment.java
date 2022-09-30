package com.example.btc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TrendingFragment extends Fragment {
    String api="2a2429ecaaa4496680cf6d23b9e8dc0a";
    ArrayList<ModelClass> modelClassArrayList, oldModelClassArrayList;
    NewsViewAdapter adapter;
    String country = "us";
    private RecyclerView recyclerViewoftrending;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.trending_layout, null);

        recyclerViewoftrending = v.findViewById(R.id.recycleviewoftrending);
        modelClassArrayList = new ArrayList<>();
        oldModelClassArrayList = new ArrayList<>();

        recyclerViewoftrending.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsViewAdapter(getContext(), modelClassArrayList, "TrendingNews", new AdapterSelecterListener() {
            @Override
            public void onUpvoteClick(ModelClass c, int position, String tabName) {
                VotingUtils.updateUpVotes(position, tabName);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onDownVoteClick(ModelClass c, int position, String tabName) {
                VotingUtils.updateDownVotes(position, tabName);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCommentClick(ModelClass c, int position, String tabName) {
                VotingUtils.goToComments(getContext(), c, position, tabName);
            }
        });
        adapter.setTabName("TrendingNews");
        recyclerViewoftrending.setAdapter(adapter);
        findNews();

        return v;
    }

    private void findNews() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("articles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.child("TrendingNews").getChildren()){
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
