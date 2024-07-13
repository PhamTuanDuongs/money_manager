package com.example.money_manager.contract.presenter;
import android.util.Log;

import com.example.money_manager.contract.ExpenseContract;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.ui.AddExpenseFragment;
import com.example.money_manager.ui.ExpenseListByMonthFragment;
import com.example.money_manager.ui.ExpenseListByWeekFragment;
import com.example.money_manager.ui.ExpenseListByYearFragment;
import com.example.money_manager.utils.AccountState;

import java.util.ArrayList;

public class ExpensePresenter implements ExpenseContract.Presenter {

    private ExpenseContract.Model model;
    private ExpenseContract.View view;

    public ExpensePresenter(ExpenseContract.Model model, ExpenseContract.View view) {
        this.model = model;
        this.view = view;

    }

    @Override
    public void onAddButtonClick(Transaction transaction) {
        String email = AccountState.getEmail(((AddExpenseFragment) view).getContext(), "email");
        model.add(transaction, email, new ExpenseContract.Model.onTransactionListener() {
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
        model.delete(id, new ExpenseContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.DeleteExpense("Delete successfully");
            }

            @Override
            public void onError(String message) {
                view.showAddError("Delete income", message);
            }
        });
    }

    @Override
    public void onUpdateButtonClick(Transaction transaction, int id) {

    }




    @Override
    public void onGetListExpenseByWeek(String date) {
        String email = AccountState.getEmail(((ExpenseListByWeekFragment) view).getContext(), "email");
        ArrayList<Transaction> trans = model.getTransactions(email,date, new ExpenseContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.setListExpense((ArrayList<Transaction>) object);

            }

            @Override
            public void onError(String message) {
                view.showAddError("Error","Error");

            }
        });
    }

    @Override
    public void onGetListExpenseByMonth(String date) {
        String email = AccountState.getEmail(((ExpenseListByMonthFragment) view).getContext(), "email");
        ArrayList<Transaction> trans = model.getTransactions(email,date, new ExpenseContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.setListExpense((ArrayList<Transaction>) object);

            }

            @Override
            public void onError(String message) {
                view.showAddError("Error","Error");

            }
        });
    }

    @Override
    public void onGetListExpenseByYear(String date) {
        String email = AccountState.getEmail(((ExpenseListByYearFragment) view).getContext(), "email");
        ArrayList<Transaction> trans = model.getTransactions(email,date, new ExpenseContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.setListExpense((ArrayList<Transaction>) object);

            }

            @Override
            public void onError(String message) {
                view.showAddError("Error","Error");

            }
        });
    }
}
