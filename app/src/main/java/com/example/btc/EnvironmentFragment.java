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
        adapter = new NewsViewAdapter(getContext(), modelClassArrayList);
        recyclerViewofEnvironment.setAdapter(adapter);

        findNews();
        return v;
    }

    private void findNews() {
        apiUtils.getApiInterface().getCategoryNews(country, category,100,api).enqueue(new Callback<TrendingNews>() {
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
