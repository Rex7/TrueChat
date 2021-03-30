package com.example.homepage;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static int NOTIFICATION_ID=1;
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        BuildNotification(remoteMessage);
        Intent pushNotification = new Intent("pushNotification");
        String title=remoteMessage.getNotification().getTitle();
        String messageBody=remoteMessage.getNotification().getBody();
        String name=remoteMessage.getData().get("name");
        Log.v("payloadDataDemo","pay"+name);


//        pushNotification.putExtra("message", remoteMessage.getData().get("message"));
//        pushNotification.putExtra("title", remoteMessage.getData().get("Title"));
//        pushNotification.putExtra("name", remoteMessage.getData().get("name"));
        pushNotification.putExtra("message", messageBody);
        pushNotification.putExtra("title", title);
        pushNotification.putExtra("name", name);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }

    private void BuildNotification(RemoteMessage remoteMessage) {
    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationCompat= new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);
        if(NOTIFICATION_ID>1073741824){
           NOTIFICATION_ID=0;
        }
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID++,notificationCompat.build());

    }
}
