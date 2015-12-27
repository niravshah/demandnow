package com.demandnow.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.demandnow.GDNApiHelper;
import com.demandnow.GDNVolleySingleton;
import com.demandnow.R;
import com.demandnow.adapters.AllJobsRecyclerAdapter;
import com.demandnow.model.JobInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nirav on 26/12/2015.
 */
public class AllJobsTabFragment extends Fragment {
    private static final String TAB_POSITION = "tab_position";
    public static final String TAB_NAME = "Past";
    private SwipeRefreshLayout swipeContainer;
    private Boolean swipeRefresh = false;

    public AllJobsTabFragment() {

    }

    public static AllJobsTabFragment newInstance(int tabPosition) {
        AllJobsTabFragment fragment = new AllJobsTabFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.rv_all_job_queue, container, false);
        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.alljobs_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh = true;
                getCurrentJobQueueFromServer(recyclerView);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        getCurrentJobQueueFromServer(recyclerView);
        return v;
    }

    private void getCurrentJobQueueFromServer(final RecyclerView recyclerView) {

        String url = GDNApiHelper.JOBS_URL + "/all";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<JobInfo> jobInfos = new ArrayList<>();
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject obj = (JSONObject) response.get(i);
                                jobInfos.add(new JobInfo(obj.getString("jobId"),obj.getString("currentStatus")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        recyclerView.setAdapter(new AllJobsRecyclerAdapter(getContext(),jobInfos));
                        if(swipeRefresh){
                            swipeContainer.setRefreshing(false);
                            Toast.makeText(getActivity(), "Swipe Refresh", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("AllJobsTabFragment", "getCurrentJobQueueFromServer - " + error.getLocalizedMessage() + error.getMessage());
                    }
                });

        GDNVolleySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }


}



