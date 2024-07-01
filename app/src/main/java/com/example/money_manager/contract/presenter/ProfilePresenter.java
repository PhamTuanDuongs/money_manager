package com.example.money_manager.contract.presenter;

import androidx.annotation.NonNull;

import com.example.money_manager.contract.ProfileContract;
import com.example.money_manager.contract.model.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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
