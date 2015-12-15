package com.demandnow.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.demandnow.GDNBaseActivity;
import com.demandnow.R;
import com.demandnow.adapters.JobSummaryTabsPagerAdapter;

/**
 * Created by Nirav on 15/12/2015.
 */
public class JobSummaryViewActivity extends GDNBaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_summary_view);
        renderNavigationDrawer();
        renderChildActivityToolbar();
        JobSummaryTabsPagerAdapter adapter = new JobSummaryTabsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
