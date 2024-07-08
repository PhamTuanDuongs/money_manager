package com.example.money_manager.contract.model;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.money_manager.R;
import com.example.money_manager.activity.authentication.LoginActivity;
import com.example.money_manager.activity.authentication.RegisterActivity;
import com.example.money_manager.contract.RegisterContract;
import com.example.money_manager.entity.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

public class RegisterModel implements RegisterContract.Model  {
    private FirebaseAuth mAuth;

    FirebaseFirestore db ;
    public RegisterModel() {
        mAuth = FirebaseAuth.getInstance();
        db =  FirebaseFirestore.getInstance();
    }

    @Override
    public void register(String email, String password, final OnRegisterFinishedListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addAccount(new Account(email));
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                        listener.onSuccess();
                    } else {
                        listener.onError(task.getException().getMessage());
                    }
    });
    }

    public void addAccount(Account account){
        db.collection("accounts").document(account.getEmail())
                .set(account)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}
