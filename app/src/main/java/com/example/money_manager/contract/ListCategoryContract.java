package com.example.money_manager.contract;

import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Reminder;

import java.util.List;

public class ListCategoryContract {
    public interface View {
        void showExpenseCategories(List<Category> expenseCategories);
        void showIncomeCategories(List<Category> incomeCategories);
        void navigateToUpdateCategory(Category category);
    }

    public interface Presenter {
        void attachView(View view);

        void detachView();

        void loadExpenseCategories();

        void loadIncomeCategories();
        void onCategoryClicked(Category category);
    }

    public interface Model {
        void getCategories(OnCategoriesGetListener listener);

        interface OnCategoriesGetListener {
            void onCategoriesGet(List<Category> categories);

            void onError(Exception e);
        }
    }
}