package com.rskopyl.notes.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rskopyl.notes.data.model.Note;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    Flowable<List<Note>> getAll();

    @Query("SELECT * FROM note WHERE id = :id")
    Flowable<Note> getById(long id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    Single<Long> insert(Note note);

    @Update(onConflict = OnConflictStrategy.ABORT)
    Completable update(Note note);

    @Query("DELETE FROM note WHERE id = :id")
    Completable deleteById(long id);
}