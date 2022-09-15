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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EconomyFragment extends Fragment {
    String api="2a2429ecaaa4496680cf6d23b9e8dc0a";
    ArrayList<ModelClass> modelClassArrayList;
    NewsViewAdapter adapter;
    String country = "us";
    String[] keywords = new String[]{"+Economy", "+bitcoin", "+crypto", "+wall street"};
    private RecyclerView recyclerViewofEconomy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.econ_layout, null);
        recyclerViewofEconomy = v.findViewById(R.id.recycleviewofeconomy);
        modelClassArrayList = new ArrayList<>();
        recyclerViewofEconomy.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsViewAdapter(getContext(), modelClassArrayList);
        recyclerViewofEconomy.setAdapter(adapter);

        findNews();
        for (int z = 0; z < modelClassArrayList.size();z++){
            String content = modelClassArrayList.get(z).getDescription();
            String head = modelClassArrayList.get(z).getTitle();
            String urltoImg = modelClassArrayList.get(z).getUrlToImage();
            if(content == null || head == null || urltoImg==null){
                modelClassArrayList.remove(z);
                z--;
            }
        }
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
