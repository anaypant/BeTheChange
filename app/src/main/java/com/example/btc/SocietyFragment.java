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

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocietyFragment extends Fragment {
    String api="2a2429ecaaa4496680cf6d23b9e8dc0a";
    ArrayList<ModelClass> modelClassArrayList;
    NewsViewAdapter adapter;
    String country = "us";
    String category = "society";
    private RecyclerView recyclerViewofSociety;
    private final String[] keywords = new String[]{"+sexism","+LGBTQ","+abortion","+Abortion","+racism"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.society_layout, null);
        recyclerViewofSociety = v.findViewById(R.id.recycleviewofsociety);
        modelClassArrayList = new ArrayList<>();
        recyclerViewofSociety.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsViewAdapter(getContext(), modelClassArrayList);
        recyclerViewofSociety.setAdapter(adapter);
        findNews();
        return v;
    }

    private void findNews() {
        for(String key:keywords){
            apiUtils.getApiInterface().getKeywordNews(100,key, api).enqueue(new Callback<TrendingNews>() {
                @Override
                public void onResponse(Call<TrendingNews> call, Response<TrendingNews> response) {
                    if(response.isSuccessful()){
                        modelClassArrayList.addAll(response.body().getArticles());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<TrendingNews> call, Throwable t) {

                }
            });
        }

    }
}
