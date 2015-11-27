package com.demandnow;

import android.os.Bundle;

public class NewJobDetailsActivity extends GDNBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_details);
        renderToolbarActionbar();
        renderNavigationDrawer();
    }

}
