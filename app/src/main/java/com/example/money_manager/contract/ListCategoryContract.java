package com.example.money_manager.contract;

import com.example.money_manager.entity.Category;

import java.util.List;

public class ListCategoryContract {
    public interface View{
        void showExpenseCategories(List<Category> expenseCategories);
        void showIncomeCategories(List<Category> incomeCategories);
    }

    public interface Presenter{
        void loadCategories();
    }

    public interface Model{
        void getCategories(OnCategoriesGetListener listener);

        interface OnCategoriesGetListener {
            void onCategoriesGet(List<Category> categories);
            void onError(Exception e);
        }
    }
}
