package com.example.money_manager.contract.presenter;

import android.util.Log;

import com.example.money_manager.activity.authentication.LandingPageActivity;
import com.example.money_manager.contract.LandingPageContract;
import com.example.money_manager.utils.AESUtil;
import com.example.money_manager.utils.AccountState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LandingPagePresenter implements LandingPageContract.Presenter {

    private LandingPageContract.View view;
    private FirebaseAuth mAuth;

    public LandingPagePresenter(LandingPageContract.View view) {
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void checkAuthentication() {
        String email = AccountState.getEmail((LandingPageActivity)view, "email");
        if(email != null && !email.isEmpty()  ){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(Objects.equals(currentUser.getEmail(), email)){
                view.navigateToHome();
                Log.d("Usersdadasdasd", currentUser.getEmail());
            }else{
                view.navigateToAuthentication();
                Log.d("Usersdadasdasd", currentUser.getEmail());
            }
        }else{
            view.navigateToAuthentication();
        }
    }
}