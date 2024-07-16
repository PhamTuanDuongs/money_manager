package com.example.money_manager.contract.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.example.money_manager.AlarmReceiver;
import com.example.money_manager.contract.CreateReminderContract;
import com.example.money_manager.contract.ListReminderContract;
import com.example.money_manager.contract.model.ListReminderModel;
import com.example.money_manager.entity.Account;
import com.example.money_manager.entity.Reminder;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
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
    public void createNewNotification(Context context, String title, String frequency, String strDate, String strTime, String comment) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
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

            Reminder reminder = new Reminder();
            reminder.setName(title);
            reminder.setComment(comment);
            reminder.setFrequency(frequency);

            switch (frequency){
                case "Once":
                    if(calendar.getTimeInMillis() > System.currentTimeMillis())
                    {
                        model.scheduleNotification(context,notificationId, title, comment, reminder, calendar, 0);
                    }
                    break;
                    case "Every 1 Minute":
                        model.scheduleNotification(context,notificationId, title, comment, reminder, calendar, 1);
                        break;
                    case "Daily":
                        model.scheduleNotification(context, notificationId, title, comment, reminder, calendar, 2);
                        break;
                    case "Weekly":
                        model.scheduleNotification(context, notificationId, title, comment, reminder, calendar, 3);
                        break;
            }
        }
    }

    private int getNotificationId(){
        int time = (int)new Date().getTime();
        return time;
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
            createNewNotification(context, reminder.getName(), reminder.getFrequency(), formattedDate, formattedTime, reminder.getComment());
        }
    }

    @Override
    public void onFailure(Exception e) {
        view.showError(e.getMessage());
    }
}
