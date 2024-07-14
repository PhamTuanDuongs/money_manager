package com.example.money_manager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
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
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        holder.titleTextView.setText(reminder.getName());
        holder.descriptionTextView.setText(reminder.getComment());
        holder.idTextView.setText(reminder.getId());
        holder.swActive.setChecked(reminder.isActive());
        holder.itemView.setOnClickListener(v -> listener.onReminderClick(reminder));
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public void updateReminders(List<Reminder> reminderList) {
        this.reminderList = reminderList;
        notifyDataSetChanged();
    }

    public interface OnReminderClickListener {
        void onReminderClick(Reminder reminder);
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;
        TextView idTextView;
        Switch swActive;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.reminder_title);
            descriptionTextView = itemView.findViewById(R.id.reminder_description);
            idTextView = itemView.findViewById(R.id.txtID);
            swActive = itemView.findViewById(R.id.swActive);
        }
    }

}
