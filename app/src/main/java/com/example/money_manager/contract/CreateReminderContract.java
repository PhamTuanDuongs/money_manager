package com.example.money_manager.contract;

import android.content.Context;

import com.example.money_manager.entity.Reminder;

public class CreateReminderContract {
    public interface View {

        void showDate(String date);
        void showTime(String time);
        void showError(String message);
        void createDefaultReminder();
        void createNewReminderSuccess();
        void createNewReminderError();
    }

    public interface Presenter {
        void onDateClicked();
        void onTimeClicked();
        void onClickCreateNewReminder();
        void defaultReminder(Context context);
        void createNewReminder(Context context, String name, String frequencey, String date, String time, String comment, String account);
    }

    public interface Model {
        void createNewReminderToDB(Reminder reminder, String notificationId, OnCreateNewReminderListener listener);

        interface OnCreateNewReminderListener {
            void onSuccess();
            void onError(String message);
        }
    }
}
