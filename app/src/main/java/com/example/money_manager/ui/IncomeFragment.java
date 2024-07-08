package com.example.money_manager.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.money_manager.R;
import com.example.money_manager.adapter.IncomeAdapter;
import com.example.money_manager.contract.IncomeContract;
import com.example.money_manager.contract.model.IncomeModel;
import com.example.money_manager.contract.presenter.IncomePresenter;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.ui.placeholder.PlaceholderContent;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class IncomeFragment extends Fragment implements IncomeContract.View {

    private ArrayList<Transaction> incomes = new ArrayList<>();
    private IncomeContract.Presenter presenter;
    private RecyclerView incomeRecycleView;
    private IncomeAdapter incomeAdapter;
    private TextView tvLoading;

    public static IncomeFragment newInstance(String param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_list_income, container, false);
        tvLoading = v.findViewById(R.id.tvLoading);
        incomeRecycleView = v.findViewById(R.id.income_recycle_view);
        incomeAdapter = new IncomeAdapter(getContext(), incomes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        incomeRecycleView.setAdapter(incomeAdapter);
        incomeRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter = new IncomePresenter(new IncomeModel(), IncomeFragment.this);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onGetListIncome();
//        setList();

    }

    public void setList() {
        Transaction trans1 = new Transaction();
        trans1.setDescription("hello duy");
        trans1.setName("111");
        trans1.setAmount(100);
        incomes.add(trans1);
    }

    @Override
    public void navigateToIncomeActivity() {

    }

    @Override
    public void setListIncome(ArrayList<Transaction> transactions) {

        for (Transaction trans : transactions
        ) {
            incomes.add(trans);
        }
        incomeAdapter.notifyDataSetChanged();
        tvLoading.setText("");

    }

    @Override
    public void showAddError(String title, String error) {

    }
}