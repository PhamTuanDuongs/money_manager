package com.example.money_manager.contract;

import android.content.Context;

import com.example.money_manager.entity.Reminder;

import java.util.Calendar;

public class UpdateReminderContract {
    public interface View {
        void showDate(String date);

        void showTime(String time);

        void showError(String message);

        void updateReminderSuccess();

        void updateReminderError();

        void fillExistData(Reminder reminder);
    }

    public interface Presenter {
        void onDateClicked();

        void onTimeClicked();

        void getReminderById(int id);

        void onClickUpdateReminder(Context context, String title, String frequencey, String strDate, String strTime, String comment, String account, int notificationId);
    }

    public interface Model {

        void getReminderById(int id, FireStoreReminderCallBack fireStoreCallBack);

        void updateReminderToDB(Reminder reminder, String notificationId, CreateReminderContract.Model.OnCreateNewReminderListener listener);

        void updateScheduleOneTimeNotification(Context context, int notificationId, String title, String message, Reminder reminder, Calendar calendar);

        void updateScheduleEvery1MinuteNotification(Context context, int notificationId, String title,
                                                    String message, Reminder reminder, Calendar calendar);

        void updateScheduleDailyNotification(Context context, int notificationId, String title,
                                             String message, Reminder reminder, Calendar calendar);

        void updateScheduleWeeklyNotification(Context context, int notificationId, String title,
                                              String message, Reminder reminder, Calendar calendar);

        interface OnUpdateNewReminderListener {
            void onSuccess();

            void onError(String message);
        }

        interface FireStoreReminderCallBack {
            void onCallBack(Reminder reminder);
        }

    }
}
