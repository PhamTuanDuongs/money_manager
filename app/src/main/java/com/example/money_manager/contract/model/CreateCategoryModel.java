package com.example.money_manager.contract.model;

import com.example.money_manager.contract.CreateCategoryContract;
import com.example.money_manager.entity.Category;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateCategoryModel implements CreateCategoryContract.Model {
    FirebaseFirestore db;

    public CreateCategoryModel() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void createCategory(Category category, OnCreateCategoryListener listener) {
        DocumentReference user = db.collection("accounts").document(category.getAccount());

        Map<String, Object> categoryData = new HashMap<>();
        categoryData.put("name", category.getName());
        categoryData.put("type", category.getType());
        categoryData.put("account_id", user);
        categoryData.put("image", category.getImage());

        db.collection("categories")
                .add(categoryData)
                .addOnSuccessListener(documentReference -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }
}
