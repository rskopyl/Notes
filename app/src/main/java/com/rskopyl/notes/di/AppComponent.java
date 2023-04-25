package com.rskopyl.notes.di;

import com.rskopyl.notes.ui.home.HomeFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                ViewModelModule.class,
                RepositoryModule.class
        }
)
@Singleton
public interface AppComponent {

    void inject(HomeFragment fragment);
}