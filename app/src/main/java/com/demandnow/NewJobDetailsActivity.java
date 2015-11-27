package com.demandnow;

import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewJobDetailsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_details);
        renderToolbarActionbar();
        renderNavigationDrawer();
    }

    private void renderToolbarActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void renderNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                Toast.makeText(NewJobDetailsActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        View drawerHeader = LayoutInflater.from(this).inflate(R.layout.drawer_header, navigationView);
        final TextView userName = (TextView) drawerHeader.findViewById(R.id.user_name_tv);
        userName.setText(SharedPrefrences.getAcctName());
        final CircleImageView imageView = (CircleImageView) drawerHeader.findViewById(R.id.profile_image);

        ImageRequest request = new ImageRequest(SharedPrefrences.getPhotUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.profile);
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
