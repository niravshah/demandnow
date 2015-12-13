package com.demandnow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.demandnow.GDNBaseActivity;
import com.demandnow.R;

/**
 * Created by Nirav on 13/12/2015.
 */
public class JobDetailViewActivity extends GDNBaseActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selector);
        renderNavigationDrawer();
        renderChildActivityToolbar();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }
}
