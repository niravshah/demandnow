package com.demandnow;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demandnow.adapters.MainTabsPagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends GDNBaseActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Toast.makeText(MainActivity.this, "Logged In: " + SharedPrefrences.getAcctName(), Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_main);
        renderToolbarActionbar();
        renderNavigationDrawer();
        buildGoogleApiClient();
        MainTabsPagerAdapter adapter = new MainTabsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_service_selector:
                startActivity(new Intent(this,ServiceSelectorActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            SharedPrefrences.setLastLocation(mLastLocation);
            Toast.makeText(MainActivity.this, "Location Updated", Toast.LENGTH_LONG).show();
            if (SharedPrefrences.getMap() != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15);
                SharedPrefrences.getMap().animateCamera(cameraUpdate);
                String url = "http://morph-stadium.codio.io:3000/nearby/"
                        + SharedPrefrences.getLastLocation().getLatitude()
                        + "/"
                        + SharedPrefrences.getLastLocation().getLongitude();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("LOGIN", response.toString());
                                Iterator<String> keys = response.keys();
                                while (keys.hasNext()) {
                                    try {
                                        String ninja = keys.next();
                                        JSONObject o = (JSONObject) response.get(ninja);

                                        SharedPrefrences.getMap().addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.parseDouble((String) o.get("latitude")),
                                                        Double.parseDouble((String) o.get("longitude"))))
                                                .title(ninja)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_takeaway_icon)));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Log.e("VolleyErorr", error.getLocalizedMessage() + error.getMessage());
                            }
                        });

                VolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}


