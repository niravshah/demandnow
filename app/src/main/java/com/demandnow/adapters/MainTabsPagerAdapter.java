package com.demandnow.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.demandnow.R;
import com.demandnow.fragments.JobQueueTabFragment;
import com.demandnow.fragments.NewRequestTabFragment;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Nirav on 20/11/2015.
 */
public class MainTabsPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager mgr;
    public MainTabsPagerAdapter(FragmentManager fm) {
        super(fm);
        mgr = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewRequestTabFragment tab = NewRequestTabFragment.newInstance(position);
                return tab;
            case 1:
                return JobQueueTabFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return NewRequestTabFragment.TAB_NAME;
            case 1:
                return JobQueueTabFragment.TAB_NAME;
            default:
                return null;
        }
    }
}
