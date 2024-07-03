package com.example.money_manager.contract.presenter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;

import com.example.money_manager.AlarmReceiver;
import com.example.money_manager.contract.CreateReminderContract;
import com.example.money_manager.contract.model.CreateReminderModel;
import com.example.money_manager.entity.Reminder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CreateReminderPresenter implements CreateReminderContract.Presenter {
    private CreateReminderContract.View view;
    private CreateReminderModel model;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public CreateReminderPresenter(CreateReminderContract.View view) {
        this.view = view;
        model = new CreateReminderModel();
    }

    @Override
    public void onDateClicked() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH ) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(((Fragment) view).getActivity(), (view, selectedYear, selectedMonth, selectedDay) -> {
            if (this.view != null) {
                this.view.showDate(String.format("%d-%02d-%02d", selectedYear, selectedMonth, selectedDay));
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public void onTimeClicked() {
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(((Fragment) view).getActivity(), (view, selectedHourOfDay, selectedMinute) -> {
            if (this.view != null) {
                this.view.showTime(String.format("%02d:%02d", selectedHourOfDay, selectedMinute));
            }
        }, hourOfDay, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onClickCreateNewReminder() {

    }

    @Override
    public void defaultReminder(Context context) {

    }

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void createNewReminder(Context context, String title, String frequencey, String strDate, String strTime, String comment, String account ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String[]date = strDate.split("-");
            String[]time = strTime.split(":");
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction("Default");
            int notificationId = getNotificationId();
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
            Reminder reminder = new Reminder(title, frequencey, strDate, strTime, comment, account);

            switch (frequencey){
                case "Once":
                    scheduleOneTimeNotification(context,notificationId, title, comment, reminder, calendar);
                    break;
                case "Every 1 Minute":
                    scheduleEvery1MinuteNotification(context,notificationId, title, comment, reminder, calendar);
                    break;
                case "Daily":
                    scheduleDailyNotification(context, notificationId, title, comment, reminder, calendar);
                    break;
                case "Weekly":
                    scheduleWeeklyNotification(context, notificationId, title, comment, reminder, calendar);
                    break;
            }
        }

    }



    public  void scheduleOneTimeNotification(Context context, int notificationId, String title, String message, Reminder reminder, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        model.createNewReminderToDB(reminder, Integer.toString(notificationId), new CreateReminderContract.Model.OnCreateNewReminderListener() {
            @Override
            public void onSuccess() {
                view.createNewReminderSuccess();
            }

            @Override
            public void onError(String message) {
                view.createNewReminderError();
            }
        });
    }

    public  void scheduleEvery1MinuteNotification(Context context, int notificationId, String title,
                                                 String message, Reminder reminder, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("Default");
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.MINUTE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000L, pendingIntent);

        model.createNewReminderToDB(reminder, Integer.toString(notificationId), new CreateReminderContract.Model.OnCreateNewReminderListener() {
            @Override
            public void onSuccess() {
                view.createNewReminderSuccess();
            }

            @Override
            public void onError(String message) {
                view.createNewReminderError();
            }
        });
    }



    public  void scheduleDailyNotification(Context context, int notificationId, String title,
                                           String message, Reminder reminder, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("Default");
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        model.createNewReminderToDB(reminder, Integer.toString(notificationId), new CreateReminderContract.Model.OnCreateNewReminderListener() {
            @Override
            public void onSuccess() {
                view.createNewReminderSuccess();
            }

            @Override
            public void onError(String message) {
                view.createNewReminderError();
            }
        });
    }


    public  void scheduleWeeklyNotification(Context context, int notificationId, String title,
                                           String message, Reminder reminder, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("Default");
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        model.createNewReminderToDB(reminder, Integer.toString(notificationId), new CreateReminderContract.Model.OnCreateNewReminderListener() {
            @Override
            public void onSuccess() {
                view.createNewReminderSuccess();
            }

            @Override
            public void onError(String message) {
                view.createNewReminderError();
            }
        });
    }
    private int getNotificationId(){
        int time = (int)new Date().getTime();
        return time;
    }
}
