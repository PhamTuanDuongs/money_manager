package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.money_manager.contract.CreateReminderContract;
import com.example.money_manager.contract.presenter.CreateReminderPresenter;
import com.example.money_manager.utils.AccountState;
import com.example.money_manager.utils.DateTimeUtils;


public class CreateReminderFragment extends Fragment implements CreateReminderContract.View {
    private Spinner sp;
    private TextView txtDate, txtHour, txt_required, txt_next_to_Update;
    private EditText edt_Create, edt_Comment;
    private  CreateReminderPresenter presenter;
    private Button btn_create_reminder;
    private String frequencey = "";



    public CreateReminderFragment() {
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
        presenter = new CreateReminderPresenter(CreateReminderFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_reminder, container, false);
        txtDate = v.findViewById(R.id.txt_create_day);
        txtHour = v.findViewById(R.id.txt_create_hour);
        txtDate.setText(DateTimeUtils.getCurrentDate().toString());
        txtHour.setText(DateTimeUtils.getCurrentHour().toString());
        txt_required = v.findViewById(R.id.txt_required_update);
        edt_Create = v.findViewById(R.id.edt_creat_name);
        txt_next_to_Update = v.findViewById(R.id.nextToUpdate);
        edt_Comment = v.findViewById(R.id.edt_comment);
        btn_create_reminder = v.findViewById(R.id.btn_create_reminder);
        btn_create_reminder.setAlpha(0.5f);
        btn_create_reminder.setEnabled(false);
        String [] values = {"Once", "Every 1 Minute", "Daily","Weekly"};
        sp = (Spinner) v.findViewById(R.id.sp_create_reminder);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp.setAdapter(adapter);
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
        btn_create_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_Create.getText().toString();
                String date = txtDate.getText().toString();
                String time = txtHour.getText().toString();
                String comment = edt_Comment.getText().toString();
                String account = AccountState.getEmail(requireContext(), "email");
                presenter.createNewReminder(requireContext(), name,frequencey,date, time, comment, account);

                Fragment listReminderFragment = new ListReminderFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, listReminderFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        txt_next_to_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateReminderFragment updateFragment = new UpdateReminderFragment();
                Bundle args = new Bundle();
                args.putString("id", "-1330938635");
                updateFragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, updateFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void handleNameReminder() {
        edt_Create.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    txt_required.setText("Name required");
                    btn_create_reminder.setAlpha(0.5f);
                    btn_create_reminder.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) ){
                    txt_required.setText("");
                    btn_create_reminder.setAlpha(1.0f);
                    btn_create_reminder.setEnabled(true);
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
    public void createNewReminderSuccess() {
        Toast.makeText(requireContext(), "Create reminder success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createNewReminderError() {
        Toast.makeText(requireContext(), "Create reminder failed", Toast.LENGTH_SHORT).show();
    }
}