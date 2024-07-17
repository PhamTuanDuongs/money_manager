package com.example.money_manager.contract.presenter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.money_manager.AlarmReceiver;
import com.example.money_manager.contract.CreateReminderContract;
import com.example.money_manager.contract.model.CreateReminderModel;
import com.example.money_manager.entity.Account;
import com.example.money_manager.entity.Reminder;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
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
        int month = c.get(Calendar.MONTH );
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(((Fragment) view).getActivity(), (view, selectedYear, selectedMonth, selectedDay) -> {
            if (this.view != null) {
                this.view.showDate(String.format("%d-%02d-%02d", selectedYear, selectedMonth, selectedDay));
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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


    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void createNewReminder(Context context, String title, String frequencey, String strDate, String strTime, String comment, String email ) {
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetimeString = strDate + " " +  strTime + ":00";
            Date datetime = null;
            try {
                datetime = sdf.parse(datetimeString);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Timestamp timestamp = new Timestamp(datetime);
            Account account = new Account(email);
            Reminder reminder = new Reminder(title, frequencey, timestamp, comment, account, true);

            switch (frequencey){
                case "Once":
                    if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Create new reminder");
                        builder.setMessage("The reminder date is in the past, so the reminder will not be shown. Save anyway?");
                        builder.setCancelable(true);
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();
                            reminder.setActive(false);
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
                        });
                        builder.setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else {
                        model.scheduleOneTimeNotification(context,notificationId, title, comment, reminder, calendar);
                        reminder.setActive(true);
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
                    break;
                case "Every 1 Minute":
                    model.scheduleEvery1MinuteNotification(context,notificationId, title, comment, reminder, calendar);
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
                    break;
                case "Daily":
                    model.scheduleDailyNotification(context, notificationId, title, comment, reminder, calendar);
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
                    break;
                case "Weekly":
                    model.scheduleWeeklyNotification(context, notificationId, title, comment, reminder, calendar);
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
                    break;
            }
        }

    }
    private int getNotificationId(){
        int time = (int)new Date().getTime();
        return time;
    }
}
