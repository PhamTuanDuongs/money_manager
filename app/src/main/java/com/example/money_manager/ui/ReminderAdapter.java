package com.example.money_manager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.money_manager.Models.Reminder;
import com.example.money_manager.R;

import java.util.List;
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private List<Reminder> reminderList;

    public ReminderAdapter(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        holder.titleTextView.setText(reminder.getTitle());
        holder.descriptionTextView.setText(reminder.getDescription());
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.reminder_title);
            descriptionTextView = itemView.findViewById(R.id.reminder_description);
        }
    }
}