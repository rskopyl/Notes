package com.rskopyl.notes.di;

import android.content.Context;

import androidx.room.RoomDatabase;

import com.rskopyl.notes.data.local.AppDatabase;
import com.rskopyl.notes.data.local.NoteDao;
import com.rskopyl.notes.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(Context context) {
        return new RoomDatabase
                .Builder<>(
                    context,
                    AppDatabase.class,
                    Constants.ROOM_DATABASE_NAME
                )
                .build();
    }

    @Provides
    public NoteDao provideNoteDao(AppDatabase appDatabase) {
        return appDatabase.getNoteDao();
    }
}