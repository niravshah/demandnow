package com.demandnow.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.demandnow.GDNApiHelper;
import com.demandnow.GDNSharedPrefrences;
import com.demandnow.GDNVolleySingleton;
import com.demandnow.R;
import com.demandnow.model.JobInfo;
import com.demandnow.model.ParentJobInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirav on 16/12/2015.
 */
public class PendingJobsExpandableAdapter extends ExpandableRecyclerAdapter<PendingJobsExpandableAdapter.JobsParentViewHolder, PendingJobsExpandableAdapter.JobsChildViewHolder> {

    private final LayoutInflater mInflater;
    private String TAG="PendingJobsExpandableAdapter";
    private Context ctx;
    private ProgressDialog mProgressDialog;

    public PendingJobsExpandableAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        ctx = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public JobsParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.pending_jobs_parent, viewGroup, false);
        return new JobsParentViewHolder(view);
    }

    @Override
    public JobsChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.pending_jobs_child, viewGroup, false);
        return new JobsChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(final JobsParentViewHolder jobsParentViewHolder, int i, Object o) {
        ParentJobInfo info = (ParentJobInfo) o;
        jobsParentViewHolder.mCrimeTitleTextView.setText(info.getTitle());
        jobsParentViewHolder.mTotal.setText("£" + info.getChildObjectList().size() * 4);
        jobsParentViewHolder.pInfo = info;
        ImageRequest request = new ImageRequest(info.getSphoto(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        jobsParentViewHolder.mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        jobsParentViewHolder.mImageView.setImageResource(R.drawable.profile);
                    }
                });

        GDNVolleySingleton.getInstance(ctx).addToRequestQueue(request);
    }

    @Override
    public void onBindChildViewHolder(JobsChildViewHolder jobsChildViewHolder, int i, Object o) {
        JobInfo job = (JobInfo) o;
        jobsChildViewHolder.mDeliveryAddressText.setText(job.getJobId());
        jobsChildViewHolder.mDeliveryDate.setText(job.getCreated().toString());

    }

    public class JobsParentViewHolder extends ParentViewHolder {

        public TextView mCrimeTitleTextView;
        public TextView mTotal;
        public ImageButton mParentDropDownArrow;
        public ImageView mImageView;
        public Button payButton;
        public ParentJobInfo pInfo;

        public JobsParentViewHolder(View itemView) {
            super(itemView);
            mCrimeTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_crime_title_text_view);
            mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_list_item_expand_arrow);
            mTotal = (TextView) itemView.findViewById(R.id.parent_list_item_total_amount);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView2);
            payButton = (Button) itemView.findViewById(R.id.pay_button);
            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = GDNApiHelper.PAYMENT_URL + pInfo.getChildObjectList().size() * 300 + "/GBP/" + pInfo.getSid();
                    List<String> jobIds = new ArrayList<String>();
                    List<Object> childObjectList = pInfo.getChildObjectList();
                    for(int i=0;i<childObjectList.size();i++){
                        JobInfo child = (JobInfo) childObjectList.get(i);
                        jobIds.add(child.getJobId());
                    }
                    Log.i(TAG, url);
                    showProgress();
                    postCharge(url, jobIds, pInfo.getSname(),pInfo.getSphoto(),pInfo.getSid());
                }
            });
        }
    }

    public class JobsChildViewHolder extends ChildViewHolder {

        public TextView mDeliveryAddressText;
        public TextView mDeliveryDate;

        public JobsChildViewHolder(View itemView) {
            super(itemView);
            mDeliveryAddressText = (TextView) itemView.findViewById(R.id.child_list_item_delivery);
            mDeliveryDate =   (TextView) itemView.findViewById(R.id.child_list_item_delivery_date);
        }
    }

    private void postCharge(String url, List<String> jobIds,  final String sname, final String sphoto, final String sid) {

        JSONArray arr = new JSONArray(jobIds);
        JSONObject data = new JSONObject();
        try {
            data.put("token", GDNSharedPrefrences.getToken());
            data.put("jobIds",arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgress();
                        showRatingsModal(sname, sphoto, sid);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("PendingJobsAdapter", "postCharge - " + error.getLocalizedMessage() + error.getMessage());
                    }
                });

        GDNVolleySingleton.getInstance(ctx).addToRequestQueue(jsObjRequest);
    }

    private void showRatingsModal(String sname, String sphoto, final String sid) {
        final Dialog rankDialog = new Dialog(ctx, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.dialog_ratings);
        rankDialog.setCancelable(true);

        ImageView imgView = (ImageView) rankDialog.findViewById(R.id.profile_image);

        Picasso.with(ctx).load(sphoto)
                .placeholder(R.drawable.profile).into(imgView);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText(sname);

        final RatingBar rBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRatingsToServer(sid, rBar.getRating());
                rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }

    private void sendRatingsToServer(String sid, float rating) {
        String url = GDNApiHelper.NEW_RATING + rating + "/" + sid ;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("PendingJobsAdapter", "postCharge - " + error.getLocalizedMessage() + error.getMessage());
                    }
                });

        GDNVolleySingleton.getInstance(ctx).addToRequestQueue(jsObjRequest);
    }

    private void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(ctx);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Processing. Please wait...");
        }

        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}