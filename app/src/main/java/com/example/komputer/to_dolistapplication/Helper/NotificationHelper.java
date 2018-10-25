package com.example.komputer.to_dolistapplication.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.komputer.to_dolistapplication.R;

import static com.example.komputer.to_dolistapplication.R.string.notification_title;

public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_ID = "myChannelId";
    public static final String CHANNEL_NAME = "myChannelName";

    public NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannel() {

        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);

        notificationChannel.setDescription("channelDescription");
        notificationChannel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(true);
    }

    public NotificationManager getNotificationManager() {
        if(notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification(PendingIntent pendingIntent, String message){

        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(message)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }
}
