package com.demandnow.fragments;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demandnow.MainActivity;
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
public class NewRequestTabFragment extends Fragment {

    public static final String TAB_NAME = "New Request";

    MapView mapView;
    private GoogleMap map;

    public NewRequestTabFragment() {
    }

    public static NewRequestTabFragment newInstance(int tabPosition) {
        NewRequestTabFragment frag = new NewRequestTabFragment();
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

        final TextInputLayout layout = (TextInputLayout)v.findViewById(R.id.text_input_layout_view);
        final LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.viewB);

        final EditText editText = (EditText) linearLayout.findViewById(R.id.postcode);

        final FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("NewRequestTabFragment", "onclick fab");
                TransitionDrawable transition = (TransitionDrawable) linearLayout.getBackground();
                transition.startTransition(1000);
                Animation slideUp = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_up);
                Animation slideDown = AnimationUtils.loadAnimation(v.getContext(), R.anim.scale_fab_out);
                fab.startAnimation(slideDown);
                fab.setVisibility(View.INVISIBLE);
                if(layout.getVisibility()==View.INVISIBLE){
                    layout.startAnimation(slideUp);
                    layout.setVisibility(View.VISIBLE);
                }
                editText.requestFocus();
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
