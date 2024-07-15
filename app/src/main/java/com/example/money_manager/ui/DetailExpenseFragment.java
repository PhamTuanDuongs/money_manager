package com.example.money_manager.ui;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.money_manager.R;
import com.example.money_manager.activity.authentication.model.ExpenseModel;
import com.example.money_manager.adapter.CategoryAdapter;
import com.example.money_manager.contract.ExpenseContract;
import com.example.money_manager.contract.presenter.ExpensePresenter;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.utils.AccountState;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DetailExpenseFragment extends Fragment {

    private Transaction transaction;
    private TextView tvName, tvCate, tvAmount, tvDate, tvDesc;
    private Button btnBack;



    public DetailExpenseFragment() {
        // Required empty public constructor
    }

    public DetailExpenseFragment(Transaction transaction) {
        this.transaction = transaction;
    }



    public static DetailExpenseFragment newInstance(String param1, String param2) {
        DetailExpenseFragment fragment = new DetailExpenseFragment();
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
        View v = inflater.inflate(R.layout.fragment_expense_detail, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvName= view.findViewById(R.id.txtExpenseTitleValue);
        tvDate=view.findViewById(R.id.tvExpenseDate);
        tvAmount=view.findViewById(R.id.txtExpenseAmountValue);
        tvDesc=view.findViewById(R.id.txtExpenseDescValue);
        tvCate=view.findViewById(R.id.txtExpenseCateValue);
        btnBack=view.findViewById(R.id.btnBack);
        tvName.setText(transaction.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(transaction.getCreateAt());
        tvDate.setText(formattedDate);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        String amount = df.format(transaction.getAmount());
        tvAmount.setText("- "+amount+ "  VND");
        tvDesc.setText(transaction.getDescription());
        tvCate.setText(transaction.getCategory().getName());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ExpenseListFragment());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getParentFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}