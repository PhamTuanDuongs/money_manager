package com.example.money_manager.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class DialogUtils {

    public static void showDialogError (String title, String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showDialogSuccess (String title, String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
