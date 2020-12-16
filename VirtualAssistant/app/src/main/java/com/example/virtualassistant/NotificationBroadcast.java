package com.example.virtualassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("ASSIGNMENT_NAME");

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, "Channel_1")
                .setSmallIcon(R.drawable.ic_baseline_assignment_24)
                .setContentTitle("Assignment Reminder")
                .setContentText(name + " is due soon.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(generateID(), builder.build());
    }

    private int generateID() {
        Random random = new Random();
        return random.nextInt(100000);
    }
}
