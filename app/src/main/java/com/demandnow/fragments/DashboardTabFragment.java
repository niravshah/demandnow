package com.demandnow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demandnow.NewJobDetailsActivity;
import com.demandnow.R;
import com.demandnow.SharedPrefrences;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nirav on 20/11/2015.
 */
public class DashboardTabFragment extends Fragment {

    public static final String TAB_NAME = "New Request";

    MapView mapView;
    private GoogleMap map;

    public DashboardTabFragment() {
    }

    public static DashboardTabFragment newInstance(int tabPosition) {
        DashboardTabFragment frag = new DashboardTabFragment();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.new_job_req_layout, container, false);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(false);
        SharedPrefrences.setMap(map);
        // Updates the location and zoom of the MapView
        if(SharedPrefrences.getLastLocation() != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(SharedPrefrences.getLastLocation().getLatitude(), SharedPrefrences.getLastLocation().getLongitude()), 10);
            map.animateCamera(cameraUpdate);
        }

        final FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DashboardTabFragment", "onclick fab");
                startActivity(new Intent(getContext(), NewJobDetailsActivity.class));
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
