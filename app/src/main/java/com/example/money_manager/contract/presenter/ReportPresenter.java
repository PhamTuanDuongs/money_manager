package com.example.money_manager.contract.presenter;

import com.example.money_manager.contract.ReportContract;
import com.example.money_manager.entity.CategorySum;

import java.util.ArrayList;
import java.util.Map;

public class ReportPresenter implements ReportContract.Presenter {

    private ReportContract.Model model;
    private ReportContract.View view;
    private int INCOME_TYPE =0;
    private int EXPENSE_TYPE =1;
    public ReportPresenter(ReportContract.Model model, ReportContract.View view) {
        this.model = model;
        this.view = view;

    }

    @Override
    public void onGetExpenseSumReport(int year, String email) {

        model.getTransactionSummary(year, EXPENSE_TYPE, email, new ReportContract.Model.onTransactionListener() {

            @Override
            public void onSuccess(Object object) {
                view.setListExpenseSum((Map<Integer, Double>) object);

            }

            @Override
            public void onError(String message) {
                view.onError(message);


            }
        });
    }

    @Override
    public void onGetIncomeSumReport(int year, String email) {
        model.getTransactionSummary(year, INCOME_TYPE, email, new ReportContract.Model.onTransactionListener() {

            @Override
            public void onSuccess(Object object) {
                view.setListIncomeSum((Map<Integer, Double>) object);

            }

            @Override
            public void onError(String message) {
                view.onError(message);


            }
        });

    }

    @Override
    public void onGetCategoryByIncomeReport(int year, String email) {
        model.getCategorySummary(year,INCOME_TYPE, email, new ReportContract.Model.onTransactionListener() {

            @Override
            public void onSuccess(Object object) {
                view.setListCategorySumByIncome((ArrayList<CategorySum>) object);

            }

            @Override
            public void onError(String message) {
                view.onError(message);


            }
        });

    }

    @Override
    public void onGetCategoryByExpenseReport(int year, String email) {
        model.getCategorySummary(year,EXPENSE_TYPE, email, new ReportContract.Model.onTransactionListener() {

            @Override
            public void onSuccess(Object object) {
                view.setListCategorySumByExpense((ArrayList<CategorySum>) object);

            }

            @Override
            public void onError(String message) {
                view.onError(message);


            }
        });

    }



}
