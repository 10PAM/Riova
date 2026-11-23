/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: ReminderReceiver.java
About: Receives the daily alarm broadcast scheduled by ReminderScheduler and
       posts the wellness reminder notification. Tapping the notification opens
       the app. On Android 13+ the notification is only posted if the user has
       granted the POST_NOTIFICATIONS permission (requested from User Settings
       when reminders are turned on).
*/

package com.example.riova.receivers;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.riova.R;
import com.example.riova.activities.MainActivity;
import com.example.riova.utilities.ReminderScheduler;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Make sure the channel exists even if the app process was recreated.
        ReminderScheduler.createChannel(context);

        // Tapping the notification opens the app.
        Intent openApp = new Intent(context, MainActivity.class);
        openApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, openApp, flags);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, ReminderScheduler.CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)
                        .setContentTitle(context.getString(R.string.reminder_title))
                        .setContentText(context.getString(R.string.reminder_text))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);

        // On Android 13+ posting requires the POST_NOTIFICATIONS permission; if
        // it is not granted, skip posting rather than crash.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationManagerCompat.from(context)
                .notify(ReminderScheduler.NOTIFICATION_ID, builder.build());
    }
}
