package com.rskopyl.notes.di;

import com.rskopyl.notes.data.preferences.AppPreferencesManager;
import com.rskopyl.notes.data.preferences.EmptyAppPreferencesManager;
import com.rskopyl.notes.repository.NoteRepository;
import com.rskopyl.notes.repository.impl.EmptyNoteRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public interface RepositoryModule {

    @Binds
    @Singleton
    NoteRepository bindNoteRepository(EmptyNoteRepository impl);

    @Binds
    @Singleton
    AppPreferencesManager bindAppPreferencesManager(EmptyAppPreferencesManager impl);
}