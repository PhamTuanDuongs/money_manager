package com.example.money_manager.contract.model;

import com.example.money_manager.contract.ListCategoryContract;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Reminder;
import com.example.money_manager.utils.AccountState;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListCategoryModel implements ListCategoryContract.Model {

    List<Category> categories = new ArrayList<>();
    @Override
    public void getCategories(OnCategoriesGetListener listener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference categoriesRef = firestore.collection("categories");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentAccount = currentUser.getEmail();

            categoriesRef.whereEqualTo("account_id", firestore.document("accounts/" + currentAccount)).get()
                    .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                        Category category = new Category();
                                        category.setId(documentSnapshot.getId());
                                        category.setName(documentSnapshot.get("name", String.class));
                                        category.setType(documentSnapshot.get("type", int.class));
                                        category.setIconImageId(documentSnapshot.get("image", String.class));
                                        String accountPath = documentSnapshot.get("account", String.class);
                                        if (accountPath != null && accountPath.startsWith("accounts/")) {
                                            String email = accountPath.substring("accounts/".length());
                                            category.setAccount(email);
                                        } else {
                                            category.setAccount(accountPath);
                                        }
                                        categories.add(category);
                                    }
                                    listener.onCategoriesGet(categories);
                                }
                                else{
                                    listener.onError(task.getException());
                                }
                    });
        }
        else{
            listener.onError(new Exception("No account found"));
        }


        // Truy vấn thêm các category có account rỗng
        categoriesRef.whereEqualTo("account", "").get()
                .addOnSuccessListener(emptyAccountSnapshots -> {
                    for (QueryDocumentSnapshot doc : emptyAccountSnapshots) {
                        Category category = new Category();
                        category.setId(doc.getId());
                        category.setName(doc.get("name", String.class));
                        category.setType(doc.get("type", int.class));
                        category.setIconImageId(doc.get("image", String.class));
                        category.setAccount(doc.get("account", String.class));
                        categories.add(category);
                    }
                    listener.onCategoriesGet(categories);
                })
                .addOnFailureListener(e -> listener.onError(e));
    }
}
