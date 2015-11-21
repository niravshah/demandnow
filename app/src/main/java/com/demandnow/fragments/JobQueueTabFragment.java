package com.demandnow.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demandnow.R;
import com.demandnow.adapters.JobQueueRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by Nirav on 20/11/2015.
 */
public class JobQueueTabFragment extends Fragment {
    private static final String TAB_POSITION = "tab_position";
    public static final String TAB_NAME = "Job Queue";

    public JobQueueTabFragment() {

    }

    public static JobQueueTabFragment newInstance(int tabPosition) {
        JobQueueTabFragment fragment = new JobQueueTabFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        int tabPosition = args.getInt(TAB_POSITION);

        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            items.add("Tab #" + tabPosition + " item #" + i);
        }

        View v =  inflater.inflate(R.layout.job_queue_list_view, container, false);
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new JobQueueRecyclerAdapter(items));

        return v;
    }}
