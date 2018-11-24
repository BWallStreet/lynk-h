package com.bitanga.android.lynkactivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PostListFragment();
        }
        else if (position == 1) {
            return new GoogleMapFragment();
//            return new MapsActivity();
        }
        else if (position == 2) {
            return new MostLynkedPostListFragment();
        }

        else if (position == 3){
            return new FriendListFragment();
        }
        else {
            return new SettingsFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
