package com.example.money_manager.contract.presenter;

import com.example.money_manager.contract.ProfileContract;
import com.example.money_manager.activity.authentication.model.ProfileModel;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.Model model;
    private ProfileContract.View view;

    public ProfilePresenter(ProfileContract.View view){
        this.view = view;
        model = new ProfileModel();
    }

     @Override
    public void onClickChangePassword(String email) {
            model.changePassword(new ProfileContract.OnChangePasswordFinishedListener() {
                @Override
                public void onSuccess() {
                    view.showNotifyResetPassword();
                }

                @Override
                public void onError(String message) {
                    view.showErrorNotifyResetPassword(message);
                }
            }, email);
    }
}
