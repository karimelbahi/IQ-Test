package com.example.playmaker.onlinequizapplication.BroadCastReceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.playmaker.onlinequizapplication.MainActivity;
import com.example.playmaker.onlinequizapplication.R;

public class AlermReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder((context), notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        builder.setSmallIcon(R.mipmap.ic_launch)
                .setContentTitle("Online IQ Test")
                .setContentText("Hey solve now and refresh your mind ^_^")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setVibrate(null)
                .setContentIntent(pendingIntent);
        // .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(0, builder.build());

    }
}
