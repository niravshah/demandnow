package com.demandnow.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.ArrayList;

/**
 * Created by Nirav on 12/12/2015.
 */
public class PostcodesIntentService extends IntentService {


    private static final String TAG = "FetchAddressService";
    protected ResultReceiver mReceiver;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public PostcodesIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        String postCode = intent.getStringExtra(Constants.POSTCODE);
        ArrayList<String> postCodes = new ArrayList<>();
        postCodes.add(postCode);
        postCodes.add("NW95DZ");
        //get neighbouring postcodes from backend
        deliverResultToReceiver(Constants.SUCCESS_RESULT, postCodes);
    }

    private void deliverResultToReceiver(int resultCode, ArrayList<String> message) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
