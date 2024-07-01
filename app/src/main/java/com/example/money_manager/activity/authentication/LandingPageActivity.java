 package com.example.money_manager.activity.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.money_manager.R;
import com.example.money_manager.activity.MainActivity;
import com.example.money_manager.contract.LandingPageContract;
import com.example.money_manager.contract.presenter.LandingPagePresenter;

 public class LandingPageActivity extends AppCompatActivity implements LandingPageContract.View {
private LandingPagePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);
        init();
    }
    private void init(){
            presenter = new LandingPagePresenter(this);
            presenter.checkAuthentication();
    }
     @Override
     public void navigateToHome() {
        startActivity(new Intent(LandingPageActivity.this, MainActivity.class));
        finish();
     }

     @Override
     public void navigateToAuthentication() {
         startActivity(new Intent(LandingPageActivity.this, AuthenticateActivity.class));
         finish();
     }

 }