package com.rskopyl.notes.repository.impl;

import androidx.annotation.NonNull;

import com.rskopyl.notes.data.local.NoteDao;
import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.repository.NoteRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteRepositoryImpl implements NoteRepository {

    private final NoteDao noteDao;

    @Inject
    public NoteRepositoryImpl(@NonNull NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @NonNull
    @Override
    public Flowable<List<Note>> getAll() {
        return noteDao.getAll().subscribeOn(Schedulers.io());
    }
}