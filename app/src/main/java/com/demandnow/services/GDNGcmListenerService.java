package com.demandnow.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.demandnow.MainActivity;
import com.demandnow.R;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;

/**
 * Created by Nirav on 29/11/2015.
 */
public class GDNGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private String requesterId;
    private String jobId;
    private String address;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String type = data.getString("type");
        String message = data.getString("gcm.notification.message");
        String details = data.getString("details");

        switch (type){
            case "PAYMENT":
                processPaymentNotification(details, message);
                break;
            case "JOB":
                processJobNotification(details,message);
                break;
            default:
                break;

        }

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);


    }

    private void processJobNotification(String details, String message) {
        String[] parts = details.split(":");
        requesterId = parts[0];
        jobId = parts[1];
        address = parts[6];
        message = message + "-" + jobId;
        sendNotification(message, address);

    }

    private void processPaymentNotification(String details, String message) {
        sendNotification(message,details);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String subMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_logo_2)
                .setContentTitle(message)
                .setContentText(subMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }
}