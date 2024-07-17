package com.example.money_manager.contract.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.money_manager.contract.ReportContract;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.CategorySum;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportModel implements ReportContract.Model {

    private static final String TRANSACTION_COLLECTION = "transactions";
    private static final String CATEGORY_COLLECTION = "categories";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    public void getTransactionSummary(int year, int type, String email, onTransactionListener listener) {
        final Map<Integer, Double> monthlyTotals = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            monthlyTotals.put(i, 0.0);
        }
        Log.d("model","model");
        DocumentReference user = firestore
                .collection("accounts")
                .document(email);
        firestore.collection(TRANSACTION_COLLECTION)
                .whereEqualTo("type",type)
                .whereEqualTo("account_id",user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                Map<String, Object> transaction = document.getData();
                                if (transaction != null) {
                                    double amount = (double) Double.parseDouble(transaction.get("amount").toString());
                                    Timestamp timestamp = (Timestamp) transaction.get("date");
                                    Date date = new Date(timestamp.toDate().getTime());
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int transactionYear = calendar.get(Calendar.YEAR);
                                    int transactionMonth = calendar.get(Calendar.MONTH);


                                    if (transactionYear == year) {
                                        double currentTotal = monthlyTotals.get(transactionMonth);
                                        monthlyTotals.put(transactionMonth, currentTotal + amount);
                                    }
                                }
                            }

                            // Gọi listener khi đã tính toán xong
                            if (listener != null) {
                                listener.onSuccess(monthlyTotals);
                            }
                        } else {
                            // Xử lý khi truy vấn không thành công
                            if (listener != null) {
                                listener.onError("Error");
                            }
                        }
                    }
                });

    }

    @Override
    public void getCategorySummary(int year,int type, String email, onTransactionListener listener) {
        DocumentReference user = firestore
                .collection("accounts")
                .document(email);
        ArrayList<CategorySum> categories = new ArrayList<>();
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

                                CategorySum c = new CategorySum();
                                String autoID= dRef.getId();
                                String name = q.get("name", String.class);
                                String img = q.get("image", String.class);
                                c.setName(name);
                                c.setIcon(img);
                                DocumentReference category = firestore
                                        .collection("categories")
                                        .document(autoID);
                                firestore.collection("transactions")
                                        .whereEqualTo("category", category)
                                        .whereEqualTo("account_id", user)
                                        .whereEqualTo("type", type)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                if (t.isSuccessful()) {
                                                double totalAmount = 0.0;
                                                QuerySnapshot querySnapshot = t.getResult();
                                                for (DocumentSnapshot document : querySnapshot) {
                                                    Timestamp timestamp = (Timestamp) document.get("date");
                                                    Date date = new Date(timestamp.toDate().getTime());
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.setTime(date);
                                                    int transactionYear = calendar.get(Calendar.YEAR);
                                                    if (transactionYear==year) {
                                                        Double amount = document.getDouble("amount");

                                                        if (amount != null) {
                                                            totalAmount += amount;
                                                        }
                                                    }
                                                }
                                                c.setTotalAmount((float) totalAmount);
                                                c.setPercent("ok");
                                                categories.add(c);
                                            }
                                            listener.onSuccess(categories);
                                        }});
                            }
                        }
                    }});
    }

}

