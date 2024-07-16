package com.example.money_manager.contract;

import com.example.money_manager.entity.Category;
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

        void setBalance(String balance);

        void setCategory(ArrayList<Category> categories);


    }

    public interface Presenter {
        void onAddButtonClick(Transaction transaction);

        void onDeleteButtonClick(String id);

        void onUpdateButtonClick(Transaction transaction, String id);

        void onGetListIncome();

        void onLoadIncome(String id);

        void onGetListIncomeByWeek(String date);
        void onGetListIncomeByMonth(String date);
        void onGetListIncomeByYear(String date);

        void onGetBalance(String email);

        void onGetCategoryListByEmailAndType(String email, int type);
    }

    public interface Model {
        ArrayList<Transaction> getTransactions(String email,String date ,onTransactionListener listener);

        void add(Transaction transaction, String email, onTransactionListener listener);

        void delete(String id, onTransactionListener listener);

        void update(Transaction transaction, String id, onTransactionListener listener);

        void load(String id, onTransactionListener listener);

        double getAccountBalance(String email, onTransactionListener listener);

        void getCategoryListByEmailAndType(String email, int type, onTransactionListener listener);


        interface onTransactionListener {
            void onSuccess(Object object);

            void onError(String message);
        }
    }
}
