package com.example.money_manager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.money_manager.R;
import com.example.money_manager.entity.Reminder;

import java.util.List;
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private List<Reminder> reminderList;
    private OnReminderClickListener listener;

    public ReminderAdapter(List<Reminder> reminderList, OnReminderClickListener listener) {
        this.reminderList = reminderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        holder.bind(reminder);
        holder.titleTextView.setText(reminder.getName());
        holder.descriptionTextView.setText(reminder.getComment());
        holder.idTextView.setText(reminder.getId());
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public void updateReminders(List<Reminder> newReminders) {
        reminderList.clear();
        reminderList.addAll(newReminders);
        notifyDataSetChanged();
    }

    public interface OnReminderClickListener {
        void onReminderClick(Reminder reminder);
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;
        TextView idTextView;
        private Reminder currentReminder;
        public ReminderViewHolder(@NonNull View itemView, OnReminderClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.reminder_title);
            descriptionTextView = itemView.findViewById(R.id.reminder_description);
            idTextView = itemView.findViewById(R.id.txtID);

            itemView.setOnClickListener(v -> {
                if (currentReminder != null) {
                    listener.onReminderClick(currentReminder);
                }
            });
        }

        public void bind(Reminder reminder) {
            currentReminder = reminder;
        }
    }
}
