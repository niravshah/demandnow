package com.demandnow.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.demandnow.fragments.JobQueueTabFragment;

/**
 * Created by Nirav on 15/12/2015.
 */
public class JobSummaryTabsPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager mgr;
    public JobSummaryTabsPagerAdapter(FragmentManager fm) {
        super(fm);
        mgr = fm;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return JobQueueTabFragment.newInstance(position);
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
                return JobQueueTabFragment.TAB_NAME;
            case 1:
                return JobQueueTabFragment.TAB_NAME;
            default:
                return null;
        }
    }
}
