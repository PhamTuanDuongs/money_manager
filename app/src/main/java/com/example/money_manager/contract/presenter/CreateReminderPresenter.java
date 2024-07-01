package com.example.money_manager.contract.presenter;

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

    @Override
    public void createNewReminder(Context context, String name, String frequencey, String strDate, String strTime, String comment, String account ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String[]date = strDate.split("-");
            String[]time = strTime.split(":");
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction("Reminder");
            int notificationId = getNotificationId();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(date[0]) );
            calendar.set(Calendar.MONTH, Integer.parseInt(date[1]));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            calendar.set(Calendar.SECOND, 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int hour = LocalDateTime.now().getHour();
                int minute = LocalDateTime.now().getMinute();
                intent.putExtra("Time", hour + ":" + minute);
                intent.putExtra("notificationId", notificationId);
                intent.putExtra("title", name);
                intent.putExtra("message", comment);
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                pendingIntent=PendingIntent.getBroadcast(context,getNotificationId(),intent, PendingIntent.FLAG_IMMUTABLE);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Reminder reminder = new Reminder(name, frequencey, strDate, strTime, comment, account);
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

    }
    private int getNotificationId(){
        int time = (int)new Date().getTime();
        return time;
    }

}
