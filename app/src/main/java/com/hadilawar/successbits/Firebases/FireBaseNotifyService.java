package com.hadilawar.successbits.Firebases;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hadilawar.successbits.MainActivity;
import com.hadilawar.successbits.R;

import java.util.Map;

/**
 * Created by l1s14bscs2083 on 8/3/2017.
 */

public class FireBaseNotifyService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data =remoteMessage.getData();


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.speaker)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.mission))
                        .setContentTitle("Be Infinite!")
                        .setContentText(data.get("quote"));
        // Handle data payload of FCM messages.

        Log.e(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.e(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.e(TAG, "FCM Data Message: " + remoteMessage.getData());


        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =  PendingIntent.getActivity(
                                                this,
                                                0,
                                                resultIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                               );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }



}