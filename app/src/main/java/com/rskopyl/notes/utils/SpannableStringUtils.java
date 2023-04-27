package com.rskopyl.notes.utils;

import android.text.SpannableString;

import androidx.annotation.NonNull;

public class SpannableStringUtils {

    public static void setSpan(
            @NonNull SpannableString string,
            @NonNull Object what,
            @NonNull String pattern,
            int flags
    ) {
        int fromIndex = 0;
        String lowercase = string.toString().toLowerCase();
        while (fromIndex != string.length()) {
            int start = lowercase.indexOf(pattern, fromIndex);
            int end = start + pattern.length();
            if (start < 0) return;
            string.setSpan(what, start, end, flags);
            fromIndex = start + 1;
        }
    }
}