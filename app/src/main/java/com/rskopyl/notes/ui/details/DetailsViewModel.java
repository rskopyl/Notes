package com.rskopyl.notes.ui.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.repository.NoteRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class DetailsViewModel extends ViewModel {

    private final NoteRepository noteRepository;

    private final MutableLiveData<Long> noteId;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @NonNull
    public final LiveData<Note> note;

    @AssistedInject
    public DetailsViewModel(
            @Assisted long noteId,
            @NonNull NoteRepository noteRepository
    ) {
        this.noteRepository = noteRepository;
        this.noteId = new MutableLiveData<>(noteId);
        if (noteId == Note.EMPTY_ID) {
            this.note = new MutableLiveData<>(new Note());
        } else {
            this.note = LiveDataReactiveStreams
                    .fromPublisher(noteRepository.getById(noteId));
        }
    }

    public void save(@NonNull String title, @NonNull String content) {
        if (title.isEmpty() && content.isEmpty()) return;
        Note note = Objects.requireNonNull(this.note.getValue());
        note = new Note(
                note.id,
                title, content,
                LocalDateTime.now(ZoneId.systemDefault()),
                note.isPinned
        );
        if (note.id == Note.EMPTY_ID) {
            Disposable operation = noteRepository
                    .insert(note)
                    .subscribe(noteId::setValue);
            disposable.add(operation);
        } else {
            noteRepository.update(note);
        }
    }

    public void toggleIsPinned() {
        Note note = Objects.requireNonNull(this.note.getValue());
        note = new Note(
                note.id,
                note.title, note.content,
                note.dateTime,
                !note.isPinned
        );
        if (note.id != Note.EMPTY_ID) {
            noteRepository.update(note);
        }
    }

    public void delete() {
        Note note = Objects.requireNonNull(this.note.getValue());
        if (note.id != Note.EMPTY_ID) {
            noteRepository.deleteById(note.id);
        }
    }

    @Override
    protected void onCleared() {
        disposable.clear();
    }

    @AssistedFactory
    public interface Factory {

        DetailsViewModel create(long noteId);
    }
}