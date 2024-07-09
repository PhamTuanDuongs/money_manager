package com.example.money_manager.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.money_manager.R;
import com.example.money_manager.contract.IncomeContract;
import com.example.money_manager.contract.model.IncomeModel;
import com.example.money_manager.contract.presenter.IncomePresenter;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.utils.AccountState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddIncomeFragment extends Fragment implements IncomeContract.View {

    private Button btnDatePicker;
    private TextView tvDate;
    private String email;
    private TextView tvEmailTitle;
    private EditText edtAmount;
    private EditText edtDesc;

    private Button btnAdd;

    private Date choosenDate;
    private IncomeContract.Presenter presenter;

    public AddIncomeFragment() {
        // Required empty public constructor
    }


    public static AddIncomeFragment newInstance(String param1, String param2) {
        AddIncomeFragment fragment = new AddIncomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        presenter = new IncomePresenter(new IncomeModel(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_income, container, false);
        btnDatePicker = v.findViewById(R.id.btnDate);
        tvDate = v.findViewById(R.id.tvDate);
        tvEmailTitle = v.findViewById(R.id.tvEmailTitle);
        btnAdd = v.findViewById(R.id.btnAddIncome);
        edtAmount = v.findViewById(R.id.edtAmount);
        edtDesc = v.findViewById(R.id.edtDesc);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tvDate.setText(day + "-" + (month + 1) + "-" + year);
                                choosenDate = new Date(year, month,day);
                            }
                        }, year, month, day
                );
                dialog.show();
            }
        });
        email = AccountState.getEmail(getContext(), "email");
        tvEmailTitle.setText("Account: " + email);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.onAddButtonClick(getTransaction());
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showAddError(String title, String error) {

    }

    @Override
    public void navigateToIncomeActivity() {

    }

    @Override
    public void showAddSuccess(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setListIncome(ArrayList<Transaction> transactions) {

    }

    @Override
    public void DeleteIncome(String message) {

    }

    public Transaction getTransaction() {
        Transaction t = new Transaction();
        t.setAmount(Double.parseDouble(edtAmount.getText().toString()));
        t.setDescription(edtDesc.getText().toString());
        t.setCreateAt(choosenDate);
        t.setType(0);
        t.setName("New transaction");
        return t;
    }
}