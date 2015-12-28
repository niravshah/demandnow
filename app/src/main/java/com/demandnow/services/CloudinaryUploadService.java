package com.demandnow.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Nirav on 28/12/2015.
 */
public class CloudinaryUploadService extends IntentService {

    private static final String TAG = "SubmitNewJobService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public CloudinaryUploadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


    }



}
