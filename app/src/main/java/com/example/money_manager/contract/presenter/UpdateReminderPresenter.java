package com.example.money_manager.contract.presenter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.money_manager.AlarmReceiver;
import com.example.money_manager.contract.CreateReminderContract;
import com.example.money_manager.contract.UpdateReminderContract;
import com.example.money_manager.contract.model.UpdateReminderModel;
import com.example.money_manager.entity.Account;
import com.example.money_manager.entity.Reminder;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateReminderPresenter implements UpdateReminderContract.Presenter {
    private UpdateReminderContract.View view;
    private UpdateReminderModel model;

    public UpdateReminderPresenter(UpdateReminderContract.View view) {
        this.view = view;
        model = new UpdateReminderModel();
    }



    @Override
    public void onDateClicked(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
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
    public void onTimeClicked(Date datetime) {
        Calendar c = Calendar.getInstance();
        c.setTime(datetime);
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
    public void getReminderById(int id) {
         model.getReminderById(id, new UpdateReminderContract.Model.FireStoreReminderCallBack() {
            @Override
            public void onCallBack(Reminder reminder) {
               if(reminder != null) {
                   view.fillExistData(reminder);
               }
            }
        });
    }

    @Override
    public void onClickUpdateReminder(Context context, String title, String frequencey, String strDate, String strTime, String comment, String email, int notificationId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
                            model.updateReminderToDB(reminder, Integer.toString(notificationId), new CreateReminderContract.Model.OnCreateNewReminderListener() {
                                @Override
                                public void onSuccess() {
                                    view.updateReminderSuccess();
                                }

                                @Override
                                public void onError(String message) {
                                    view.updateReminderError();
                                }
                            });
                        });
                        builder.setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else {
                        model.updateScheduleOneTimeNotification(context,notificationId, title, comment, reminder, calendar);
                        reminder.setActive(true);
                        model.updateReminderToDB(reminder, Integer.toString(notificationId), new CreateReminderContract.Model.OnCreateNewReminderListener() {
                            @Override
                            public void onSuccess() {
                                view.updateReminderSuccess();
                            }

                            @Override
                            public void onError(String message) {
                                view.updateReminderError();
                            }
                        });
                    }
                    break;
                case "Every 1 Minute":
                    model.updateScheduleEvery1MinuteNotification(context,notificationId, title, comment, reminder, calendar);
                    model.updateReminderToDB(reminder, Integer.toString(notificationId), new CreateReminderContract.Model.OnCreateNewReminderListener() {
                        @Override
                        public void onSuccess() {
                            view.updateReminderSuccess();
                        }

                        @Override
                        public void onError(String message) {
                            view.updateReminderError();
                        }
                    });
                    break;
                case "Daily":
                    model.updateScheduleDailyNotification(context, notificationId, title, comment, reminder, calendar);
                    model.updateReminderToDB(reminder, Integer.toString(notificationId), new CreateReminderContract.Model.OnCreateNewReminderListener() {
                        @Override
                        public void onSuccess() {
                            view.updateReminderSuccess();
                        }

                        @Override
                        public void onError(String message) {
                            view.updateReminderError();
                        }
                    });
                    break;
                case "Weekly":
                    model.updateScheduleWeeklyNotification(context, notificationId, title, comment, reminder, calendar);
                    model.updateReminderToDB(reminder, Integer.toString(notificationId), new CreateReminderContract.Model.OnCreateNewReminderListener() {
                        @Override
                        public void onSuccess() {
                            view.updateReminderSuccess();
                        }

                        @Override
                        public void onError(String message) {
                            view.updateReminderError();
                        }
                    });
                    break;
            }
        }
    }
}
