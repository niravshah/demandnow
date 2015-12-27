package com.demandnow.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.demandnow.fragments.AllJobsTabFragment;
import com.demandnow.fragments.InProgressJobsTabFragment;

/**
 * Created by Nirav on 15/12/2015.
 */
public class JobTabsPagerAdapter extends FragmentStatePagerAdapter {

    public JobTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return InProgressJobsTabFragment.newInstance(position);
            case 1:
                return AllJobsTabFragment.newInstance(position);
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
                return InProgressJobsTabFragment.TAB_NAME;
            case 1:
                return AllJobsTabFragment.TAB_NAME;
            default:
                return null;
        }
    }
}
