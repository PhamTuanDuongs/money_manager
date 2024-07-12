package com.example.money_manager.contract.presenter;

import com.example.money_manager.contract.ListCategoryContract;
import com.example.money_manager.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryPresenter implements ListCategoryContract.Presenter, ListCategoryContract.Model.OnCategoriesGetListener {

    private ListCategoryContract.View view;
    private ListCategoryContract.Model model;
    public ListCategoryPresenter(ListCategoryContract.View view, ListCategoryContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void loadCategories() {
        model.getCategories(this);
    }

    @Override
    public void onCategoriesGet(List<Category> categories) {
        List<Category> expenseCategories = new ArrayList<>();
        List<Category> incomeCategories = new ArrayList<>();
        for (Category category : categories) {
            if (category.getType() == 1) {
                expenseCategories.add(category);
            } else if (category.getType() == 2) {
                incomeCategories.add(category);
            }
        }
        view.showExpenseCategories(expenseCategories);
        view.showIncomeCategories(incomeCategories);
    }

    @Override
    public void onError(Exception e) {

    }
}
