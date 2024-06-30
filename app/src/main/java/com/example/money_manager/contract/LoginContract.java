package com.example.money_manager.contract;

public class LoginContract {

    public  interface View {
        void showLoginError(String message);
        void showLoginErrorNeedToVerifyEmail(String message);
        void navigateToHome();
        void showPopupNetworkError();
    }

    public interface Presenter {
        void onLoginButtonClick(String email, String password);
    }

    public interface Model {
        void login(String email, String password, LoginContract.Model.OnLoginFinishedListener listener);

        interface OnLoginFinishedListener {
            void onSuccess();
            void onError(String message);
            void onErrorNeedToVerifyEmail(String message);
        }
    }
}
