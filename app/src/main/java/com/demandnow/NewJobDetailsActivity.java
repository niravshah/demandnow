package com.demandnow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.demandnow.services.Constants;
import com.demandnow.services.FetchAddressIntentService;
import com.demandnow.services.FetchLocationIntentService;
import com.demandnow.services.SubmitNewJobService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewJobDetailsActivity extends GDNBaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private AddressResultReceiver mResultReceiver;
    private LocationResultReceiver mLocationReciever;
    private EditText mHouseNumber;
    private EditText mStreetName;
    private EditText mPostcode;
    private GoogleMap map;
    private ProgressDialog mProgressDialog;
    private Double deliveryLatitude;
    private Double deliveryLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_details);
        renderChildActivityToolbar();
        renderNavigationDrawer();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.jobdetails_map);
        mapFragment.getMapAsync(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        mLocationReciever = new LocationResultReceiver(new Handler());
        mHouseNumber = (EditText) findViewById(R.id.houseNumber);
        mStreetName = (EditText) findViewById(R.id.streetName);
        mPostcode = (EditText) findViewById(R.id.postCode);
        findViewById(R.id.address_search_btn).setOnClickListener(this);
        findViewById(R.id.request_service).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (GDNSharedPrefrences.getLastLocation() != null) {
            map = googleMap;
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            Location lastLocation = GDNSharedPrefrences.getLastLocation();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 15);
            googleMap.animateCamera(cameraUpdate);
            startIntentService();
        }
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, GDNSharedPrefrences.getLastLocation());
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_search_btn:
                convertAddressToLocation();
                break;
            case R.id.request_service:
                requestService();
                break;
        }
    }

    private void requestService() {
        String serviceId = GDNSharedPrefrences.getServiceId();
        Double pickupLong = GDNSharedPrefrences.getLastLocation().getLongitude();
        Double pickupLat = GDNSharedPrefrences.getLastLocation().getLatitude();
        Intent intent = new Intent(this, SubmitNewJobService.class);
        intent.putExtra(Constants.SubmitNewJobService.DELIVERY_LATITUDE,deliveryLatitude.toString());
        intent.putExtra(Constants.SubmitNewJobService.DELIVERY_LONGITUDE,deliveryLongitude.toString());
        intent.putExtra(Constants.SubmitNewJobService.PICKUP_LATITUDE,pickupLat.toString());
        intent.putExtra(Constants.SubmitNewJobService.PICKUP_LONGITUDE,pickupLong.toString());
        intent.putExtra(Constants.SubmitNewJobService.SERVICE,serviceId);
        startService(intent);
        Toast.makeText(this, "Job Submitted", Toast.LENGTH_LONG).show();
        NavUtils.navigateUpFromSameTask(this);
    }

    private void convertAddressToLocation() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        showProgress();
        String address = mHouseNumber.getText().toString() + " "
                + mStreetName.getText().toString() + " "
                +  mPostcode.getText().toString();
        Intent intent = new Intent(this, FetchLocationIntentService.class);
        intent.putExtra(Constants.RECEIVER, mLocationReciever);
        intent.putExtra(Constants.ADDRESS_DATA_EXTRA, address);
        startService(intent);
    }

    private void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Getting Address...");
        }

        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(NewJobDetailsActivity.this,  resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_LONG).show();
            }

        }
    }


    class LocationResultReceiver extends ResultReceiver {

        public LocationResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                hideProgress();
                findViewById(R.id.request_service).setEnabled(true);
                Toast.makeText(NewJobDetailsActivity.this,  resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_LONG).show();
                Double lat = deliveryLatitude = resultData.getDouble(Constants.LATITUDE);
                Double lon = deliveryLongitude = resultData.getDouble(Constants.LONGITUDE);
                LatLng latlng = new LatLng(lat,lon);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 15);
                map.addMarker(new MarkerOptions().position(latlng));
                map.animateCamera(cameraUpdate);
            }

        }
    }
}
