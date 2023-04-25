package com.rskopyl.notes.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@Module
public class DisposableModule {

    @Provides
    @ApplicationDisposable
    @Singleton
    public CompositeDisposable provideApplicationDisposable() {
        return new CompositeDisposable();
    }
}