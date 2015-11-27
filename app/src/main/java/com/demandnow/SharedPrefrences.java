package com.demandnow;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by Nirav on 21/11/2015.
 */
public class SharedPrefrences {

    private static GoogleMap map;
    private static Location mLastLocation;
    private static String acctName;
    private static ArrayList<String> subs = new ArrayList<>();
    private static String currentService;

    public static String getAcctEmail() {
        return acctEmail;
    }

    public static void setAcctEmail(String acctEmail) {
        SharedPrefrences.acctEmail = acctEmail;
    }

    private static String acctEmail;

    public static String getPhotUrl() {
        return photUrl;
    }

    public static void setPhotUrl(String photUrl) {
        SharedPrefrences.photUrl = photUrl;
    }

    private static String photUrl;

    public static GoogleMap getMap() {
        return SharedPrefrences.map;
    }

    public static void setMap(GoogleMap map) {
        SharedPrefrences.map = map;
    }

    public static Location getLastLocation() {
        return mLastLocation;
    }

    public static void setLastLocation(Location mLastLocation) {
        SharedPrefrences.mLastLocation = mLastLocation;
    }

    public static String getAcctName() {
        return acctName;
    }

    public static void setAcctName(String acctName) {
        SharedPrefrences.acctName = acctName;
    }

    public static ArrayList<String> getSubs() {
        return subs;
    }

    public static void setSubs(ArrayList<String> subs) {
        SharedPrefrences.subs = subs;
    }

    public static String getCurrentService() {
        return currentService;
    }

    public static void setCurrentService(String currentService) {
        SharedPrefrences.currentService = currentService;
    }
}
