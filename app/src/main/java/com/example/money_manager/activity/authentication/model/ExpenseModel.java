package com.example.money_manager.activity.authentication.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.money_manager.contract.ExpenseContract;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpenseModel implements ExpenseContract.Model {

    private static final String TRANSACTION_COLLECTION = "transactions";
    private static final String CATEGORY_COLLECTION = "categories";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public ArrayList<Transaction> getTransactions(String email, String date, onTransactionListener listener) {
        String[] dates = date.split(" - ");
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
        Date date1, date2;
        try {
            date1 =formatter.parse(dates[0]);
            date2=formatter.parse(dates[1]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        date2.setHours(23);
        date2.setMinutes(59);
        date2.setSeconds(59);

        DocumentReference user = firestore
                .collection("accounts")
                .document(email);

        ArrayList<Transaction> expenses = new ArrayList<>();
        firestore
                .collection(TRANSACTION_COLLECTION)
                .whereEqualTo("type", 1)
                .whereEqualTo("account_id", user)
                .whereGreaterThanOrEqualTo("date", date1)
                .whereLessThanOrEqualTo("date", date2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size()!=0){
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                Transaction t = new Transaction();
                                double amount = q.get("amount", double.class);
                                Date createAt = q.get("date", Date.class);
                                DocumentReference category=q.getDocumentReference("category");
                                Category c= new Category();
                                c.setAutoID(category.getId());
                                category.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {

                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String categoryName = document.get("name",String.class);
                                                c.setName(categoryName);
                                                t.setCategory(c);
                                                expenses.add(t);
                                            }


                                            listener.onSuccess(expenses);
                                        }
                                    }
                                });
                                String description = q.get("description", String.class);

                                String name = q.get("name", String.class);
                                t.setAmount(amount);
                                t.setCreateAt(createAt);
                                t.setDescription(description);

                                t.setName(name);
                            }
                            }else{
                                listener.onSuccess(expenses);
                            }
                        }
                    }
                });

        return expenses;
    }

    @Override
    public void getCategoryListByEmailAndType(String email, int type, onTransactionListener listener) {
        DocumentReference user = firestore
                .collection("accounts")
                .document(email);

        ArrayList<Category> categories = new ArrayList<>();
        firestore
                .collection(CATEGORY_COLLECTION)
                .whereEqualTo("type", type)
                .whereEqualTo("account_id", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot q : task.getResult()) {

                                    DocumentReference dRef = q.getReference();

                                    Category c = new Category();
                                    String autoID= dRef.getId();
                                    String name = q.get("name", String.class);
                                    String img = q.get("image", String.class);
                                    c.setName(name);
                                    c.setAutoID(autoID);
                                    c.setIconImageId(img);
                                    categories.add(c);

                                }
                                listener.onSuccess(categories);


                }
                    }});

    }

    @Override
    public void add(Transaction transaction, String email, onTransactionListener listener) {

        DocumentReference user = firestore
                .collection("accounts")
                .document(email);
        DocumentReference category = firestore
                .collection("categories")
                .document(transaction.getCategory().getAutoID());

        Map<String, Object> docData = new HashMap<>();
        docData.put("amount", transaction.getAmount());
        docData.put("date", transaction.getCreateAt());
        docData.put("name", transaction.getName());
        docData.put("description", transaction.getDescription());
        docData.put("type", transaction.getType());
        docData.put("account_id", user);
        docData.put("category", category);
        firestore.collection(TRANSACTION_COLLECTION)
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        listener.onSuccess(transaction);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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
