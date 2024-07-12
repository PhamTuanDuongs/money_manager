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
import com.example.money_manager.contract.ExpenseContract;
import com.example.money_manager.activity.authentication.model.ExpenseModel;
import com.example.money_manager.contract.presenter.ExpensePresenter;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.utils.AccountState;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddExpenseFragment extends Fragment implements ExpenseContract.View {

    private Button btnDatePicker;
    private TextView tvDate;
    private String balance;
    private TextView tvBalance;
    private EditText edtAmount;
    private EditText edtDesc;

    private Button btnAdd;

    private Timestamp choosenDate;
    private ExpenseContract.Presenter presenter;

    public AddExpenseFragment() {
        // Required empty public constructor
    }


    public static AddExpenseFragment newInstance(String param1, String param2) {
        AddExpenseFragment fragment = new AddExpenseFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        presenter = new ExpensePresenter(new ExpenseModel(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_expense, container, false);
        btnDatePicker = v.findViewById(R.id.btnSelectDate);
        tvDate = v.findViewById(R.id.tvExpenseDate);
        tvBalance = v.findViewById(R.id.txtBalance);
        btnAdd = v.findViewById(R.id.btnAddExpense);
        edtAmount = v.findViewById(R.id.edtExpenseValue);
        edtDesc = v.findViewById(R.id.ediExpenseDesc);
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
//                                choosenDate = new Date(year, month,day);
                            }
                        }, year, month, day
                );
                dialog.show();
            }
        });
        balance = AccountState.getAccountBalance(getContext(), "balance")+ " VND";
        tvBalance.setText("Balance: " + balance);
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
    public void navigateToExpenseActivity() {

    }

    @Override
    public void showAddSuccess(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setListExpense(ArrayList<Transaction> transactions) {

    }

    @Override
    public void DeleteExpense(String message) {

    }

    public Transaction getTransaction() {
        Transaction t = new Transaction();
        t.setAmount(Double.parseDouble(edtAmount.getText().toString()));
        t.setDescription(edtDesc.getText().toString());
        t.setCreateAt(choosenDate);
        t.setType(1);
        t.setName("New transaction");
        return t;
    }
}