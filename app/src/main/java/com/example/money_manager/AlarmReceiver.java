package com.example.money_manager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.cardemulation.CardEmulation;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.money_manager.contract.presenter.ListReminderPresenter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "111";
    @SuppressLint("NotificationPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
            if(Objects.equals("Default", intent.getAction())){
                int notificationId = intent.getIntExtra("notificationId", -1);
                String title = intent.getStringExtra("title");
                String message = intent.getStringExtra("message");
                String frequency = intent.getStringExtra("frequency");

                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Money_Manager", NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setCategory(CardEmulation.CATEGORY_PAYMENT)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSmallIcon(R.drawable.dollar);
                notificationManager.notify(notificationId, builder.build());

                if ("Once".equals(frequency)) {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("reminders").document(String.valueOf(notificationId))
                            .update("isActive", false)
                            .addOnSuccessListener(aVoid -> {
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure
                            });
                }
            }
    }
}
