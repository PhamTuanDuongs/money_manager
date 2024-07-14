package com.example.money_manager.contract.model;

import android.util.Log;

import com.example.money_manager.contract.ListReminderContract;
import com.example.money_manager.entity.Reminder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListReminderModel implements ListReminderContract.Model {

    @Override
    public void getReminders(OnFinishedListener listener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference remindersRef = firestore.collection("reminders");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentAccount = currentUser.getEmail();

            remindersRef.whereEqualTo("account", currentAccount).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Reminder> reminders = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Reminder reminder = doc.toObject(Reminder.class);
                        reminder.setId(doc.getId());
                        reminders.add(reminder);
                    }
                    listener.onFinished(reminders);
                } else {
                    listener.onFailure(task.getException());
                }
            });
        }else {
            listener.onFailure(new Exception("No account found"));
        }
    }
}
