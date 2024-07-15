package com.example.money_manager.ui;

import static com.example.money_manager.utils.DateTimeUtils.getCurrentMonth;

import static com.example.money_manager.utils.DateTimeUtils.getDateYearString;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;


import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ExpenseListByYearFragment extends Fragment implements ExpenseContract.View {

    private ArrayList<Transaction> expenses = new ArrayList<>();
    private ExpenseContract.Presenter presenter;
    private RecyclerView expenseRecycleView;
    private ExpenseAdapter expenseAdapter;
    private ProgressBar pbLoading;
    private TextView txtDate, txtNoExpense;
    private Button btnPrevious;
    private Button btnNext;
    private int expensePosition;

    public static ExpenseListByYearFragment newInstance(String param1, String param2) {
        ExpenseListByYearFragment fragment = new ExpenseListByYearFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expense_list_year, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ExpensePresenter(new ExpenseModel(), ExpenseListByYearFragment.this);
        pbLoading = view.findViewById(R.id.progressBar);
        expenseRecycleView = view.findViewById(R.id.expense_recycle_view_year);
        expenseAdapter = new ExpenseAdapter(getContext(), expenses);
        expenseRecycleView.setAdapter(expenseAdapter);
        expenseRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        txtDate = view.findViewById(R.id.txtSelectedYear);
        txtDate.setText(getCurrentMonth().substring(4,8));
        btnNext =view.findViewById(R.id.btnNextYear);
        btnPrevious =view.findViewById(R.id.btnPreviousYear);
        txtNoExpense =view.findViewById(R.id.txtNoExpense);
        txtNoExpense.setText("");
        expenseAdapter.setListener(new ExpenseAdapter.ExpenseListClickListener() {
            @Override
            public void OnDelete(View v, int position) {
                expensePosition = position;
                Transaction t = expenseAdapter.getItem(position);
                presenter.onDeleteButtonClick(t.getAutoID());

            }

            @Override
            public void OnUpdate(View v, Transaction transaction) {
                loadFragment(new UpdateExpenseFragment(transaction));
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseRecycleView.setVisibility(View.GONE);
                txtNoExpense.setVisibility(View.GONE);
                String w = txtDate.getText().toString();
                pbLoading.setVisibility(View.VISIBLE);
                txtDate.setText(Integer.parseInt(w)+1+"");
                presenter.onGetListExpenseByYear(getDateYearString(Integer.parseInt(w)+1+""));

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseRecycleView.setVisibility(View.GONE);
                txtNoExpense.setVisibility(View.GONE);
                pbLoading.setVisibility(View.VISIBLE);
                String w = txtDate.getText().toString();
                txtDate.setText(Integer.parseInt(w)-1+"");
                presenter.onGetListExpenseByYear(getDateYearString(Integer.parseInt(w)-1+""));

            }
        });
        presenter.onGetListExpenseByYear(getDateYearString(getCurrentMonth().substring(4,8)));
    }



    @Override
    public void showAddSuccess(String message) {
        new AlertDialog.Builder(getContext())

                .setMessage(message)
                .setIcon(R.drawable.check)
                .setPositiveButton("OK", null)
                .show();
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
        new AlertDialog.Builder(getContext())

                .setMessage(message)
                .setIcon(R.drawable.error)
                .setPositiveButton("OK", null)
                .show();

        expenseAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAddError(String title, String error) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(error)
                .setIcon(R.drawable.error)
                .setPositiveButton("OK", null)
                .show();

    }

    @Override
    public void updateExpense(Transaction transaction) {

    }
    private void loadFragment(Fragment fragment) {

        FragmentManager manager = getParentFragment().getParentFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}