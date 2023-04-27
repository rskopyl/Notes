package com.rskopyl.notes.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class ContextUtils {

    public static void showSoftInput(@NonNull Context context, @NonNull View view) {
        view.requestFocus();
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftInput(@NonNull Context context, @NonNull View view) {
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        View focusedView = view.findFocus();
        if (focusedView != null) focusedView.clearFocus();
    }
}