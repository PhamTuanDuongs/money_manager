package com.example.money_manager.contract.presenter;

import com.example.money_manager.contract.LandingPageContract;
import com.example.money_manager.contract.model.LandingPageModel;


public class LandingPagePresenter implements LandingPageContract.Presenter {

    private LandingPageModel model;
    private LandingPageContract.View view;

    public LandingPagePresenter(LandingPageContract.View view) {
        model = new LandingPageModel();
        this.view = view;
    }

    @Override
    public void checkAuthentication() {
        if (model.isAuthenticated()) {
            view.navigateToHome();
        } else {
            view.navigateToAuthentication();
        }
    }
}
