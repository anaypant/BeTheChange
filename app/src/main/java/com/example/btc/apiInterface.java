package com.example.btc;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface apiInterface {
    String BASE_URL = "https://newsapi.org/v2/";

    @GET("top-headlines")
    Call<TrendingNews> getNews(
            @Query("country") String country,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey
    );
    @GET("everything")
    Call<TrendingNews> getKeywordNews(
            @Query("pageSize")  int pageSize,
            @Query("q") String keyword,
            @Query("apiKey") String apiKey
    );
    @GET("top-headlines")
    Call<TrendingNews> getCategoryNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey
    );



}
