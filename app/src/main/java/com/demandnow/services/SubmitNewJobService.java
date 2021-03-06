package com.demandnow.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demandnow.GDNApiHelper;
import com.demandnow.GDNSharedPrefrences;
import com.demandnow.GDNVolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nirav on 07/12/2015.
 */
public class SubmitNewJobService extends IntentService {

    private static final String TAG = "SubmitNewJobService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public SubmitNewJobService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String delLat = intent.getStringExtra(Constants.SubmitNewJobService.DELIVERY_LATITUDE);
        String delLon = intent.getStringExtra(Constants.SubmitNewJobService.DELIVERY_LONGITUDE);
        String pickupLat = intent.getStringExtra(Constants.SubmitNewJobService.PICKUP_LATITUDE);
        String pickupLon = intent.getStringExtra(Constants.SubmitNewJobService.PICKUP_LONGITUDE);
        String serviceId = intent.getStringExtra(Constants.SubmitNewJobService.SERVICE);
        String deliveryAddress = intent.getStringExtra(Constants.SubmitNewJobService.DELIVERY_ADDRESS);
        JSONObject data = new JSONObject();
        try {
            data.put("deliveryAddress", deliveryAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url =  serviceId + "/" + GDNSharedPrefrences.getAcctId() + "/" + pickupLat + "/" + pickupLon + "/" + delLat + "/" + delLon;
        JsonObjectRequest jsonObjectRequest = getJsonObjectRequest(url,data);
        GDNVolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest(String urlz, JSONObject data) {
        String url = GDNApiHelper.NEW_REQUEST_URL + urlz;
        return new JsonObjectRequest
                (Request.Method.POST, url,data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG , response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(TAG, error.getLocalizedMessage() + error.getMessage());
                    }
                });
    }

}
