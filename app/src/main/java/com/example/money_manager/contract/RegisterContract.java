package com.example.money_manager.contract;

public class RegisterContract {
    // interface for the View
   public  interface View {
        void showRegistrationError(String message);
        void showRegistrationSuccess(String message);
        void navigateToLogin();
        void resetDataOfFields();
        void showPopupNetworkError();

   }

   public interface Presenter {
        void onRegisterButtonClick(String email, String password);
   }

    public interface Model {
        void register(String email, String password, OnRegisterFinishedListener listener);

        interface OnRegisterFinishedListener {
            void onSuccess();
            void onError(String message);
        }
    }
}
