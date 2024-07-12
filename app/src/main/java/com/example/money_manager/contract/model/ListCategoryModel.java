package com.example.money_manager.contract.model;

import com.example.money_manager.contract.ListCategoryContract;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Reminder;
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

    @Override
    public void getCategories(OnCategoriesGetListener listener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference categoriesRef = firestore.collection("categories");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentAccount = currentUser.getEmail();

            categoriesRef.whereEqualTo("account", currentAccount).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Category> categories = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Category category = document.toObject(Category.class);
                            categories.add(category);
                        }

                        // Truy vấn thêm các category có account rỗng
                        categoriesRef.whereEqualTo("account", "").get()
                                .addOnSuccessListener(emptyAccountSnapshots -> {
                                    for (QueryDocumentSnapshot document : emptyAccountSnapshots) {
                                        Category category = document.toObject(Category.class);
                                        categories.add(category);
                                    }
                                    listener.onCategoriesGet(new ArrayList<>(categories));
                                })
                                .addOnFailureListener(e -> listener.onError(e));
                    })
                    .addOnFailureListener(e -> listener.onError(e));
        }
    }
}
