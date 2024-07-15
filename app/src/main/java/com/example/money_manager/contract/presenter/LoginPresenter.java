package com.example.money_manager.contract.presenter;

import com.example.money_manager.activity.authentication.LoginActivity;
import com.example.money_manager.contract.LoginContract;
import com.example.money_manager.contract.model.LoginModel;
import com.example.money_manager.utils.AccountState;

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
                    AccountState.saveEmail((LoginActivity)view, email, "email");
                    double balance = model.getAccountBalance(email, new LoginContract.Model.onTransactionListener() {
                                @Override
                                public void onSuccess(Object object) {
                                }
                                @Override
                                public void onError(String message) {

                                }
                            });


                    AccountState.saveAccountBalance((LoginActivity)view, balance, "balance");
                    view.navigateToHome();
                }

                @Override
                public void onError(String titel, String message) {
                    view.showLoginError(titel ,message);
                }

                @Override
                public void onErrorNeedToVerifyEmail(String title, String message) {
                    view.showLoginErrorNeedToVerifyEmail(title, message);
                }
            });
    }

    @Override
    public void onResetPassWord(String email) {
        model.resetPassword(email, new LoginContract.Model.OnResetPasswordListener() {
            @Override
            public void onSuccess() {
                view.showResetSuccess("Password Reset", "You will receive a password reset email. Please ensure that your new password is at least 8 characters long.");
            }

            @Override
            public void onError(String message) {
                view.showResetError("Password Reset failed", message);
            }
        });
    }
}
