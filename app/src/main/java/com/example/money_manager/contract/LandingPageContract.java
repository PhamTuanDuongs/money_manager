package com.example.money_manager.contract;

public class LandingPageContract {
    public interface  View {
        void navigateToHome();
        void navigateToAuthentication();
    }


    public interface Presenter {
        void checkAuthentication();
    }

    public interface Model {
        boolean isAuthenticated();
    }
}
