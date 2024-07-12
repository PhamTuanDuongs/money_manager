package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.money_manager.R;
import com.example.money_manager.contract.UpdateReminderContract;
import com.example.money_manager.contract.presenter.CreateReminderPresenter;
import com.example.money_manager.contract.presenter.UpdateReminderPresenter;
import com.example.money_manager.entity.Reminder;
import com.example.money_manager.utils.AccountState;
import com.example.money_manager.utils.DateTimeUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateReminderFragment extends Fragment implements UpdateReminderContract.View {
    private Spinner sp;
    private TextView txtDate, txtHour, txt_required;
    private EditText edt_Update, edt_Comment;
    private UpdateReminderPresenter presenter;
    private Button btn_update_reminder;
    private String frequencey = "";

    public UpdateReminderFragment() {
    }


    public static UpdateReminderFragment newInstance(String reminderId) {
        UpdateReminderFragment fragment = new UpdateReminderFragment();
        Bundle args = new Bundle();
        args.putString("reminderId", reminderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new UpdateReminderPresenter(UpdateReminderFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_reminder, container, false);
        txtDate = v.findViewById(R.id.txt_update_day);
        txtHour = v.findViewById(R.id.txt_update_hour);
        txtDate.setText(DateTimeUtils.getCurrentDate().toString());
        txtHour.setText(DateTimeUtils.getCurrentHour().toString());
        txt_required = v.findViewById(R.id.txt_required_update);
        edt_Update = v.findViewById(R.id.edt_update_name);
        edt_Comment = v.findViewById(R.id.edt_comment_update);
        btn_update_reminder = v.findViewById(R.id.btn_update_reminder);
        btn_update_reminder.setAlpha(0.5f);
        btn_update_reminder.setEnabled(false);
        String [] values = {"Once", "Every 1 Minute", "Daily","Weekly"};
        sp = (Spinner) v.findViewById(R.id.sp_update_reminder);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp.setAdapter(adapter);
        String value = getArguments().getString("id");
        presenter.getReminderById(Integer.parseInt(value));
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleNameReminder();
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDateClicked();
            }
        });
        txtHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onTimeClicked();
            }
        });
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                frequencey = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (parent.getCount() > 0) {
                    frequencey = parent.getItemAtPosition(0).toString();
                }
            }
        });

        btn_update_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_Update.getText().toString();
                String date = txtDate.getText().toString();
                String time = txtHour.getText().toString();
                String comment = edt_Comment.getText().toString();
                String account = AccountState.getEmail(requireContext(), "email");
                String value = getArguments().getString("id");
                presenter.onClickUpdateReminder(requireContext(), name,frequencey,date, time, comment, account, Integer.parseInt(value));
            }
        });
    }

    private void handleNameReminder() {
        edt_Update.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    txt_required.setText("Name required");
                    btn_update_reminder.setAlpha(0.5f);
                    btn_update_reminder.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) ){
                    txt_required.setText("");
                    btn_update_reminder.setAlpha(1.0f);
                    btn_update_reminder.setEnabled(true);
                }
            }
        });
    }
    @Override
    public void showDate(String date) {
        txtDate.setText(date);
    }

    @Override
    public void showTime(String time) {
        txtHour.setText(time);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), "Create reminder failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateReminderSuccess() {
        Toast.makeText(requireContext(), "Update reminder success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateReminderError() {
        Toast.makeText(requireContext(), "Update reminder failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fillExistData(Reminder reminder) {
        edt_Update.setText(reminder.getName());
        edt_Comment.setText(reminder.getComment());
        txtDate.setText(reminder.getDate());
        txtHour.setText(reminder.getTime());
        String frequency = reminder.getFrequency();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) sp.getAdapter();
        int spinnerPosition = adapter.getPosition(frequency);
        sp.setSelection(spinnerPosition);
    }
}