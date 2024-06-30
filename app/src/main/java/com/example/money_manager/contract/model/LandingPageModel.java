package com.example.money_manager.contract.model;

import com.example.money_manager.contract.LandingPageContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingPageModel implements LandingPageContract.Model {

    private FirebaseAuth mAuth;

    public LandingPageModel() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean isAuthenticated() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }
}
