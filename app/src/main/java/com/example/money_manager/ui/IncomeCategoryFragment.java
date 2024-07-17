package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.money_manager.R;
import com.example.money_manager.contract.ListCategoryContract;
import com.example.money_manager.contract.model.ListCategoryModel;
import com.example.money_manager.contract.presenter.ListCategoryPresenter;
import com.example.money_manager.entity.Category;

import java.util.List;

public class IncomeCategoryFragment extends Fragment implements ListCategoryContract.View {

    private RecyclerView rvIncomeCategories;
    private CategoryAdapter categoryAdapter;
    private ListCategoryContract.Presenter presenter;

    public IncomeCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_category, container, false);

        rvIncomeCategories = view.findViewById(R.id.rv_income_categories);

        presenter = new ListCategoryPresenter(new ListCategoryModel());
        presenter.attachView(this);

        categoryAdapter = new CategoryAdapter();
        rvIncomeCategories.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvIncomeCategories.setAdapter(categoryAdapter);

        presenter.loadIncomeCategories();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void showExpenseCategories(List<Category> expenseCategories) {
    }

    @Override
    public void showIncomeCategories(List<Category> incomeCategories) {
        categoryAdapter.setCategories(incomeCategories);
    }
}