package com.rskopyl.notes.utils;

import android.text.Editable;
import android.text.TextWatcher;

public interface DefaultTextWatcher extends TextWatcher {

    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    default void afterTextChanged(Editable s) {
    }
}