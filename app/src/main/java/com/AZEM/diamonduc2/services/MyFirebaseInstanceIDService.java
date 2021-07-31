package com.AZEM.diamonduc2.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.initialActivities.MainActivity;
import com.AZEM.diamonduc2.initialActivities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().isEmpty()){
            showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }else{
            showNotification(remoteMessage.getData());
        }
        if (remoteMessage.getNotification().getTitle().equals("Try your luck now")){
            startActivity(getStartCommandIntent(new Intent(getApplicationContext(), MainActivity.class)));
        }else {
            startActivity(getStartCommandIntent(new Intent(getApplicationContext(), MainActivity.class)));
        }
    }
    private void showNotification(Map<String,String> data){
        String title=data.get("title").toString();
        String body=data.get("body").toString();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "AZEM.diamonduc2.services.test";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification"
            ,NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Diamond UC");
            notificationChannel.enableLights(true);
            notificationChannel.canShowBadge();
            notificationChannel.setLightColor(Color.rgb(253,192,7));
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.gems)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("info");
        notificationManager.notify(new Random().nextInt(),builder.build());
    }
    private void showNotification(String title,String body){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "AZEM.diamonduc2.services.test";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification"
                    ,NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Diamond Uc");
            notificationChannel.enableLights(true);
            notificationChannel.canShowBadge();
            notificationChannel.setLightColor(Color.rgb(253,192,7));
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.gems)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("info");
        notificationManager.notify(new Random().nextInt(),builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
