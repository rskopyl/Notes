package com.rskopyl.notes.repository;

import androidx.annotation.NonNull;

import com.rskopyl.notes.data.model.Note;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface NoteRepository {

    @NonNull
    Flowable<List<Note>> getAll();

    @NonNull
    Flowable<Note> getById(long id);

    @NonNull
    Single<Long> insert(Note note);

    void update(Note note);

    void deleteById(long id);
}