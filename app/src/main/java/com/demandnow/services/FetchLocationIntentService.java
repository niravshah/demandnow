package com.demandnow.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.demandnow.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nirav on 07/12/2015.
 */
public class FetchLocationIntentService extends IntentService {

    private static final String TAG = "FetchLocationService";
    protected ResultReceiver mReceiver;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public FetchLocationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        String address = intent.getStringExtra(Constants.ADDRESS_DATA_EXTRA);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(address,1);

        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    address, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, null,errorMessage);
        } else {
            Address add = addresses.get(0);
            LatLng latlng = new LatLng(add.getLatitude(),add.getLongitude());
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,latlng,null);
        }

    }

    private void deliverResultToReceiver(int resultCode, LatLng latLng, String errorMessgae) {
        Bundle bundle = new Bundle();
        if(latLng != null) {
            bundle.putDouble(Constants.LATITUDE, latLng.latitude);
            bundle.putDouble(Constants.LONGITUDE, latLng.longitude);
        }
        mReceiver.send(resultCode, bundle);
    }
}
