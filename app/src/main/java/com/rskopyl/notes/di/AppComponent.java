package com.rskopyl.notes.di;

import android.content.Context;

import com.rskopyl.notes.ui.home.HomeFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@Component(
        modules = {
                ViewModelModule.class,
                RepositoryModule.class,
                DisposableModule.class,
                DatabaseModule.class,
                PreferencesModule.class
        }
)
@Singleton
public interface AppComponent {

    void inject(HomeFragment fragment);

    @ApplicationDisposable
    CompositeDisposable getApplicationDisposable();

    @Component.Factory
    interface Factory {

        AppComponent create(@BindsInstance Context context);
    }
}