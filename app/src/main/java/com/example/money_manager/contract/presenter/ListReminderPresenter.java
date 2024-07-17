package com.example.money_manager.contract.presenter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.money_manager.AlarmReceiver;
import com.example.money_manager.contract.CreateReminderContract;
import com.example.money_manager.contract.ListReminderContract;
import com.example.money_manager.contract.model.ListReminderModel;
import com.example.money_manager.entity.Account;
import com.example.money_manager.entity.Reminder;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListReminderPresenter implements ListReminderContract.Presenter, ListReminderContract.Model.OnFinishedListener {
    private ListReminderContract.View view;
    private ListReminderContract.Model model;
    private Context context;

    public ListReminderPresenter(ListReminderContract.View view, Context context) {
        this.view = view;
        this.model = new ListReminderModel();
        this.context = context;
    }
    @Override
    public void loadReminders() {
        model.getReminders(this);
    }

    @Override
    public void onReminderClicked(Reminder reminder) {
        view.navigateToUpdateReminder(reminder);
    }

    @Override
    public void createNewNotification(Context context, Reminder reminder, String strDate, String strTime) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            String[]date = strDate.split("-");
            String[]time = strTime.split(":");
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction("Default");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[2]);
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year );
            calendar.set(Calendar.MONTH, month - 1);  // Calendar.MONTH is zero-based
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            int notificationId = Integer.parseInt(reminder.getId());
            String title = reminder.getName();
            String comment = reminder.getComment();
            String frequency = reminder.getFrequency();
            Boolean isActive = reminder.isActive();

            switch (frequency){
                case "Once":
                    if(calendar.getTimeInMillis() > System.currentTimeMillis())
                    {
                        model.scheduleNotification(context,notificationId, title, comment, reminder, calendar, 0,isActive);
                    }
                    break;
                    case "Every 1 Minute":
                        model.scheduleNotification(context,notificationId, title, comment, reminder, calendar, 1,isActive);
                        break;
                    case "Daily":
                        model.scheduleNotification(context, notificationId, title, comment, reminder, calendar, 2,isActive);
                        break;
                    case "Weekly":
                        model.scheduleNotification(context, notificationId, title, comment, reminder, calendar, 3,isActive);
                        break;
            }
        }
    }

    @Override
    public void updateNotification(Context context, Reminder reminder, String strDate, String strTime) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            int notificationId = Integer.parseInt(reminder.getId());
            String title = reminder.getName();
            String comment = reminder.getComment();
            String frequency = reminder.getFrequency();
            Boolean isActive = reminder.isActive();

            String[]date = strDate.split("-");
            String[]time = strTime.split(":");
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction("Default");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[2]);
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year );
            calendar.set(Calendar.MONTH, month - 1);  // Calendar.MONTH is zero-based
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            switch (frequency){
                case "Once":
                    if(calendar.getTimeInMillis() > System.currentTimeMillis())
                    {
                        model.scheduleNotification(context,notificationId, title, comment, reminder, calendar, 0,isActive);
                    }
                    break;
                case "Every 1 Minute":
                    model.scheduleNotification(context,notificationId, title, comment, reminder, calendar, 1,isActive);
                    break;
                case "Daily":
                    model.scheduleNotification(context, notificationId, title, comment, reminder, calendar, 2,isActive);
                    break;
                case "Weekly":
                    model.scheduleNotification(context, notificationId, title, comment, reminder, calendar, 3,isActive);
                    break;
            }
        }
    }

    @Override
    public void onFinished(List<Reminder> reminders) {
        view.showReminders(reminders);
        for (Reminder reminder : reminders) {
            Timestamp timestamp = reminder.getDateTime();
            Date date = timestamp.toDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(date);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = timeFormat.format(date);
            createNewNotification(context, reminder, formattedDate, formattedTime);
        }
    }

    @Override
    public void onFailure(Exception e) {
        view.showError(e.getMessage());
    }

    @Override
    public void onSwitchToggle(Reminder reminder, Context context) {
        model.updateReminder(reminder);
        if (!reminder.isActive()) {
            cancelNotification(reminder.getId(), context);
            model.scheduleNotification(context, Integer.parseInt(reminder.getId()), reminder.getName(), reminder.getComment(), reminder, Calendar.getInstance(), 0, false);
        } else {
            Timestamp timestamp = reminder.getDateTime();
            Date date = timestamp.toDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(date);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = timeFormat.format(date);
            updateNotification(context, reminder, formattedDate, formattedTime);
        }
    }

    public void cancelNotification(String reminderId, Context context) {
        int notificationId = Integer.parseInt(reminderId);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }
}
