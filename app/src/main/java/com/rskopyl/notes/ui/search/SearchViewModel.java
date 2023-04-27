package com.rskopyl.notes.ui.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.data.preferences.AppPreferences;
import com.rskopyl.notes.data.preferences.AppPreferencesManager;
import com.rskopyl.notes.repository.NoteRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> filter;

    @NonNull
    public final LiveData<List<Note>> notes;

    @NonNull
    public final LiveData<String> pattern;

    @NonNull
    public final LiveData<AppPreferences.Rendering> rendering;

    @Inject
    public SearchViewModel(
            @NonNull NoteRepository noteRepository,
            @NonNull AppPreferencesManager appPreferencesManager
            ) {
        this.filter = new MutableLiveData<>("");
        this.notes = Transformations.switchMap(
                filter,
                filter -> {
                    if (filter.isEmpty()) {
                        return new MutableLiveData<>(Collections.emptyList());
                    } else {
                        return LiveDataReactiveStreams.fromPublisher(
                                noteRepository.getAll().map(notes ->
                                        notes.stream()
                                                .filter(new SearchPredicate(filter))
                                                .collect(Collectors.toList())
                                )
                        );
                    }
                }
        );
        this.pattern = filter;
        this.rendering = LiveDataReactiveStreams.fromPublisher(
                appPreferencesManager.getAll().map(
                        appPreferences -> appPreferences.rendering
                )
        );
    }

    public void search(@NonNull String filter) {
        this.filter.setValue(filter);
    }

    public void clear() {
        this.filter.setValue("");
    }

    private static class SearchPredicate implements Predicate<Note> {

        private final String filter;

        public SearchPredicate(String filter) {
            this.filter = filter;
        }

        @Override
        public boolean test(@NonNull Note note) {
            List<String> texts = Arrays.asList(note.title, note.content);
            return texts.stream().anyMatch(
                    text -> text.toLowerCase().contains(filter.toLowerCase())
            );
        }
    }
}