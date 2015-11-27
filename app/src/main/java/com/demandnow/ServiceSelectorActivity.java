package com.demandnow;

import android.os.Bundle;

/**
 * Created by Nirav on 27/11/2015.
 */
public class ServiceSelectorActivity extends GDNBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selector);
        renderToolbarActionbar();
        renderNavigationDrawer();
    }
}
