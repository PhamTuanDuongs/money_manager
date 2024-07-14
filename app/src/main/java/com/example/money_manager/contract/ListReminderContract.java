package com.example.money_manager.contract;

import android.content.Context;

import com.example.money_manager.entity.Reminder;

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
    }

    public interface Model {
        void getReminders(OnFinishedListener listener);

        interface OnFinishedListener {
            void onFinished(List<Reminder> reminders);
            void onFailure(Exception e);
        }
    }
}
