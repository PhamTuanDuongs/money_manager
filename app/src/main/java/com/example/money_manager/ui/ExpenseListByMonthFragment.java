package com.example.money_manager.ui;

import static com.example.money_manager.utils.DateTimeUtils.getCurrentMonth;
import static com.example.money_manager.utils.DateTimeUtils.getCurrentWeek;
import static com.example.money_manager.utils.DateTimeUtils.getDateMonthString;
import static com.example.money_manager.utils.DateTimeUtils.getNextMonth;
import static com.example.money_manager.utils.DateTimeUtils.getNextWeek;
import static com.example.money_manager.utils.DateTimeUtils.getPreviousMonth;
import static com.example.money_manager.utils.DateTimeUtils.getPreviousWeek;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.money_manager.R;
import com.example.money_manager.adapter.ExpenseAdapter;
import com.example.money_manager.contract.ExpenseContract;
import com.example.money_manager.activity.authentication.model.ExpenseModel;
import com.example.money_manager.contract.presenter.ExpensePresenter;
import com.example.money_manager.entity.Transaction;


import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ExpenseListByMonthFragment extends Fragment implements ExpenseContract.View {

    private ArrayList<Transaction> expenses = new ArrayList<>();
    private ExpenseContract.Presenter presenter;
    private RecyclerView expenseRecycleView;
    private ExpenseAdapter expenseAdapter;
    private ProgressBar pbLoading;
    private TextView txtDate, txtNoExpense;
    private Button btnPrevious;
    private Button btnNext;
    private int expensePosition;

    public static ExpenseListByMonthFragment newInstance(String param1, String param2) {
        ExpenseListByMonthFragment fragment = new ExpenseListByMonthFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate","onCreate");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expense_list_month, container, false);






        /*expenseAdapter.setListenerDelete(new ExpenseAdapter.ExpenseListClickListener() {
            @Override
            public void OnClick(View v, int position) {
                expensePosition = position;
                Transaction t = expenseAdapter.getItem(position);
                presenter.onDeleteButtonClick(t.getId());
            }
        });

        expenseAdapter.setListenerUpdate(new ExpenseAdapter.ExpenseListClickListener() {
            @Override
            public void OnClick(View v, int position) {
                expensePosition = position;

            }
        });*/

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ExpensePresenter(new ExpenseModel(), ExpenseListByMonthFragment.this);
        pbLoading = view.findViewById(R.id.progressBar);
        expenseRecycleView = view.findViewById(R.id.expense_recycle_view_month);
        expenseAdapter = new ExpenseAdapter(getContext(), expenses);
        expenseRecycleView.setAdapter(expenseAdapter);
        expenseRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        txtDate = view.findViewById(R.id.txtSelectedMonth);
        txtDate.setText(getCurrentMonth());
        btnNext =view.findViewById(R.id.btnNextMonth);
        btnPrevious =view.findViewById(R.id.btnPreviousMonth);
        txtNoExpense =view.findViewById(R.id.txtNoExpense);
        txtNoExpense.setText("");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseRecycleView.setVisibility(View.GONE);
                txtNoExpense.setVisibility(View.GONE);
                String w = txtDate.getText().toString();
                pbLoading.setVisibility(View.VISIBLE);
                txtDate.setText(getNextMonth(w));
                presenter.onGetListExpenseByMonth(getDateMonthString(getNextMonth(w)));

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseRecycleView.setVisibility(View.GONE);
                txtNoExpense.setVisibility(View.GONE);
                pbLoading.setVisibility(View.VISIBLE);
                String w = txtDate.getText().toString();
                txtDate.setText(getPreviousMonth(w));
                presenter.onGetListExpenseByMonth(getDateMonthString(getPreviousMonth(w)));

            }
        });

        presenter.onGetListExpenseByMonth(getDateMonthString(getCurrentMonth()));
    }

    @Override
    public void navigateToExpenseActivity() {

    }

    @Override
    public void showAddSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setListExpense(ArrayList<Transaction> transactions) {
        expenses.clear();

        for (Transaction trans : transactions
        ) {
            expenses.add(trans);
        }
        if(expenses.size()==0){
            txtNoExpense.setVisibility(View.VISIBLE);
            txtNoExpense.setText("No data");
        }else{
            txtNoExpense.setVisibility(View.GONE);
            expenseRecycleView.setVisibility(View.VISIBLE);
        }
        expenseAdapter.notifyDataSetChanged();
        pbLoading.setVisibility(View.GONE);

    }

    @Override
    public void DeleteExpense(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
        expenseAdapter.removeItem(expensePosition);
        expenseAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAddError(String title, String error) {

    }
}