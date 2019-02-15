package com.example.englishtester.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class SimpleDialogHelper {
    public static void showMessage(String title, String message, Context context) {
        new AlertDialog.Builder(context)//
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public static void showConfirm(String title, String message, DialogInterface.OnClickListener confirmListener, Context context) {
        new AlertDialog.Builder(context)//
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("確定", confirmListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}