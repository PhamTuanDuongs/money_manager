package com.example.money_manager.contract.model;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.money_manager.AlarmReceiver;
import com.example.money_manager.contract.ListReminderContract;
import com.example.money_manager.entity.Reminder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListReminderModel implements ListReminderContract.Model {

    @Override
    public void getReminders(OnFinishedListener listener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference remindersRef = firestore.collection("reminders");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentAccount = currentUser.getEmail();

            remindersRef.whereEqualTo("account", firestore.document("accounts/" + currentAccount)).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Reminder> reminders = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Reminder reminder = new Reminder();
                        reminder.setId(doc.getId());
                        reminder.setName(doc.get("name", String.class));
                        reminder.setComment(doc.get("comment", String.class));
                        reminder.setDateTime(doc.get("dateTime", Timestamp.class));
                        reminder.setFrequency(doc.get("frequency", String.class));
                        //reminder.setActive(doc.get("isActive",Boolean.class));
                        reminders.add(reminder);
                    }
                    listener.onFinished(reminders);
                } else {
                    listener.onFailure(task.getException());
                }
            });
        }else {
            listener.onFailure(new Exception("No account found"));
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void scheduleNotification(Context context, int notificationId, String title, String message, Reminder reminder, Calendar calendar, int repeatType) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("Default");
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            switch (repeatType) {
                case 1:
                    calendar.add(Calendar.MINUTE, 1);
                    break;
                case 2:
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    break;
                case 3:
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                default:
                    break;
            }
        }

        if (repeatType == 0) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            long repeatInterval = 0;
            switch (repeatType) {
                case 1:
                    repeatInterval = 1000 * 60; // 1 minute
                    break;
                case 2:
                    repeatInterval = AlarmManager.INTERVAL_DAY; // 1 day
                    break;
                case 3:
                    repeatInterval = AlarmManager.INTERVAL_DAY * 7; // 1 week
                    break;
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), repeatInterval, pendingIntent);
        }
    }
}
