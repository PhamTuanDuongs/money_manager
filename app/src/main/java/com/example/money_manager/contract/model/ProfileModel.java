package com.example.money_manager.contract.model;

import androidx.annotation.NonNull;

import com.example.money_manager.contract.ProfileContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileModel implements ProfileContract.Model {

    @Override
    public void changePassword(ProfileContract.OnChangePasswordFinishedListener listener, String email) {
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
}
