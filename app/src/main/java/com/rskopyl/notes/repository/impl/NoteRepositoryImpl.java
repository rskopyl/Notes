package com.rskopyl.notes.repository.impl;

import androidx.annotation.NonNull;

import com.rskopyl.notes.data.local.NoteDao;
import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.di.ApplicationDisposable;
import com.rskopyl.notes.repository.NoteRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteRepositoryImpl implements NoteRepository {

    private final NoteDao noteDao;

    private final CompositeDisposable disposable;

    @Inject
    public NoteRepositoryImpl(
            @NonNull NoteDao noteDao,
            @NonNull @ApplicationDisposable CompositeDisposable disposable
    ) {
        this.noteDao = noteDao;
        this.disposable = disposable;
    }

    @NonNull
    @Override
    public Flowable<List<Note>> getAll() {
        return noteDao.getAll().subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Flowable<Note> getById(long id) {
        return noteDao.getById(id).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<Long> insert(Note note) {
        CompletableFuture<Long> noteId = new CompletableFuture<>();
        Disposable operation = noteDao.insert(note)
                .subscribeOn(Schedulers.io())
                .subscribe(noteId::complete);
        disposable.add(operation);
        return Single.fromFuture(noteId);
    }

    @Override
    public void update(Note note) {
        Disposable operation = noteDao.update(note)
                .subscribeOn(Schedulers.io())
                .subscribe();
        disposable.add(operation);
    }

    @Override
    public void deleteById(long id) {
        Disposable operation = noteDao.deleteById(id)
                .subscribeOn(Schedulers.io())
                .subscribe();
        disposable.add(operation);
    }
}