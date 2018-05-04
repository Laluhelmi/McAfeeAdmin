package com.example.l.macprojectadmin.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.l.macprojectadmin.App.AppSession;
import com.example.l.macprojectadmin.Claim;
import com.example.l.macprojectadmin.MainActivity;
import com.example.l.macprojectadmin.MyLib.UploadFile;
import com.example.l.macprojectadmin.R;
import com.example.l.macprojectadmin.Validasi;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by L on 13/12/17.
 */

public class TerimaNotif extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Handler handler = new Handler(Looper.getMainLooper());
        Intent intent = new Intent("notif");
        if(remoteMessage.getData().get("type").equals("user_validasi")){
            String user = remoteMessage.getData().get("user");
            intent.putExtra("type","validasi");
            sendNotification(user+" Meminta Validasi","User Validasi"
            ,Validasi.class);
            AppSession session = new AppSession(this);
            session.setBadgeCount(session.getBadgeCount()+1);

            MainActivity.setBadge(getApplicationContext(),session.getBadgeCount());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }else if(remoteMessage.getData().get("type").equals("claim")){
            String user = remoteMessage.getData().get("nama_user");
            sendNotification(user+" mangkalim Hadiah","Claim Hadiah",
                    Claim.class);
            intent.putExtra("type","claim");
            AppSession session = new AppSession(this);
            session.setBadgeCount(session.getBadgeCount()+1);

            MainActivity.setBadge(getApplicationContext(),session.getBadgeCount());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
    private void sendNotification(String messageBody,String type,
                                  Class<?> c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(type)
                .setContentText(messageBody)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
