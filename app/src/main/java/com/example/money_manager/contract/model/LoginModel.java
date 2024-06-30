package com.example.money_manager.contract.model;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.money_manager.R;
import com.example.money_manager.activity.MainActivity;
import com.example.money_manager.activity.authentication.LoginActivity;
import com.example.money_manager.contract.LoginContract;
import com.example.money_manager.contract.RegisterContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginModel implements LoginContract.Model {


    private FirebaseAuth mAuth;

    public LoginModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password, LoginContract.Model.OnLoginFinishedListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                listener.onSuccess();
                            } else {
                                listener.onErrorNeedToVerifyEmail("Please verify your email before login");
                                mAuth.getCurrentUser().reload();
                                mAuth.getCurrentUser().sendEmailVerification();
                            }
                        } else {
                            listener.onError(task.getException().getMessage());
                        }
                    }
                });
    }
}