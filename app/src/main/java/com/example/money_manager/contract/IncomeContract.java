package com.example.money_manager.contract;

import com.example.money_manager.entity.Transaction;

import java.util.ArrayList;

public class IncomeContract {

    public interface View {
        void showAddError(String title,String error);
        void navigateToIncomeActivity();
        void showAddSuccess(String message);
        void setListIncome(ArrayList<Transaction> transactions);

        void DeleteIncome(String message);

        void updateIncome(Transaction transaction);

        void updateIncomeOnSuccess(String message);


    }

    public interface Presenter {
        void onAddButtonClick(Transaction transaction);

        void onDeleteButtonClick(int id);

        void onUpdateButtonClick(Transaction transaction, int id);

        void onGetListIncome();

        void onLoadIncome(int id);

        void onGetListIncomeByWeek(String date);
        void onGetListIncomeByMonth(String date);
        void onGetListIncomeByYear(String date);
    }

    public interface Model {
        ArrayList<Transaction> getTransactions(String email,String date ,onTransactionListener listener);

        void add(Transaction transaction, String email, onTransactionListener listener);

        void delete(int id, onTransactionListener listener);

        void update(Transaction transaction, int id, onTransactionListener listener);

        void load(int id, onTransactionListener listener);

        interface onTransactionListener {
            void onSuccess(Object object);

            void onError(String message);
        }
    }
}
