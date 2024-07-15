package com.example.money_manager.contract.model;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.money_manager.R;
import com.example.money_manager.activity.MainActivity;
import com.example.money_manager.activity.authentication.LoginActivity;
import com.example.money_manager.contract.ExpenseContract;
import com.example.money_manager.contract.LoginContract;
import com.example.money_manager.contract.RegisterContract;
import com.example.money_manager.entity.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginModel implements LoginContract.Model {


    private FirebaseAuth mAuth;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static final String TRANSACTION_COLLECTION = "transactions";

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
                                listener.onErrorNeedToVerifyEmail("Login Failed","Please verify your email before login");
                                mAuth.getCurrentUser().reload();
                                mAuth.getCurrentUser().sendEmailVerification();
                            }
                        } else {
                            listener.onError("Login Failed","Invalid authentication data.");
                        }
                    }
                });
    }

    @Override
    public void resetPassword(String email, OnResetPasswordListener listener) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onSuccess();
                        } else {
                            listener.onError(task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public double getAccountBalance(String email, onTransactionListener listener) {

        DocumentReference user = firestore
                .collection("accounts")
                .document(email);
        ArrayList<Double> accountBalance = new ArrayList<>();
        firestore
                .collection(TRANSACTION_COLLECTION)
                .whereEqualTo("account_id", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                double amount = q.get("amount", double.class);
                                int type = q.get("type", int.class);
                                if (type ==0){
                                    accountBalance.add(amount);
                                }else{
                                    accountBalance.add(-amount);
                                }

                            }
                            listener.onSuccess(accountBalance);
                        }

                    }
                });
        double sum = 0.0;
        for (Double number : accountBalance) {
            sum += number;
        }
        return sum;
    }

}
