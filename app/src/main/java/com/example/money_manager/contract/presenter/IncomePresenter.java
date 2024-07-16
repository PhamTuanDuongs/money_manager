package com.example.money_manager.contract.presenter;

import android.util.Log;

import com.example.money_manager.adapter.IncomeAdapter;
import com.example.money_manager.contract.IncomeContract;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.ui.AddIncomeFragment;
import com.example.money_manager.ui.IncomeFragment;
import com.example.money_manager.ui.IncomeListByMonthFragment;
import com.example.money_manager.ui.IncomeListByWeekFragment;
import com.example.money_manager.ui.IncomeListByYearFragment;
import com.example.money_manager.utils.AccountState;

import java.util.ArrayList;

public class IncomePresenter implements IncomeContract.Presenter {

    private IncomeContract.Model model;
    private IncomeContract.View view;

    public IncomePresenter(IncomeContract.Model model, IncomeContract.View view) {
        this.model = model;
        this.view = view;

    }

    @Override
    public void onAddButtonClick(Transaction transaction) {
        String email = AccountState.getEmail(((AddIncomeFragment) view).getContext(), "email");
        model.add(transaction, email, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.showAddSuccess("Add successfully");
            }

            @Override
            public void onError(String message) {
                view.showAddError("Error Add: ", message);

            }
        });
    }

    @Override
    public void onDeleteButtonClick(int id) {
        Log.d("TEST_DELETE", "Onclick Delete");
        model.delete(id, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.DeleteIncome("Delete successfully");
            }

            @Override
            public void onError(String message) {
                view.showAddError("Delete income", message);
            }
        });
    }

    @Override
    public void onUpdateButtonClick(Transaction transaction, int id) {
        model.update(transaction, id, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.updateIncomeOnSuccess("Update income successfully");
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public void onGetListIncome() {
        String email = AccountState.getEmail(((IncomeFragment) view).getContext(), "email");
        ArrayList<Transaction> trans = model.getTransactions(email, "", new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.setListIncome((ArrayList<Transaction>) object);

            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public void onLoadIncome(int id) {
        model.load(id, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                Transaction transaction = (Transaction) object;
                view.updateIncome(transaction);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public void onGetListIncomeByWeek(String date) {
        String email = AccountState.getEmail(((IncomeListByWeekFragment) view).getContext(), "email");
        ArrayList<Transaction> trans = model.getTransactions(email, date, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.setListIncome((ArrayList<Transaction>) object);
            }

            @Override
            public void onError(String message) {
                view.showAddError("Error", "Error");
            }
        });
    }

    @Override
    public void onGetListIncomeByMonth(String date) {
        String email = AccountState.getEmail(((IncomeListByMonthFragment) view).getContext(), "email");
        ArrayList<Transaction> trans = model.getTransactions(email, date, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.setListIncome((ArrayList<Transaction>) object);

            }

            @Override
            public void onError(String message) {
                view.showAddError("Error", "Error");

            }
        });
    }

    @Override
    public void onGetListIncomeByYear(String date) {
        String email = AccountState.getEmail(((IncomeListByYearFragment) view).getContext(), "email");
        ArrayList<Transaction> trans = model.getTransactions(email, date, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.setListIncome((ArrayList<Transaction>) object);

            }

            @Override
            public void onError(String message) {
                view.showAddError("Error", "Error");

            }
        });
    }

    @Override
    public void onGetBalance(String email) {
        model.getAccountBalance(email, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                double sum = 0;
                ArrayList<Double> accountBalance = (ArrayList<Double>) object;
                for (double number : accountBalance
                ) {
                    sum += number;
                }
                view.setBalance("Balance: " + sum + " " + "VND");

            }

            @Override
            public void onError(String message) {
                view.setBalance("Balance: 0.0 VND");

            }
        });
    }

    @Override
    public void onGetCategoryListByEmailAndType(String email, int type) {
        model.getCategoryListByEmailAndType(email, type, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.setCategory((ArrayList<Category>) object);

            }

            @Override
            public void onError(String message) {
                view.showAddError("Error", "Load category error");
            }
        });
    }
}
