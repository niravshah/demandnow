package com.demandnow;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by Nirav on 21/11/2015.
 */
public class GDNSharedPrefrences {

    public static final String SENT_TOKEN_TO_SERVER="sent_token_to_server";
    public static final String REGISTRATION_COMPLETE="registration_complete";

    private static GoogleMap map;
    private static Location mLastLocation;
    private static String acctName;
    private static ArrayList<String> subs = new ArrayList<>();
    private static String currentService;
    private static String acctId;
    private static String serviceId;
    private static Boolean paymentVerified;
    private static Boolean phoneVerified;
    private static String token;


    public static String getAcctEmail() {
        return acctEmail;
    }

    public static void setAcctEmail(String acctEmail) {
        GDNSharedPrefrences.acctEmail = acctEmail;
    }

    private static String acctEmail;

    public static String getPhotUrl() {
        return photUrl;
    }

    public static void setPhotUrl(String photUrl) {
        GDNSharedPrefrences.photUrl = photUrl;
    }

    private static String photUrl;

    public static GoogleMap getMap() {
        return GDNSharedPrefrences.map;
    }

    public static void setMap(GoogleMap map) {
        GDNSharedPrefrences.map = map;
    }

    public static Location getLastLocation() {
        return mLastLocation;
    }

    public static void setLastLocation(Location mLastLocation) {
        GDNSharedPrefrences.mLastLocation = mLastLocation;
    }

    public static String getAcctName() {
        return acctName;
    }

    public static void setAcctName(String acctName) {
        GDNSharedPrefrences.acctName = acctName;
    }

    public static ArrayList<String> getSubs() {
        return subs;
    }

    public static void setSubs(ArrayList<String> subs) {
        GDNSharedPrefrences.subs = subs;
    }

    public static String getCurrentService() {
        return currentService;
    }

    public static void setCurrentService(String currentService) {
        GDNSharedPrefrences.currentService = currentService;
    }

    public static void setAcctId(String acctId) {
        GDNSharedPrefrences.acctId = acctId;
    }

    public static String getAcctId() {
        return acctId;
    }

    public static String getServiceId() {
        return serviceId;
    }

    public static void setServiceId(String serviceId) {
        GDNSharedPrefrences.serviceId = serviceId;
    }

    public static Boolean getPaymentVerified() {
        return paymentVerified;
    }

    public static void setPaymentVerified(Boolean paymentVerified) {
        GDNSharedPrefrences.paymentVerified = paymentVerified;
    }

    public static Boolean getPhoneVerified() {
        return phoneVerified;
    }

    public static void setPhoneVerified(Boolean phoneVerified) {
        GDNSharedPrefrences.phoneVerified = phoneVerified;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        GDNSharedPrefrences.token = token;
    }
}
