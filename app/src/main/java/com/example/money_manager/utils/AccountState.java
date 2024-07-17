package com.example.money_manager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountState {
    private static final String PREFS_NAME = "Money_Manager";

    // Method to save email to SharedPreferences
    public static void saveEmail(Context context, String email, String KEY) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY, email);
        editor.apply();
    }

    // Method to retrieve email from SharedPreferences
    public static String getEmail(Context context, String KEY) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY, null); // Return null if key not found
    }
    public static void saveAccountBalance(Context context, double accountBalance, String KEY) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY, accountBalance+"");
        editor.apply();
    }
    public static String getAccountBalance(Context context, String KEY) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY, null); // Return null if key not found
    }
    public static void updateAccountBalance(Context context, double amount, String KEY) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        double currentAmount = Double.parseDouble(getAccountBalance(context,KEY));
        editor.remove(KEY);
        currentAmount+=currentAmount;
        saveAccountBalance(context,currentAmount,KEY);
    }
}
