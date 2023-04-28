package com.example.proyectomovil.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import com.example.proyectomovil.R;

public class AlertUtils {

    private static final String TAG = AlertUtils.class.getName();
    public static void  shortToast(Context context, String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void  longToast(Context context, String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void shortSimpleSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void longSimpleSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    public static void indefiniteSnackbar(View parentView, String message) {
        Log.i(TAG, String.format("indefiniteSnackbar: %s", message));
        Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.snackBar_dismiss_label, view -> {
            snackbar.dismiss();
        });
        snackbar.show();
    }
}
