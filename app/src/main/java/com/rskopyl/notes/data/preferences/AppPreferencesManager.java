package com.rskopyl.notes.data.preferences;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Flowable;

public interface AppPreferencesManager {

    @NonNull
    Flowable<AppPreferences> getAll();

    void toggleRendering();
}