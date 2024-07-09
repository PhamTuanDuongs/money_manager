package com.example.money_manager.contract.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.money_manager.contract.IncomeContract;
import com.example.money_manager.entity.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IncomeModel implements IncomeContract.Model {

    private static final String TRANSACTION_COLLECTION = "transactions";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public ArrayList<Transaction> getTransactions(String email, onTransactionListener listener) {
        DocumentReference user = firestore
                .collection("accounts")
                .document(email);

        ArrayList<Transaction> incomes = new ArrayList<>();
        firestore
                .collection(TRANSACTION_COLLECTION)
                .whereEqualTo("type", 0)
                .whereEqualTo("account_id", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                double amount = q.get("amount", double.class);
                                Date createAt = q.get("createdAt", Date.class);
                                String description = q.get("description", String.class);
                                int id = q.get("id", int.class);
                                String name = q.get("name", String.class);
                                Transaction t = new Transaction();
                                t.setAmount(amount);
                                t.setCreateAt(createAt);
                                t.setDescription(description);
                                t.setId(id);
                                t.setName(name);
                                incomes.add(t);

                            }
                            listener.onSuccess(incomes);
                        }
                    }
                });

        return incomes;
    }

    @Override
    public void add(Transaction transaction, String email, onTransactionListener listener) {

        DocumentReference user = firestore
                .collection("accounts")
                .document(email);

        Map<String, Object> docData = new HashMap<>();
        docData.put("amount", transaction.getAmount());
        docData.put("createAt", transaction.getCreateAt());
        docData.put("description", transaction.getDescription());
        docData.put("type", transaction.getType());
        docData.put("account_id", user);
        docData.put("id", getTransactionId());

        transaction.setId(getTransactionId());
        firestore.collection(TRANSACTION_COLLECTION)
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("ADD_TRANS", "DocumentSnapshot written with ID: " + documentReference.getId());
                        listener.onSuccess(transaction);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ADD_TRANS", "Error adding document", e);
                        listener.onError("Failed to add");
                    }
                });
    }

    @Override
    public void delete(int id, onTransactionListener listener) {
        Log.d("TEST_DELETE", "Id: " + id);
        firestore
                .collection(TRANSACTION_COLLECTION)
                .whereEqualTo("id", id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() >= 1) {
                            for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                                firestore
                                        .collection(TRANSACTION_COLLECTION)
                                        .document(q.getId())
                                        .delete();
                            }

                            listener.onSuccess(null);

                        } else {
                            listener.onError("Delete failed");
                        }

                        Log.d("TEST_DELETE", queryDocumentSnapshots.size() + "");
                    }
                });


    }

    @Override
    public void update(Transaction transactionn, int id, onTransactionListener listener) {
        Map<String, Object> result = new HashMap<>();
        result.put("amount", transactionn.getAmount());
        result.put("description", transactionn.getDescription());
        firestore.collection(TRANSACTION_COLLECTION).document(id + "").update(result);
    }

    private int getTransactionId() {
        int time = (int) new Date().getTime();
        return time;
    }
}
