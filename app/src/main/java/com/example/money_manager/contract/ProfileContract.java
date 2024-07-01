package com.example.money_manager.contract;

public class ProfileContract {
    public interface View {
        void showProfile();
        void showErrorNotifyResetPassword(String message);
        void showNotifyResetPassword();
    }

    public interface Presenter {
        void onClickChangePassword(String email);
    }

    public interface Model {
        void changePassword(OnChangePasswordFinishedListener listener, String email);
    }

    public interface OnChangePasswordFinishedListener {
        void onSuccess();
        void onError(String message);
    }
}
