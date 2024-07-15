package com.example.money_manager.contract;

import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class ExpenseContract {

    public interface View {
        void showAddError(String title,String error);
        void updateExpense(Transaction transaction);
        void showAddSuccess(String message);
        void setListExpense(ArrayList<Transaction> transactions);
        void DeleteExpense(String message);
    }

    public interface Presenter {
        void onAddButtonClick(Transaction transaction);

        void onDeleteButtonClick(String autoID);

        void onUpdateButtonClick(Transaction transaction);

        void onGetListExpenseByWeek(String date);
        void onGetListExpenseByMonth(String date);
        void onGetListExpenseByYear(String date);
    }

    public interface Model {
        ArrayList<Transaction> getTransactions(String email, String date, onTransactionListener listener);
        void getCategoryListByEmailAndType(String email, int type, onTransactionListener listener);

        void add(Transaction transaction, String email, onTransactionListener listener);

        void delete(String autoID, onTransactionListener listener);
        double getAccountBalance(String email, ExpenseContract.Model.onTransactionListener listener);

        void update(Transaction transaction, onTransactionListener listener);

        interface onTransactionListener {
            void onSuccess(Object object);

            void onError(String message);
        }
    }
}
