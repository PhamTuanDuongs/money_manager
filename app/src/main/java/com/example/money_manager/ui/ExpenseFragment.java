package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
public class ExpenseFragment extends Fragment implements ExpenseContract.View {

    private ArrayList<Transaction> expenses = new ArrayList<>();
    private ExpenseContract.Presenter presenter;
    private RecyclerView expenseRecycleView;
    private ExpenseAdapter expenseAdapter;
    private Button btnCreateExpense;
    private ProgressBar pbLoading;
    private int expensePosition;

    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
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
        View v = inflater.inflate(R.layout.fragment_list_expense, container, false);
        presenter = new ExpensePresenter(new ExpenseModel(), ExpenseFragment.this);
        pbLoading = v.findViewById(R.id.progressBar);
        btnCreateExpense=v.findViewById(R.id.btnCreateExpense);
        expenseRecycleView = v.findViewById(R.id.expense_recycle_view);
        expenseAdapter = new ExpenseAdapter(getContext(), expenses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        expenseRecycleView.setAdapter(expenseAdapter);
        expenseRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        expenseAdapter.setListenerDelete(new ExpenseAdapter.ExpenseListClickListener() {
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
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onGetListExpense();
//        setList();
        btnCreateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void setList() {
        Transaction trans1 = new Transaction();
        trans1.setDescription("hello duy");
        trans1.setName("111");
        trans1.setAmount(100);
        expenses.add(trans1);
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

        for (Transaction trans : transactions
        ) {
            expenses.add(trans);
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