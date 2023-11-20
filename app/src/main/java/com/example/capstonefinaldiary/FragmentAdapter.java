package com.example.capstonefinaldiary;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return WeekFragment.newInstance();
            case 1:
                return MonthFragment.newInstance();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 2;
    }
}