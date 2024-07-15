package com.example.money_manager.contract.model;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.money_manager.AlarmReceiver;
import com.example.money_manager.contract.CreateReminderContract;
import com.example.money_manager.entity.Reminder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateReminderModel implements CreateReminderContract.Model {

    FirebaseFirestore db ;

    public CreateReminderModel(){
        db =  FirebaseFirestore.getInstance();
    }

    @Override
    public void createNewReminderToDB(Reminder reminder, String notificationId, OnCreateNewReminderListener listener) {
        DocumentReference user = db
                .collection("accounts")
                .document(reminder.getAccount().getEmail());
        Map<String, Object> reminderData = new HashMap<>();
        reminderData.put("name", reminder.getName());
        reminderData.put("frequency", reminder.getFrequency());
        reminderData.put("dateTime", reminder.getDatetime());
        reminderData.put("comment", reminder.getComment());
        reminderData.put("account", user);
        reminderData.put("isActive", reminder.isActive());
        db.collection("reminders").document(notificationId)
                .set(reminderData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getMessage());

                    }
                });
    }
    @SuppressLint("ScheduleExactAlarm")
    @Override
    public  void scheduleOneTimeNotification(Context context, int notificationId, String title, String message, Reminder reminder, Calendar calendar) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction("Default");
            intent.putExtra("notificationId", notificationId);
            intent.putExtra("title", title);
            intent.putExtra("message", message);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    @Override
    public void scheduleEvery1MinuteNotification(Context context, int notificationId, String title, String message, Reminder reminder, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("Default");
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.MINUTE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60, pendingIntent);
    }

    @Override
    public void scheduleDailyNotification(Context context, int notificationId, String title, String message, Reminder reminder, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("Default");
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void scheduleWeeklyNotification(Context context, int notificationId, String title, String message, Reminder reminder, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("Default");
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
