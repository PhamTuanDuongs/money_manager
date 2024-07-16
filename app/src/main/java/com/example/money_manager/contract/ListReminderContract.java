package com.example.money_manager.contract;

import android.content.Context;

import com.example.money_manager.entity.Reminder;

import java.util.Calendar;
import java.util.List;

public class ListReminderContract {
    public interface View {
        void showReminders(List<Reminder> reminders);
        void showError(String message);
        void navigateToUpdateReminder(Reminder reminder);
    }

    public interface Presenter {
        void loadReminders();
        void onReminderClicked(Reminder reminder);
        void createNewNotification(Context context, String name, String frequencey, String date, String time, String comment);
    }

    public interface Model {
        void getReminders(OnFinishedListener listener);
        void scheduleNotification(Context context, int notificationId, String title, String message, Reminder reminder, Calendar calendar, int repeatType);

        interface OnFinishedListener {
            void onFinished(List<Reminder> reminders);
            void onFailure(Exception e);
        }
    }
}
