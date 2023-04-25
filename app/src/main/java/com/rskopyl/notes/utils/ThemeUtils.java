package com.rskopyl.notes.utils;

import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.NonNull;

public class ThemeUtils {

    public static int resolveAttribute(@NonNull Resources.Theme theme, int resid) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(resid, typedValue, true);
        return typedValue.data;
    }
}