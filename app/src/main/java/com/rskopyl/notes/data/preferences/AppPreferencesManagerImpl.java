package com.rskopyl.notes.data.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import androidx.annotation.NonNull;

import com.rskopyl.notes.data.preferences.AppPreferences.Rendering;
import com.rskopyl.notes.di.ApplicationDisposable;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AppPreferencesManagerImpl implements AppPreferencesManager {

    private final SharedPreferences sharedPreferences;

    private final CompositeDisposable disposable;

    @Inject
    public AppPreferencesManagerImpl(
            @NonNull SharedPreferences sharedPreferences,
            @NonNull @ApplicationDisposable CompositeDisposable disposable
    ) {
        this.sharedPreferences = sharedPreferences;
        this.disposable = disposable;
    }

    @NonNull
    @Override
    public Flowable<AppPreferences> getAll() {
        final OnSharedPreferenceChangeListener[] listener = { null };
        Flowable<AppPreferences> flowable = Flowable.create(
                emitter -> {
                    listener[0] = (sharedPreferences, key) -> {
                        emitter.onNext(mapPreferences(sharedPreferences));
                    };
                    emitter.onNext(mapPreferences(sharedPreferences));
                    sharedPreferences.registerOnSharedPreferenceChangeListener(listener[0]);
                },
                BackpressureStrategy.LATEST
        );
        return flowable
                .doOnCancel(() -> {
                    sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener[0]);
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void toggleRendering() {
        Disposable operation = getAll().firstElement()
                .subscribeOn(Schedulers.io())
                .subscribe(appPreferences -> {
                    Rendering rendering = appPreferences.rendering.opposite();
                    sharedPreferences.edit()
                            .putString(Keys.RENDERING, rendering.name())
                            .apply();
                });
        disposable.add(operation);
    }

    @NonNull
    private AppPreferences mapPreferences(@NonNull SharedPreferences preferences) {
        return new AppPreferences(
                Rendering.valueOf(
                        preferences.getString(
                                Keys.RENDERING, Rendering.LIST.name()
                        )
                )
        );
    }

    private static class Keys {

        private static final String RENDERING = "rendering";
    }
}