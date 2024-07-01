package com.example.money_manager.contract.model;

import androidx.annotation.NonNull;

import com.example.money_manager.contract.CreateReminderContract;
import com.example.money_manager.entity.Account;
import com.example.money_manager.entity.Reminder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateReminderModel implements CreateReminderContract.Model {

    FirebaseFirestore db ;

    public CreateReminderModel(){
        db =  FirebaseFirestore.getInstance();
    }

    @Override
    public void createNewReminderToDB(Reminder reminder, String notificationId, OnCreateNewReminderListener listener) {
        db.collection("reminders").document(notificationId)
                .set(reminder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getMessage());

                    }
                });
    }


}
