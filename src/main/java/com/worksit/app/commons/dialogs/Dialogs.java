package com.worksit.app.commons.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by SKYNET-DEV01 on 27/02/2017.
 */

public class Dialogs {

    private static ProgressDialog progressDialog = null;

    public static void showError(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    public static void showInfo(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    public static void showQuestionYesCancel(Context context, String title, String message, String yesText, String cancelText, DialogInterface.OnClickListener yesClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(yesText, yesClickListener)
                .show();

    }

    public static void showProgressDialog(Context context, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);

            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
        else {
            progressDialog.setMessage(message);
        }
    }

    public static void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
        catch (Exception e) {
            //para evitar abortar. identificado com o uso do Firebase
        }
    }
}
