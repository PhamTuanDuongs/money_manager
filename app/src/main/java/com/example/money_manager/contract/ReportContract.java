package com.example.money_manager.contract;
import com.example.money_manager.entity.CategorySum;

import java.util.ArrayList;
import java.util.Map;

public class ReportContract {
    public interface View {
        void setListExpenseSum(Map<Integer, Double> monthlyTotals);
        void setListIncomeSum(Map<Integer, Double> monthlyTotals);
        void setListCategorySumByIncome(ArrayList<CategorySum> cates);
        void setListCategorySumByExpense(ArrayList<CategorySum> cates);
        void onError(String message);

    }

    public interface Presenter {
        void onGetExpenseSumReport (int year, String email);
        void onGetIncomeSumReport (int year, String email);
        void onGetCategoryByIncomeReport(int year, String email);
        void onGetCategoryByExpenseReport(int year, String email);


    }

    public interface Model {
        void getTransactionSummary(int year, int type, String email, onTransactionListener listener);
        void getCategorySummary(int year,int type, String email, onTransactionListener listener);

        interface onTransactionListener {
            void onSuccess(Object object);

            void onError(String message);
        }
    }
}
