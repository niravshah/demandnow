package com.demandnow.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demandnow.R;
import com.demandnow.GDNSharedPrefrences;
import com.demandnow.GDNVolleySingleton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Nirav on 20/11/2015.
 */
public class DashboardTabFragment extends Fragment {

    public static final String TAB_NAME = "Dashboard";

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

        View v = inflater.inflate(R.layout.fragment_dashboard_layout, container, false);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(false);
        GDNSharedPrefrences.setMap(map);
        // Updates the location and zoom of the MapView
        if (GDNSharedPrefrences.getLastLocation() != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(GDNSharedPrefrences.getLastLocation().getLatitude(), GDNSharedPrefrences.getLastLocation().getLongitude()), 10);
            map.animateCamera(cameraUpdate);
            String url = "http://morph-stadium.codio.io:3000/nearby/"
                    + GDNSharedPrefrences.getLastLocation().getLatitude()
                    + "/"
                    + GDNSharedPrefrences.getLastLocation().getLongitude();
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

                                    map.addMarker(new MarkerOptions()
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

            GDNVolleySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
        }

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
