package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.money_manager.Models.Reminder;
import com.example.money_manager.R;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListReminderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListReminderFragment() {
        // Required empty public constructor
    }

    public static ListReminderFragment newInstance(String param1, String param2) {
        ListReminderFragment fragment = new ListReminderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_reminder, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Sample data for testing
        List<Reminder> reminders = new ArrayList<>();
        reminders.add(new Reminder("Meeting", "Discuss project updates"));
        reminders.add(new Reminder("Doctor Appointment", "Routine check-up"));
        reminders.add(new Reminder("Grocery Shopping", "Buy vegetables and fruits"));

        // Set adapter
        ReminderAdapter adapter = new ReminderAdapter(reminders);
        recyclerView.setAdapter(adapter);

        Button addReminderButton = view.findViewById(R.id.btnAddReminder);
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CreateReminderFragment
                Fragment createReminderFragment = new CreateReminderFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, createReminderFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}