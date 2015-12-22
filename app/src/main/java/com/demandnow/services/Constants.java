package com.demandnow.services;

/**
 * Created by Nirav on 07/12/2015.
 */
public class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    public static final String ADDRESS_DATA_EXTRA = ".ADDRESS_DATA_EXTRA";
    public static final String LATITUDE = ".LATITUDE";
    public static final String LONGITUDE = ".LONGITUDE";
    public static final String POSTCODE = ".POSTCODE";

    public class SubmitNewJobService{
        public static final String DELIVERY_LATITUDE = "DELIVERY_LATITUDE";
        public static final String DELIVERY_LONGITUDE = "DELIVERY_LONGITUDE";
        public static final String PICKUP_LATITUDE = "PICKUP_LATITUDE";
        public static final String PICKUP_LONGITUDE = "PICKUP_LONGITUDE";
        public static final String SERVICE = "SERVICE";
        public static final String DELIVERY_ADDRESS = "DELIVERY_ADDRESS";
    }

}
