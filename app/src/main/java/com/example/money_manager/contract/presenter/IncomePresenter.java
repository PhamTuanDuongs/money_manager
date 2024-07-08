package com.example.money_manager.contract.presenter;

import com.example.money_manager.adapter.IncomeAdapter;
import com.example.money_manager.contract.IncomeContract;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.ui.IncomeFragment;
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

    }

    @Override
    public void onDeleteButtonClick(int id) {

    }

    @Override
    public void onUpdateButtonClick(Transaction transaction, int id) {

    }

    @Override
    public void onGetListIncome() {
        String email = AccountState.getEmail(((IncomeFragment) view).getContext(),"email" );
        ArrayList<Transaction> trans = model.getTransactions(email, new IncomeContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                view.setListIncome((ArrayList<Transaction>) object);

            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
