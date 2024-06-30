package com.example.money_manager.contract.presenter;

import com.example.money_manager.contract.LoginContract;
import com.example.money_manager.contract.model.LoginModel;
import com.example.money_manager.utils.NetworkUtils;

public class LoginPresenter implements LoginContract.Presenter {

   private LoginContract.View view;
   private LoginContract.Model model;

   public LoginPresenter(LoginContract.View view){
       this.view = view;
       model = new LoginModel();
   }

    @Override
    public void onLoginButtonClick(String email, String password) {
            model.login(email, password, new LoginContract.Model.OnLoginFinishedListener() {
                @Override
                public void onSuccess() {
                    view.navigateToHome();
                }

                @Override
                public void onError(String message) {
                    view.showLoginError(message);
                }

                @Override
                public void onErrorNeedToVerifyEmail(String message) {
                    view.showLoginErrorNeedToVerifyEmail(message);
                }
            });
    }
}
