package com.example.money_manager.contract;

import com.example.money_manager.entity.Transaction;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class IncomeContract {

    public interface View {
        void showAddError(String title,String error);
        void navigateToIncomeActivity();
        void setListIncome(ArrayList<Transaction> transactions);
    }

    public interface Presenter {
        void onAddButtonClick(Transaction transaction);

        void onDeleteButtonClick(int id);

        void onUpdateButtonClick(Transaction transaction, int id);

        void onGetListIncome();
    }

    public interface Model {
        ArrayList<Transaction> getTransactions(String email, onTransactionListener listener);

        void add(Transaction transaction);

        void delete(int id);

        void update(Transaction transaction, int id);

        interface onTransactionListener {
            void onSuccess(Object object);

            void onError(String message);
        }
    }
}
