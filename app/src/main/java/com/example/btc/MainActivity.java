package com.example.btc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem trending, economy, climate, social;
    PagerAdapter pagerAdapter, addFriendsAdapter;
    ImageButton profileButton, addFriends;
    Button upVoteButton, downVoteButton;
    Toolbar toolbar;
    TextView neatTitleText;
    String apiKey = "2a2429ecaaa4496680cf6d23b9e8dc0a";

    //d9c1ce9082704e27bb1d4def64559eaa
    //24apant
    //2a2429ecaaa4496680cf6d23b9e8dc0a
    //anaypant212


    String[] econKeys = new String[]{"+Economy", "+bitcoin", "+crypto", "+wall street"};
    String[] socKeys = new String[]{"+sexism","+LGBTQ","+abortion","+Abortion","+racism","+affirmative action","+Antifa","Affordable Care Act","+covid","+filibuster","+gerrymandering","+voter fraud","+immigration"};
    String[] environmentKeys = new String[]{"+climate", "+climate change", "+health", "+pollution", "+ems", "+global warming", "+green", "+epa", "+sustainability", "+health"};

    List<String> lastUpdated = new ArrayList<>();
    public  ArrayList<ModelClass> trendingNewsList = new ArrayList<>();
    public ArrayList<ModelClass> economyNewsList = new ArrayList<>();
    public ArrayList<ModelClass> environmentNewsList = new ArrayList<>();
    public ArrayList<ModelClass> socialNewsList = new ArrayList<>();
    private final int updateTime = 12; // how many hours we update
    String[] neatTitleThingies = new String[]{"Howdy","What's poppin", "Hey","Look out","Heads up","Lookin' good"};
    private final int neatTitleTopLength = 10;
    private final boolean DEBUG_GET_NEWS = false;
    private final int numPages=32;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        trending = findViewById(R.id.TrendingTab);
        economy = findViewById(R.id.EconomyTab);
        climate = findViewById(R.id.EnvironmentTab);
        social = findViewById(R.id.SocietyTab);
        upVoteButton = findViewById(R.id.UpVote);
        downVoteButton = findViewById(R.id.DownVote);


        ViewPager viewPager = findViewById(R.id.fragment_container);
        ViewPager addFriendsPager = findViewById(R.id.AddUserContainer);
        tabLayout = findViewById(R.id.NewsTabLayout);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),4);
        addFriendsAdapter = new PagerAdapter(getSupportFragmentManager(), 1);

        addFriendsPager.setAdapter(addFriendsAdapter);
        viewPager.setAdapter(pagerAdapter);
        profileButton = findViewById(R.id.UserProfile);
        neatTitleText = findViewById(R.id.NeatUserTitle);
        addFriends = findViewById(R.id.AddFriends);


        // Update the recent articles every x hours here.
        String date = String.valueOf(new Date().getTime());


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("apiUpdate");
        date = String.valueOf((int) Long.parseLong(date)/3.6e6);
        String finalDate = date;

        ref.child("LastUpdated").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if((Math.abs(Double.parseDouble((snapshot.getValue(String.class))) - Double.parseDouble((finalDate))) >= (updateTime)) || DEBUG_GET_NEWS){
                    ref.child("LastUpdated").setValue(finalDate);

                    // add new archive news
                    System.out.println("\n");
                    System.out.println("Finding news");
                    System.out.println("\n");

                    findNews();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //add some neat title
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        assert u!= null;
        if(u.getDisplayName().length() >= neatTitleTopLength){
            neatTitleText.setText(neatTitleThingies[getRandomNumber(0,neatTitleThingies.length)] + "!");
        }
        else{
            neatTitleText.setText(neatTitleThingies[getRandomNumber(0,neatTitleThingies.length)] + ", " + u.getDisplayName().toString() + "!");

        }

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, AddUsers.class);
                        startActivity(intent);
                        finish();
                    }

                },0);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to settings page
                // we want to sign out
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, settings.class);
                        startActivity(intent);
                        finish();
                    }

                },0);

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3){ // home
                    //System.out.println("yes");

                    pagerAdapter.notifyDataSetChanged();
                }




            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }



    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void setNewComments(String Field,String i){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("comments");
        ref.child(Field).child(i).setValue("");
    }
    private void findNews() {
        //trending
        apiUtils.getApiInterface().getNews("us",numPages,apiKey).enqueue(new Callback<TrendingNews>() {
            @Override
            public void onResponse(Call<TrendingNews> call, Response<TrendingNews> response) {
                if(response.isSuccessful()){
                    pagerAdapter.notifyDataSetChanged();
                    //add to database
                    DatabaseReference archivesReference = FirebaseDatabase.getInstance().getReference("articles");
                    ArrayList<ModelClass> temporary = response.body().getArticles();
                    for(int x = 0; x < temporary.size();x++){
                        ModelClass z = temporary.get(x);
                        z.setDownVotes("0");
                        z.setUpVotes("0");
                        setNewComments("TrendingNews",String.valueOf(x));

                    }
                    archivesReference.child("TrendingNews").setValue(temporary);
                }
            }

            @Override
            public void onFailure(Call<TrendingNews> call, Throwable t) {

            }
        });

        //economy
        for(String key:econKeys){
            apiUtils.getApiInterface().getKeywordNews(numPages,key, apiKey).enqueue(new Callback<TrendingNews>() {
                @Override
                public void onResponse(Call<TrendingNews> call, Response<TrendingNews> response) {
                    if(response.isSuccessful()){
                        pagerAdapter.notifyDataSetChanged();
                        DatabaseReference archivesReference = FirebaseDatabase.getInstance().getReference("articles");
                        ArrayList<ModelClass> temporary = response.body().getArticles();
                        for(int x = 0; x < temporary.size();x++){
                            ModelClass z = temporary.get(x);
                            z.setDownVotes("0");
                            z.setUpVotes("0");
                            setNewComments("EconomyNews",String.valueOf(x));
                        }
                        archivesReference.child("EconomyNews").setValue(temporary);
                    }
                }

                @Override
                public void onFailure(Call<TrendingNews> call, Throwable t) {

                }
            });
        }

        //environment
        for(String key:environmentKeys){
            apiUtils.getApiInterface().getKeywordNews(numPages,key, apiKey).enqueue(new Callback<TrendingNews>() {
                @Override
                public void onResponse(Call<TrendingNews> call, Response<TrendingNews> response) {
                    if(response.isSuccessful()){
                        pagerAdapter.notifyDataSetChanged();
                        DatabaseReference archivesReference = FirebaseDatabase.getInstance().getReference("articles");
                        ArrayList<ModelClass> temporary = response.body().getArticles();
                        for(int x = 0; x < temporary.size();x++){
                            ModelClass z = temporary.get(x);
                            z.setDownVotes("0");
                            z.setUpVotes("0");
                            setNewComments("EnvironmentNews",String.valueOf(x));
                        }
                        archivesReference.child("EnvironmentNews").setValue(temporary);


                    }
                }

                @Override
                public void onFailure(Call<TrendingNews> call, Throwable t) {

                }
            });
        }




        //social
        for(String key:socKeys){
            apiUtils.getApiInterface().getKeywordNews(100,key, apiKey).enqueue(new Callback<TrendingNews>() {
                @Override
                public void onResponse(Call<TrendingNews> call, Response<TrendingNews> response) {
                    if(response.isSuccessful()){
                        pagerAdapter.notifyDataSetChanged();
                        DatabaseReference archivesReference = FirebaseDatabase.getInstance().getReference("articles");
                        ArrayList<ModelClass> temporary = response.body().getArticles();
                        for(int x = 0; x < temporary.size();x++){
                            ModelClass z = temporary.get(x);
                            z.setDownVotes("0");
                            z.setUpVotes("0");
                            setNewComments("SocietyNews",String.valueOf(x));
                        }
                        archivesReference.child("SocietyNews").setValue(temporary);

                    }
                }

                @Override
                public void onFailure(Call<TrendingNews> call, Throwable t) {

                }
            });
        }
    }


}