package com.example.money_manager.contract.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.money_manager.contract.IncomeContract;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IncomeModel implements IncomeContract.Model {

    private static final String TRANSACTION_COLLECTION = "transactions";
    private static final String CATEGORY_COLLECTION = "categories";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public ArrayList<Transaction> getTransactions(String email,String date, onTransactionListener listener) {
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

        ArrayList<Transaction> incomes = new ArrayList<>();
        firestore
                .collection(TRANSACTION_COLLECTION)
                .whereEqualTo("type", 0)
                .whereEqualTo("account_id", user)
                .whereGreaterThanOrEqualTo("date", date1)
                .whereLessThanOrEqualTo("date", date2)
                .orderBy("date")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size()!=0){
                                for (QueryDocumentSnapshot q : task.getResult()) {
                                    DocumentReference dRef = q.getReference();
                                    Transaction t = new Transaction();
                                    t.setAutoID(dRef.getId());
                                    double amount = q.get("amount", double.class);
                                    Date createAt = q.get("date", Date.class);
                                    int id = q.get("id",int.class);
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
                                                    incomes.add(t);
                                                }


                                                listener.onSuccess(incomes);
                                            }
                                        }
                                    });
                                    String description = q.get("description", String.class);

                                    String name = q.get("name", String.class);
                                    t.setId(id);
                                    t.setAmount(amount);
                                    t.setCreateAt(createAt);
                                    t.setDescription(description);
                                    t.setName(name);
                                }
                            }else{
                                listener.onSuccess(incomes);
                            }
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
        DocumentReference category = firestore
                .collection("categories")
                .document(transaction.getCategory().getAutoID());

        Map<String, Object> docData = new HashMap<>();
        docData.put("amount", transaction.getAmount());
        docData.put("date", transaction.getCreateAt());
        docData.put("description", transaction.getDescription());
        docData.put("type", transaction.getType());
        docData.put("account_id", user);
        docData.put("id", getTransactionId());
        docData.put("name",transaction.getName());
        docData.put("category", category);

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
        DocumentReference category = firestore
                .collection("categories")
                .document(transactionn.getCategory().getAutoID());
        Map<String, Object> docData = new HashMap<>();
        docData.put("amount", transactionn.getAmount());
        docData.put("date", transactionn.getCreateAt());
        docData.put("description", transactionn.getDescription());
        docData.put("name",transactionn.getName());
        docData.put("category", category);
        firestore
                .collection(TRANSACTION_COLLECTION)
                .whereEqualTo("id", id)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                firestore
                                        .collection(TRANSACTION_COLLECTION)
                                        .document(q.getId())
                                        .update(docData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                listener.onSuccess(null);
                                            }
                                        });
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void load(int id, onTransactionListener listener) {
        firestore
                .collection(TRANSACTION_COLLECTION)
                .whereEqualTo("type", 0)
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                double amount = q.get("amount", double.class);
                                String description = q.get("description", String.class);
                                int id = q.get("id", int.class);
                                Timestamp createAt = q.get("date", Timestamp.class);
                                String name = q.get("name", String.class);
                                Transaction t = new Transaction();
                                t.setCreateAt(createAt.toDate());
                                t.setAmount(amount);
                                t.setDescription(description);
                                t.setId(id);
                                t.setName(name);

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
                                                listener.onSuccess(t);

                                            }



                                        }
                                    }
                                });
                                break;
                            }
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

    private int getTransactionId() {
        int time = (int) new Date().getTime();
        return time;
    }
}
