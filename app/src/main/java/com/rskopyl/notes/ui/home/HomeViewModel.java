package com.rskopyl.notes.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.data.preferences.AppPreferences;
import com.rskopyl.notes.data.preferences.AppPreferencesManager;
import com.rskopyl.notes.repository.NoteRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private final AppPreferencesManager appPreferencesManager;

    @NonNull
    public final LiveData<List<Note>> notes;

    @NonNull
    public final LiveData<AppPreferences.Rendering> rendering;

    @Inject
    public HomeViewModel(
            @NonNull NoteRepository noteRepository,
            @NonNull AppPreferencesManager appPreferencesManager
    ) {
        super();
        this.appPreferencesManager = appPreferencesManager;
        this.notes = LiveDataReactiveStreams.fromPublisher(
                noteRepository.getAll().map(notes -> {
                    long max = LocalDateTime.MAX.toEpochSecond(ZoneOffset.UTC);
                    notes.sort(Comparator.comparingLong(note -> {
                        long time = note.dateTime.toEpochSecond(ZoneOffset.UTC);
                        return note.isPinned ? -time : (max - time);
                    }));
                    return notes;
                })
        );
        this.rendering = LiveDataReactiveStreams.fromPublisher(
                appPreferencesManager.getAll().map(
                        appPreferences -> appPreferences.rendering
                )
        );
    }

    public void toggleRendering() {
        appPreferencesManager.toggleRendering();
    }
}