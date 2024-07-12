package com.example.money_manager.contract.presenter;

import com.example.money_manager.contract.ListReminderContract;
import com.example.money_manager.contract.model.ListReminderModel;
import com.example.money_manager.entity.Reminder;

import java.util.List;

public class ListReminderPresenter implements ListReminderContract.Presenter, ListReminderContract.Model.OnFinishedListener {
    private ListReminderContract.View view;
    private ListReminderContract.Model model;

    public ListReminderPresenter(ListReminderContract.View view) {
        this.view = view;
        this.model = new ListReminderModel();
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
    public void onFinished(List<Reminder> reminders) {
        view.showReminders(reminders);
    }

    @Override
    public void onFailure(Exception e) {
        view.showError(e.getMessage());
    }
}
