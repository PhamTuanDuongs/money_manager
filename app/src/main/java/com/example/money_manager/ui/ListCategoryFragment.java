package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.money_manager.R;
import com.example.money_manager.contract.ListCategoryContract;
import com.example.money_manager.contract.model.ListCategoryModel;
import com.example.money_manager.contract.presenter.ListCategoryPresenter;
import com.example.money_manager.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryFragment extends Fragment implements ListCategoryContract.View {
    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;
    private ListCategoryContract.Presenter presenter;
    private Button btnExpenseTab;
    private Button btnIncomeTab;
    private Button btnAddCategory;
    private boolean isExpenseTabSelected = true;
    public ListCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
        rvCategories = view.findViewById(R.id.rv_categories);
        btnExpenseTab = view.findViewById(R.id.btn_expense_tab);
        btnIncomeTab = view.findViewById(R.id.btn_income_tab);
        btnAddCategory = view.findViewById(R.id.btn_add_category);

        presenter = new ListCategoryPresenter(this, new ListCategoryModel());
        categoryAdapter = new CategoryAdapter();

        rvCategories.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvCategories.setAdapter(categoryAdapter);

        btnExpenseTab.setOnClickListener(v -> {
            isExpenseTabSelected = true;
            presenter.loadCategories();
        });

        btnIncomeTab.setOnClickListener(v -> {
            isExpenseTabSelected = false;
            presenter.loadCategories();
        });

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment createReminderFragment = new CreateReminderFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, createReminderFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        presenter.loadCategories();

        return view;
    }

    @Override
    public void showExpenseCategories(List<Category> expenseCategories) {
        if (isExpenseTabSelected) {
            categoryAdapter.setCategories(expenseCategories);
        }
    }

    @Override
    public void showIncomeCategories(List<Category> incomeCategories) {
        if (!isExpenseTabSelected) {
            categoryAdapter.setCategories(incomeCategories);
        }
    }
}