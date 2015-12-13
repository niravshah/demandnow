package com.demandnow.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demandnow.R;
import com.demandnow.model.JobInfo;

import java.util.List;

/**
 * Created by Nirav on 21/11/2015.
 */
public class JobQueueRecyclerAdapter extends RecyclerView.Adapter<JobQueueRecyclerAdapter.ViewHolder> {

    private List<JobInfo> mItems;

    public JobQueueRecyclerAdapter(List<JobInfo> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_queue_list_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        JobInfo item = mItems.get(i);
        viewHolder.mJobItem.setText(item.getJobId());
        viewHolder.mJobItemstatus.setText(item.getJobStatus());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mJobItem;
        private final TextView mJobItemstatus;


        ViewHolder(View v) {
            super(v);
            mJobItem = (TextView)v.findViewById(R.id.job_item);
            mJobItemstatus = (TextView)v.findViewById(R.id.job_item_status);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<JobInfo> list) {
        mItems.addAll(list);
        notifyDataSetChanged();
    }

}
