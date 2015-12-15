package com.demandnow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends GDNBaseActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, View.OnClickListener {


    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private GoogleMap map;
    public static final String TAB_NAME = "Dashboard";
    private static final int REQUEST_INVITE = 9000;
    private static final String TAG = "DashboardTabFragment";
    private static final int RESULT_OK = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Toast.makeText(MainActivity.this, "Logged In: " + GDNSharedPrefrences.getAcctName(), Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_main);
        renderToolbarActionbar();
        renderNavigationDrawer();
        initializeBroadcastReciever();
        buildGoogleApiClient();
        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.app_invite).setOnClickListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.jobdetails_map);
        mapFragment.getMapAsync(this);

    }

    private void initializeBroadcastReciever() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(GDNSharedPrefrences.SENT_TOKEN_TO_SERVER, false);
                if(sentToken){
                    Toast.makeText(MainActivity.this, "GCM Token Sent", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "GCM Token Error", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GDNSharedPrefrences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            GDNSharedPrefrences.setLastLocation(mLastLocation);
            Toast.makeText(MainActivity.this, "Location Updated", Toast.LENGTH_LONG).show();
            if (GDNSharedPrefrences.getMap() != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15);
                GDNSharedPrefrences.getMap().animateCamera(cameraUpdate);
                JsonObjectRequest jsObjRequest = getJsonObjectRequest();
                GDNVolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
            }
        }
    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest() {
        String url = GDNApiHelper.BASE_URL
                + "/"+ GDNSharedPrefrences.getServiceId()
                + "/nearby/"
                + GDNSharedPrefrences.getLastLocation().getLatitude()
                + "/"
                + GDNSharedPrefrences.getLastLocation().getLongitude();
        return new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("LOGIN", response.toString());
                        Iterator<String> keys = response.keys();
                        while (keys.hasNext()) {
                            try {
                                String ninja = keys.next();
                                JSONObject o = (JSONObject) response.get(ninja);

                                GDNSharedPrefrences.getMap().addMarker(new MarkerOptions()
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
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(false);

        GDNSharedPrefrences.setMap(map);
        if (GDNSharedPrefrences.getLastLocation() != null) {
            Location lastLocation = GDNSharedPrefrences.getLastLocation();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 15);
            map.animateCamera(cameraUpdate);
        }
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent and log a message
                // The ids array contains the unique invitation ids for each invitation sent
                // (one for each contact select by the user). You can use these for analytics
                // as the ID will be consistent on the sending and receiving devices.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
            } else {
                // Sending failed or it was canceled, show failure message to the user
                //showMessage(getString(R.string.send_failed));
            }
        }
    }
    private void startNewJobDetailsActivity() {
        startActivity(new Intent(this, NewJobDetailsActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_invite:
                onInviteClicked();
                break;
            case R.id.fab:
                startNewJobDetailsActivity();
                break;
        }
    }
}


