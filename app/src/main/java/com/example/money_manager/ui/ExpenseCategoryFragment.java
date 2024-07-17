package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.money_manager.R;
import com.example.money_manager.contract.ListCategoryContract;
import com.example.money_manager.contract.model.ListCategoryModel;
import com.example.money_manager.contract.presenter.ListCategoryPresenter;
import com.example.money_manager.entity.Category;

import java.util.List;

public class ExpenseCategoryFragment extends Fragment implements ListCategoryContract.View {
    private RecyclerView rvExpenseCategories;
    private CategoryAdapter categoryAdapter;
    private ListCategoryContract.Presenter presenter;

    public ExpenseCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_category, container, false);

        rvExpenseCategories = view.findViewById(R.id.rv_expense_categories);

        presenter = new ListCategoryPresenter(new ListCategoryModel());
        presenter.attachView(this);

        categoryAdapter = new CategoryAdapter();
        categoryAdapter.setOnCategoryClickListener(category -> presenter.onCategoryClicked(category));
        rvExpenseCategories.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvExpenseCategories.setAdapter(categoryAdapter);

        presenter.loadExpenseCategories();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void showExpenseCategories(List<Category> expenseCategories) {
        categoryAdapter.setCategories(expenseCategories);
    }

    @Override
    public void showIncomeCategories(List<Category> incomeCategories) {
    }

    @Override
    public void navigateToUpdateCategory(Category category) {
//        Fragment updateCategoryFragment = UpdateCategoryFragment.newInstance(category.getId());
//        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//        transaction.replace(R.id.nav_host_fragment_content_main, updateCategoryFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
        Log.d("Category", "clicked! " + category.getId());
    }
}