package com.rskopyl.notes.di;

import com.rskopyl.notes.data.preferences.AppPreferencesManager;
import com.rskopyl.notes.data.preferences.AppPreferencesManagerImpl;
import com.rskopyl.notes.repository.NoteRepository;
import com.rskopyl.notes.repository.impl.NoteRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public interface RepositoryModule {

    @Binds
    @Singleton
    NoteRepository bindNoteRepository(NoteRepositoryImpl impl);

    @Binds
    @Singleton
    AppPreferencesManager bindAppPreferencesManager(AppPreferencesManagerImpl impl);
}