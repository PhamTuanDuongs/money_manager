package com.example.money_manager.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.money_manager.R;
import com.example.money_manager.adapter.IncomeAdapter;
import com.example.money_manager.contract.IncomeContract;
import com.example.money_manager.contract.model.IncomeModel;
import com.example.money_manager.contract.presenter.IncomePresenter;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.ui.placeholder.PlaceholderContent;

import java.util.ArrayList;

import io.github.muddz.styleabletoast.StyleableToast;

/**
 * A fragment representing a list of Items.
 */
public class IncomeFragment extends Fragment implements IncomeContract.View {

    private ArrayList<Transaction> incomes = new ArrayList<>();
    private IncomeContract.Presenter presenter;
    private RecyclerView incomeRecycleView;
    private IncomeAdapter incomeAdapter;
    private TextView tvLoading;

    private int incomePosition;

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
        presenter = new IncomePresenter(new IncomeModel(), IncomeFragment.this);
        tvLoading = v.findViewById(R.id.tvLoading);
        incomeRecycleView = v.findViewById(R.id.income_recycle_view);
        incomeAdapter = new IncomeAdapter(getContext(), incomes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        incomeRecycleView.setAdapter(incomeAdapter);
        incomeRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        incomeAdapter.setListenerDelete(new IncomeAdapter.IncomeListClickListener() {
            @Override
            public void OnClick(View v, int position) {
                incomePosition = position;
                Transaction t = incomeAdapter.getItem(position);
                presenter.onDeleteButtonClick(t.getId());
            }
        });

        incomeAdapter.setListenerUpdate(new IncomeAdapter.IncomeListClickListener() {
            @Override
            public void OnClick(View v, int position) {
                incomePosition = position;
                IncomeDialogFragment fragment = new IncomeDialogFragment(incomeAdapter.getItem(position).getId());
                FragmentManager manager = getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

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
    public void showAddSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setListIncome(ArrayList<Transaction> transactions) {
        incomes.clear();
        for (Transaction trans : transactions
        ) {
            incomes.add(trans);
        }
        incomeAdapter.notifyDataSetChanged();
        tvLoading.setText("");

    }

    @Override
    public void DeleteIncome(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        incomeAdapter.removeItem(incomePosition);
        incomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateIncome(Transaction transaction) {

    }

    @Override
    public void updateIncomeOnSuccess(String message) {

    }

    @Override
    public void showAddError(String title, String error) {

    }
}