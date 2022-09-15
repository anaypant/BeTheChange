package com.example.btc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem trending, economy, climate, social;
    PagerAdapter pagerAdapter;
    Toolbar toolbar;
    String apiKey = "2a2429ecaaa4496680cf6d23b9e8dc0a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
                    pagerAdapter.notifyDataSetChanged();
                    //System.out.println("yes");
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
}