package com.example.money_manager.contract;

public class LoginContract {

    public interface View {
        void showLoginError(String title, String message);

        void showLoginErrorNeedToVerifyEmail(String title, String message);

        void navigateToHome();

        void showPopupNetworkError(String title, String message);

        void showResetSuccess(String title, String message);
        void showResetError(String title, String message);
    }

    public interface Presenter {
        void onLoginButtonClick(String email, String password);

        void onResetPassWord(String email);
    }

    public interface Model {
        void login(String email, String password, LoginContract.Model.OnLoginFinishedListener listener);

        void resetPassword(String email, LoginContract.Model.OnResetPasswordListener listener);

        double getAccountBalance(String email, LoginContract.Model.onTransactionListener listener);

        interface OnLoginFinishedListener {
            void onSuccess();

            void onError(String title, String message);

            void onErrorNeedToVerifyEmail(String title, String message);
        }

        interface OnResetPasswordListener {
            void onSuccess();

            void onError(String message);
        }
        interface onTransactionListener {
            void onSuccess(Object object);

            void onError(String message);
        }
    }
}
