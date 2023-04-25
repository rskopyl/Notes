package com.rskopyl.notes.data.preferences;

import androidx.annotation.NonNull;

import com.rskopyl.notes.data.preferences.AppPreferences.Rendering;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class EmptyAppPreferencesManager implements AppPreferencesManager {

    @Inject
    public EmptyAppPreferencesManager() {
    }

    @NonNull
    @Override
    public Flowable<AppPreferences> getAll() {
        return Flowable.just(new AppPreferences(Rendering.LIST));
    }

    @Override
    public void toggleRendering() {
    }
}