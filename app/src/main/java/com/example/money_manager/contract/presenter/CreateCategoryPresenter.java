package com.example.money_manager.contract.presenter;

import com.example.money_manager.contract.CreateCategoryContract;
import com.example.money_manager.contract.model.CreateCategoryModel;
import com.example.money_manager.entity.Category;

public class CreateCategoryPresenter implements CreateCategoryContract.Presenter {
    private final CreateCategoryContract.View view;
    private final CreateCategoryModel model;

    public CreateCategoryPresenter(CreateCategoryContract.View view) {
        this.view = view;
        model = new CreateCategoryModel();
    }

    @Override
    public void createCategory(String name, int type, String email) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Category category = new Category(name, type, email);
            model.createCategory(category, new CreateCategoryContract.Model.OnCreateCategoryListener() {
                @Override
                public void onSuccess() {
                    view.createCategorySuccess();
                }

                @Override
                public void onError(String message) {
                    view.createCategoryError();
                }
            });
        }
    }
}
