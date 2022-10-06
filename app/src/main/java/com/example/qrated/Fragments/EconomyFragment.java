package com.example.qrated.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrated.Adapters.NewsAdapter;
import com.example.qrated.Frames.ModelNews;
import com.example.qrated.Interfaces.ButtonCallback;
import com.example.qrated.Interfaces.FirebaseNewsCallback;
import com.example.qrated.R;
import com.example.qrated.utils.firebaseUtils;

import java.util.ArrayList;

public class EconomyFragment extends Fragment {

    private static final String ARG_PARAM1 = "tabName";
    private RecyclerView recyclerView;
    NewsAdapter adapter;
    ArrayList<ModelNews> news = new ArrayList<>();
    private String tabName;


    public EconomyFragment() {this.tabName = "EconomyNews";}
    public static EconomyFragment newInstance(String param1) {
        EconomyFragment fragment = new EconomyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trending, container, false);

        recyclerView = v.findViewById(R.id.recycleviewoftrending);
        firebaseUtils.findNewsFromDB(tabName, new FirebaseNewsCallback() {
            @Override
            public void onDBCallback(ArrayList<ModelNews> val) {
                news = val;
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new NewsAdapter(getContext(), news, tabName, new ButtonCallback() {
                    @Override
                    public void onCallback(int pos) {
                        adapter.notifyItemChanged(pos);
                        recyclerView.smoothScrollToPosition(pos);

                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        return v;
    }


}