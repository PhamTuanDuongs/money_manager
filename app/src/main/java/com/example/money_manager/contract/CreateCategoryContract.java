package com.example.money_manager.contract;

import com.example.money_manager.entity.Category;

public class CreateCategoryContract {
    public interface View {
        void createCategorySuccess();

        void createCategoryError();
    }

    public interface Presenter {
        void createCategory(String name, int type, String account);
    }

    public interface Model {
        void createCategory(Category category, OnCreateCategoryListener listener);

        interface OnCreateCategoryListener {
            void onSuccess();

            void onError(String message);
        }
    }
}
