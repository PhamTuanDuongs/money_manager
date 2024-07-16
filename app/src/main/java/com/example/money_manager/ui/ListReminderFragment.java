package com.example.money_manager.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.money_manager.R;
import com.example.money_manager.contract.ListReminderContract;
import com.example.money_manager.contract.presenter.ListReminderPresenter;
import com.example.money_manager.entity.Reminder;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListReminderFragment extends Fragment implements ListReminderContract.View, ReminderAdapter.OnReminderClickListener{
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private ListReminderPresenter presenter;
    public ListReminderFragment() {
        // Required empty public constructor
    }

    public static ListReminderFragment newInstance() {
        ListReminderFragment fragment = new ListReminderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ListReminderPresenter(this, requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_reminder, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Reminder> reminders = new ArrayList<>();
        adapter = new ReminderAdapter(reminders, this);
        recyclerView.setAdapter(adapter);
        presenter.loadReminders();

        Button addReminderButton = view.findViewById(R.id.btnAddReminder);
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment createReminderFragment = new CreateReminderFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, createReminderFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void showReminders(List<Reminder> reminders) {
        adapter.updateReminders(reminders);
    }

    @Override
    public void showError(String message) {
        Log.d("Firestore", "Error getting reminders: " + message);
    }

    @Override
    public void navigateToUpdateReminder(Reminder reminder) {
        Fragment updateFragment = UpdateReminderFragment.newInstance(reminder.getId());
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main, updateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onReminderClick(Reminder reminder) {
        navigateToUpdateReminder(reminder);
    }
}