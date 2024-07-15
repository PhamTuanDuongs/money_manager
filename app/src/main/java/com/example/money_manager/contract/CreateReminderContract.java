package com.example.money_manager.contract;

import android.content.Context;

import com.example.money_manager.entity.Reminder;

import java.util.Calendar;

public class CreateReminderContract {
    public interface View {

        void showDate(String date);
        void showTime(String time);
        void showError(String message);
        void createNewReminderSuccess();
        void createNewReminderError();
    }

    public interface Presenter {
        void onDateClicked();
        void onTimeClicked();
        void createNewReminder(Context context, String name, String frequencey, String date, String time, String comment, String account);
    }

    public interface Model {
        void createNewReminderToDB(Reminder reminder, String notificationId, OnCreateNewReminderListener listener);
        void scheduleOneTimeNotification(Context context, int notificationId, String title, String message, Reminder reminder, Calendar calendar);

        void scheduleEvery1MinuteNotification(Context context, int notificationId, String title,
                                              String message, Reminder reminder, Calendar calendar);

        void scheduleDailyNotification(Context context, int notificationId, String title,
                                       String message, Reminder reminder, Calendar calendar);

        void scheduleWeeklyNotification(Context context, int notificationId, String title,
                                        String message, Reminder reminder, Calendar calendar);


        interface OnCreateNewReminderListener {
            void onSuccess();
            void onError(String message);
        }
    }
}
