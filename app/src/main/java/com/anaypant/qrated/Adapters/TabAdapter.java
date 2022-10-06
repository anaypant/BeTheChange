package com.anaypant.qrated.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {
    private final int tabCount;
    public TabAdapter(@NonNull FragmentManager fm, int behavior){
        super(fm, new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {
            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public State getCurrentState() {
                return null;
            }
        });
        this.tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new com.anaypant.qrated.Fragments.TrendingFragment();
            case 1:
                return new com.anaypant.qrated.Fragments.EconomyFragment();
            case 2:
                return new com.anaypant.qrated.Fragments.EnvironmentFragment();
            case 3:
                return new com.anaypant.qrated.Fragments.SocietyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return this.tabCount;
    }
}
