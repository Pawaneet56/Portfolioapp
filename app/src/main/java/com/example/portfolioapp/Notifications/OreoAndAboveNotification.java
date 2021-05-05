package com.example.portfolioapp.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class OreoAndAboveNotification extends ContextWrapper {

    private static final String Id = "some_id";

    private static final String Name = "PortFolioApp";

    private NotificationManager notificationManager;

    public OreoAndAboveNotification(Context base) {
        super(base);

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O)
        {
            createchannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createchannel() {

        NotificationChannel notificationChannel = new NotificationChannel(Id,Name, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager()
    {
        if(notificationManager==null)
        {
            notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification(String title, String body, PendingIntent pintent, Uri sounduri, String icon)
    {
        return new Notification.Builder(getApplicationContext(),Id)
                .setContentIntent(pintent)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(sounduri)
                .setAutoCancel(true)
                .setSmallIcon(Integer.parseInt(icon));
    }


}
