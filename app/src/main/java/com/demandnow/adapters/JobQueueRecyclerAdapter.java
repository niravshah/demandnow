package com.demandnow.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demandnow.R;
import com.demandnow.activity.JobDetailViewActivity;
import com.demandnow.model.JobInfo;

import java.util.List;

/**
 * Created by Nirav on 21/11/2015.
 */
public class JobQueueRecyclerAdapter extends RecyclerView.Adapter<JobQueueRecyclerAdapter.ViewHolder> {

    private List<JobInfo> mItems;
    private Context context;
    public JobQueueRecyclerAdapter(Context ctx, List<JobInfo> items) {
        context = ctx; mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_queue_list_row, viewGroup, false);
        return new ViewHolder(context,v);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mJobItem;
        private final TextView mJobItemstatus;
        private Context context;


        ViewHolder(Context ctx, View v) {
            super(v);
            mJobItem = (TextView)v.findViewById(R.id.job_item);
            mJobItemstatus = (TextView)v.findViewById(R.id.job_item_status);
            context = ctx;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); // gets item position
            Toast.makeText(context, "Clicked Item: " + position, Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, JobDetailViewActivity.class));
        }
    }

}
