package com.rskopyl.notes.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.rskopyl.notes.data.model.Note;

@Database(
        version = 1,
        entities = {Note.class},
        exportSchema = true
)
@TypeConverters(AppDatabaseConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();
}