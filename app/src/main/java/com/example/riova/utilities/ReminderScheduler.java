/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: ReminderScheduler.java
About: Schedules (and cancels) the daily wellness reminder that the "Enable
       reminders" switch in User Settings controls. Uses AlarmManager with an
       inexact daily repeat (so no exact-alarm permission is needed) to fire a
       broadcast once a day at REMINDER_HOUR; ReminderReceiver then posts the
       actual notification. Also creates the notification channel required on
       Android 8+ (API 26).

       Known limitation: inexact repeating alarms do not survive a device
       reboot. A future improvement is a RECEIVE_BOOT_COMPLETED receiver that
       calls schedule() again on boot.
*/

package com.example.riova.utilities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.riova.R;
import com.example.riova.receivers.ReminderReceiver;

import java.util.Calendar;

public class ReminderScheduler {

    /** Notification channel id for Riova's reminders. */
    public static final String CHANNEL_ID = "riova_reminders";

    /** Id used when posting the reminder notification. */
    public static final int NOTIFICATION_ID = 1001;

    // Request code that keeps the daily alarm's PendingIntent unique.
    private static final int REQUEST_CODE = 2001;

    // Hour of day (24h) the reminder fires. 9 = 9:00 AM.
    private static final int REMINDER_HOUR = 9;

    /**
     * Creates the notification channel (required on API 26+). Safe to call
     * repeatedly - creating an existing channel is a no-op.
     *
     * @param context any context.
     */
    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.reminder_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.reminder_channel_description));

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Schedules a daily reminder starting at the next REMINDER_HOUR. Calling
     * this again just updates the existing alarm, so it is safe to call on both
     * toggle-on and page load.
     *
     * @param context any context.
     */
    public static void schedule(Context context) {
        createChannel(context);

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, REMINDER_HOUR);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // If today's reminder time has already passed, start tomorrow.
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                buildPendingIntent(context));
    }

    /**
     * Cancels the daily reminder, if one is scheduled.
     *
     * @param context any context.
     */
    public static void cancel(Context context) {
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(buildPendingIntent(context));
        }
    }

    /**
     * Builds the (stable) PendingIntent that points at ReminderReceiver. Using
     * the same request code and action every time means schedule()/cancel()
     * always refer to the same alarm.
     */
    private static PendingIntent buildPendingIntent(Context context) {
        Intent intent = new Intent(context, ReminderReceiver.class);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, flags);
    }
}
