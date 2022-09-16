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
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Console;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem trending, economy, climate, social;
    PagerAdapter pagerAdapter;
    Toolbar toolbar;
    String apiKey = "2a2429ecaaa4496680cf6d23b9e8dc0a";
    String[] econKeys = new String[]{"+Economy", "+bitcoin", "+crypto", "+wall street"};
    String[] socKeys = new String[]{"+sexism","+LGBTQ","+abortion","+Abortion","+racism"};
    String lastUpdated;
    public  ArrayList<ModelClass> trendingNewsList = new ArrayList<>();
    public ArrayList<ModelClass> economyNewsList = new ArrayList<>();
    public ArrayList<ModelClass> environmentNewsList = new ArrayList<>();
    public ArrayList<ModelClass> socialNewsList = new ArrayList<>();
    private final int updateTime = 2; // how many hours we update

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

        ViewPager viewPager = findViewById(R.id.fragment_container);
        tabLayout = findViewById(R.id.NewsTabLayout);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),4);
        viewPager.setAdapter(pagerAdapter);
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


    public Boolean checkForRequest(){
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = df.format(currentTime);
        String hours = time.substring(11, 12);
        System.out.println("\n\n\n\n\n\n\n\n" + time + "\n\n\n\n\n\n\n\n\n");
        if(lastUpdated == null){
            lastUpdated = hours;
            return true;
        }
        else{
            int lastUpdatedInt = Integer.parseInt(lastUpdated);
            int t = Integer.parseInt(hours);
            if(lastUpdatedInt == 12 - updateTime + 1){
                if(t >= updateTime - 1){
                    lastUpdated  = String.valueOf(t);
                    return true;
                }
            }
            else if(lastUpdatedInt == 12){
                if(t >= updateTime){
                    lastUpdated  = String.valueOf(t);
                    return true;
                }
            }
            else if(t - 2 >= lastUpdatedInt){
                lastUpdated  = String.valueOf(t);
                return true;

            }
        }
        return false;


    }

}