package com.demandnow.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demandnow.GDNApiHelper;
import com.demandnow.GDNBaseActivity;
import com.demandnow.GDNVolleySingleton;
import com.demandnow.R;

import org.json.JSONObject;

/**
 * Created by Nirav on 13/12/2015.
 */
public class JobDetailViewActivity extends GDNBaseActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selector);
        renderNavigationDrawer();
        renderChildActivityToolbar();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String jobId = extras.getString("JOB_ID");
            getJobDetailsFromServer(jobId);
        }
    }

    private void getJobDetailsFromServer(String jobId) {
        String url = GDNApiHelper.JOB_URL + jobId;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("VolleyErorr", "- getJobDetailsFromServer - " + error.getLocalizedMessage() + error.getMessage());
                    }
                });

        GDNVolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
}
