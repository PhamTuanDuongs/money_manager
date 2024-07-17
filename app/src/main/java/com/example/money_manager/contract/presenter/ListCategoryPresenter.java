package com.example.money_manager.contract.presenter;

import com.example.money_manager.contract.ListCategoryContract;
import com.example.money_manager.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryPresenter implements ListCategoryContract.Presenter, ListCategoryContract.Model.OnCategoriesGetListener {

    private ListCategoryContract.View view;
    private ListCategoryContract.Model model;

    public ListCategoryPresenter(ListCategoryContract.Model model) {
        this.model = model;
    }

    @Override
        public void attachView(ListCategoryContract.View view) {
            this.view = view;
        }

    @Override
    public void detachView() {
                this.view = null;
            }

    @Override
    public void loadExpenseCategories() {
                model.getCategories(this);
            }

    @Override
    public void loadIncomeCategories() {
                model.getCategories(this);
            }

    @Override
    public void onCategoryClicked(Category category) {
        if (view != null) {
            view.navigateToUpdateCategory(category);
        }
    }

    @Override
    public void onCategoriesGet(List<Category> categories) {

        if (view != null) {
            List<Category> expenseCategories = filterCategoriesByType(categories, 1);
            List<Category> incomeCategories = filterCategoriesByType(categories, 0);

            view.showExpenseCategories(expenseCategories);
            view.showIncomeCategories(incomeCategories);
        }
    }

    @Override
    public void onError(Exception e) {

    }

    private List<Category> filterCategoriesByType(List<Category> categories, int type) {
        List<Category> filteredCategories = new ArrayList<>();
        for (Category category : categories) {
            if (category.getType() == type) {
                filteredCategories.add(category);
            }
        }
        return filteredCategories;
    }
}