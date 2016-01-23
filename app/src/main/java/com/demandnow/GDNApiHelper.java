package com.demandnow;

/**
 * Created by Nirav on 12/12/2015.
 */
public class GDNApiHelper {

    public static final String SERVER = "http://morph-stadium.codio.io:3000";
    public static final String BASE_URL = SERVER + "/api";
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String ACTIVATE_URL = BASE_URL + "/activate";
    public static final String GCM_URL = BASE_URL + "/gcm";
    public static final String SERVICES_URL = BASE_URL + "/services";
    public static final String ADDRESS_URL = BASE_URL + "/addresses";
    public static final String JOBS_URL = BASE_URL + "/jobs/" + GDNSharedPrefrences.getAcctId();
    public static final String JOB_URL = BASE_URL + "/job/" + GDNSharedPrefrences.getAcctId() + "/";
    public static final String NEW_REQUEST_URL = BASE_URL + "/job/";
    public static final String ADD_PAYMENT_METHOD_URL = BASE_URL + "/stripe/" + GDNSharedPrefrences.getAcctId() + "/token";
    public static final String PAYMENT_URL = BASE_URL + "/stripe/charge/" + GDNSharedPrefrences.getAcctId() + "/";

    public static final String NEW_RATING = BASE_URL + "/ratings/" + GDNSharedPrefrences.getAcctId() + "/";
}
