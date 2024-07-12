package com.example.money_manager.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.money_manager.R;
import com.example.money_manager.contract.IncomeContract;
import com.example.money_manager.contract.model.IncomeModel;
import com.example.money_manager.contract.presenter.IncomePresenter;
import com.example.money_manager.entity.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IncomeDialogFragment extends Fragment implements IncomeContract.View {
    private Button btnDatePicker;
    private TextView tvDate;
    private String email;
    private TextView tvEmailTitle;
    private EditText edtAmount;
    private EditText edtDesc;

    private Button btnUpdate;
    private int incomeId;
    private IncomeContract.Presenter presenter;

    private Date choosenDate;


    public IncomeDialogFragment(int incomeId) {
        this.incomeId = incomeId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new IncomePresenter(new IncomeModel(), this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "Id: " + incomeId, Toast.LENGTH_SHORT).show();
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_update_income, container, false);
        btnDatePicker = v.findViewById(R.id.btnDate);
        tvDate = v.findViewById(R.id.tvDate);
        btnUpdate = v.findViewById(R.id.btnUpdateIncome);
        edtAmount = v.findViewById(R.id.edtAmount);
        edtDesc = v.findViewById(R.id.edtDesc);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onLoadIncome(incomeId);
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
                                choosenDate = new Date(year, month, day);
                                Toast.makeText(getContext(), choosenDate.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day
                );
                dialog.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onUpdateButtonClick(getTransaction(), incomeId);
            }
        });
    }

    @Override
    public void showAddError(String title, String error) {

    }

    @Override
    public void navigateToIncomeActivity() {

    }

    @Override
    public void showAddSuccess(String message) {

    }

    @Override
    public void setListIncome(ArrayList<Transaction> transactions) {

    }

    @Override
    public void DeleteIncome(String message) {

    }

    @Override
    public void updateIncome(Transaction transaction) {
        edtAmount.setText(transaction.getAmount() + "");
        edtDesc.setText(transaction.getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(transaction.getCreateAt().toDate());
        tvDate.setText(formattedDate);

    }

    @Override
    public void updateIncomeOnSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public Transaction getTransaction() {
        Transaction t = new Transaction();
        t.setAmount(Double.parseDouble(edtAmount.getText().toString()));
        t.setDescription(edtDesc.getText().toString());
        t.setType(0);
        t.setName("New transaction");
        return t;
    }
}
