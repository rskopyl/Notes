package com.rskopyl.notes.repository;

import androidx.annotation.NonNull;

import com.rskopyl.notes.data.model.Note;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface NoteRepository {

    @NonNull
    Flowable<List<Note>> getAll();
}