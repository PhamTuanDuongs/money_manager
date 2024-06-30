package com.example.money_manager.contract.presenter;

import com.example.money_manager.contract.RegisterContract;
import com.example.money_manager.contract.model.RegisterModel;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View view;
    private RegisterContract.Model model;

    private FirebaseAuth mAuth;
    public RegisterPresenter(RegisterContract.View view) {
        this.model = new RegisterModel();
        this.view = view;
    }

    @Override
    public void onRegisterButtonClick(String email, String password) {

        model.register(email, password, new RegisterContract.Model.OnRegisterFinishedListener() {
            @Override
            public void onSuccess() {
                view.showRegistrationSuccess("User registered successfully. Please verify your email");
                view.navigateToLogin();
                view.resetDataOfFields();
            }

            @Override
            public void onError(String message) {
                view.showRegistrationError(message);
            }
        });
    }
}
