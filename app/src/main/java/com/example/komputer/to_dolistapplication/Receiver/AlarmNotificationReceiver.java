package com.example.komputer.to_dolistapplication.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import com.example.komputer.to_dolistapplication.Activity.MainActivity;
import com.example.komputer.to_dolistapplication.Helper.NotificationHelper;
import com.example.komputer.to_dolistapplication.R;
import java.util.Random;


public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendNotificationAPI26(context, getNotificationText());
        }else{
            sendNotification(context, getNotificationText());
        }


    }



    public static String getNotificationText(){

        String[] textArray = {"Don't forget about today's tasks :)", "Did you do today's tasks?", "I hope you done your tasks"};
        int randomText = new Random().nextInt(textArray.length);
        String text = (textArray[randomText]);

        return text;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotificationAPI26(Context context, String message) {

        NotificationHelper notificationHelper;
        Notification.Builder notificationBuilder;

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationHelper = new NotificationHelper(context);
        notificationBuilder = notificationHelper.getNotification(pendingIntent, message);

        notificationHelper.getNotificationManager().notify(new Random().nextInt(), notificationBuilder.build());
    }

    private void sendNotification(Context context, String message) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(message)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setPriority(3)
                .setContentIntent(pendingIntent);

        notificationManager.notify(new Random().nextInt(), builder.build());
    }

}

