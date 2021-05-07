package com.example.portfolioapp.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.portfolioapp.Fragments.HomeFragment;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class  FirebaseMessaging extends FirebaseMessagingService {


    private static final String ADMIN_CHANNEL_ID = "admin_channel";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SharedPreferences sp = getSharedPreferences("Sp_USER",MODE_PRIVATE);
        String currentUser = sp.getString("current_uid","None");

        String pid = remoteMessage.getData().get("pid");
        String sender = remoteMessage.getData().get("sender");
        String title = remoteMessage.getData().get("nTitle");
        String description = remoteMessage.getData().get("pDescription");

        assert sender != null;
        if(!sender.equals(currentUser))
        {
            showPostNotification(pid,title,description);
        }
    }

    private void showPostNotification(String pid, String title, String description) {

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int NotificationId = new Random().nextInt(3000);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            setupPostNotificationChannel(notificationManager);
        }

        Intent intent = new Intent(this,MainActivity.class);
        //intent.putExtra("Fragment_Name","HomeFragment");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeicon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_notification);

        Uri notificationSounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largeicon)
                .setContentTitle(title)
                .setContentText(description)
                .setSound(notificationSounduri)
                .setContentIntent(pendingIntent);

        notificationManager.notify(NotificationId,builder.build());



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupPostNotificationChannel(NotificationManager notificationManager) {

        CharSequence channelName = "New Notification";
        String channelDescription = "Device to device post notification";

        NotificationChannel adminchannel = new NotificationChannel(ADMIN_CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_HIGH);
        adminchannel.setDescription(channelDescription);
        adminchannel.enableLights(true);
        adminchannel.setLightColor(Color.RED);
        adminchannel.enableVibration(true);

        if(notificationManager!=null)
        {
            notificationManager.createNotificationChannel(adminchannel);
        }


    }
}
