package com.example.money_manager.contract.model;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.money_manager.R;
import com.example.money_manager.activity.authentication.LoginActivity;
import com.example.money_manager.activity.authentication.RegisterActivity;
import com.example.money_manager.contract.RegisterContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.github.muddz.styleabletoast.StyleableToast;

public class RegisterModel implements RegisterContract.Model  {
    private FirebaseAuth mAuth;

    public RegisterModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void register(String email, String password, final OnRegisterFinishedListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onError(task.getException().getMessage());
                    }
    });
    }
}
