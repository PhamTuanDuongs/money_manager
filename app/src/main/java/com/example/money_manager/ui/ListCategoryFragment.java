package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.money_manager.R;
import com.example.money_manager.contract.ListCategoryContract;

public class ListCategoryFragment extends Fragment{
    private Button btnExpenseTab;
    private Button btnIncomeTab;
    private Button btnAddCategory;

    public ListCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);

        btnExpenseTab = view.findViewById(R.id.btn_expense_tab);
        btnIncomeTab = view.findViewById(R.id.btn_income_tab);
        btnAddCategory = view.findViewById(R.id.btn_add_category);

        replaceFragment(new ExpenseCategoryFragment());

        btnExpenseTab.setOnClickListener(v -> {
            replaceFragment(new ExpenseCategoryFragment());
        });

        btnIncomeTab.setOnClickListener(v -> {
            replaceFragment(new IncomeCategoryFragment());
        });

        btnAddCategory.setOnClickListener(v -> {
            Fragment createReminderFragment = new CreateReminderFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_view, createReminderFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}