package com.rskopyl.notes.repository.impl;

import androidx.annotation.NonNull;

import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.repository.NoteRepository;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class EmptyNoteRepository implements NoteRepository {

    @Inject
    public EmptyNoteRepository() {
    }

    @NonNull
    @Override
    public Flowable<List<Note>> getAll() {
        return Flowable.fromIterable(Collections.emptyList());
    }
}