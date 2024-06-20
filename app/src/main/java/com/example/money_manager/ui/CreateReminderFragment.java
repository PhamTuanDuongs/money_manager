package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.money_manager.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateReminderFragment extends Fragment {


    public CreateReminderFragment() {
        // Required empty public constructor
    }

    public static CreateReminderFragment newInstance(String param1, String param2) {
        CreateReminderFragment fragment = new CreateReminderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_reminder, container, false);
    }
}