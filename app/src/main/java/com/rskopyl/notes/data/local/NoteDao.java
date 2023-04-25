package com.rskopyl.notes.data.local;

import androidx.room.Dao;
import androidx.room.Query;

import com.rskopyl.notes.data.model.Note;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    Flowable<List<Note>> getAll();
}