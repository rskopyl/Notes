package com.rskopyl.notes.ui.details;

import static com.jakewharton.rxbinding4.view.RxView.clicks;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rskopyl.notes.Notes;
import com.rskopyl.notes.R;
import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.databinding.FragmentDetailsBinding;
import com.rskopyl.notes.utils.ContextUtils;
import com.rskopyl.notes.utils.DefaultTextWatcher;
import com.rskopyl.notes.utils.MenuUtils;
import com.rskopyl.notes.utils.ThemeUtils;
import com.rskopyl.notes.utils.ViewModelFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class DetailsFragment extends Fragment {

    @Inject
    public DetailsViewModel.Factory viewModelFactory;

    private FragmentDetailsBinding binding;

    private DetailsViewModel viewModel;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private boolean isOpenedFirstTime;

    private TextWatcher enableTextWatcher;

    private final Map<Boolean, String> isPinnedToText = new HashMap<>(2);

    public DetailsFragment() {
        super(R.layout.fragment_details);
    }

    private void setupUi() {
        binding.toolbar.setNavigationOnClickListener((view) -> {
            ContextUtils.hideSoftInput(requireContext(), binding.getRoot());
            getParentFragmentManager().popBackStack();
        });

        View miSave = binding.toolbar.findViewById(R.id.mi_save);
        Disposable observer = clicks(miSave)
                .throttleFirst(1L, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> {
                    viewModel.save(
                            binding.etTitle.getText().toString().trim(),
                            binding.etContent.getText().toString().trim()
                    );
                    ContextUtils.hideSoftInput(requireContext(), binding.getRoot());
                    showSavedToast();
                });
        disposable.add(observer);

        MenuUtils.enable(binding.toolbar.getMenu(), false);
        MenuUtils.setOnMenuItemClickListener(
                binding.toolbar.getMenu(),
                (menuItem) -> {
                    if (menuItem.getItemId() == R.id.mi_share) {
                        Note note = Objects.requireNonNull(viewModel.note.getValue());
                        if (note.id != Note.EMPTY_ID) shareNote(note);
                    } else if (menuItem.getItemId() == R.id.mi_pin) {
                        viewModel.toggleIsPinned();
                    } else if (menuItem.getItemId() == R.id.mi_delete) {
                        viewModel.delete();
                        getParentFragmentManager().popBackStack();
                    } else {
                        return false;
                    }
                    ContextUtils.hideSoftInput(requireContext(), binding.getRoot());
                    return true;
                }
        );
        binding.etTitle.addTextChangedListener(enableTextWatcher);
        binding.etContent.addTextChangedListener(enableTextWatcher);
    }

    private void setupObservers() {
        viewModel.note.observe(
                getViewLifecycleOwner(),
                this::showNote
        );
    }

    private void showNote(@NonNull Note note) {
        if (note.id == Note.EMPTY_ID && isOpenedFirstTime) {
            ContextUtils.showSoftInput(requireContext(), binding.etContent);
        }
        if (binding.etTitle.getText().toString().isEmpty()) {
            binding.etTitle.setText(note.title);
        }
        if (binding.etContent.getText().toString().isEmpty()) {
            binding.etContent.setText(note.content);
        }
        String isPinned = isPinnedToText.get(note.isPinned);
        binding.toolbar.getMenu().findItem(R.id.mi_pin).setTitle(isPinned);
    }

    private void showSavedToast() {
        Toast.makeText(
                requireContext(),
                R.string.note_saved,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void shareNote(@NonNull Note note) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, note.title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, note.content);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Notes) context.getApplicationContext()).appComponent.inject(this);
        enableTextWatcher = new EnableTextWatcher();
        isPinnedToText.put(false, getString(R.string.pin));
        isPinnedToText.put(true, getString(R.string.unpin));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isOpenedFirstTime = savedInstanceState == null;
        binding = FragmentDetailsBinding.bind(view);
        viewModel = new ViewModelProvider(
                getViewModelStore(),
                new ViewModelFactory<>(() -> {
                    Bundle args = getArguments();
                    long noteId = args != null ?
                            args.getLong(Args.NOTE_ID, Note.EMPTY_ID) :
                            Note.EMPTY_ID;
                    return viewModelFactory.create(noteId);
                })
        ).get(DetailsViewModel.class);
        setupUi();
        setupObservers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
        binding = null;
    }

    private class EnableTextWatcher implements DefaultTextWatcher {

        private final int colorOnBackground;
        private final int colorOnSurfaceVariant;

        private EnableTextWatcher() {
            this.colorOnBackground = ThemeUtils.resolveAttribute(
                    requireContext().getTheme(),
                    com.google.android.material.R.attr.colorOnBackground
            );
            this.colorOnSurfaceVariant = ThemeUtils.resolveAttribute(
                    requireActivity().getTheme(),
                    com.google.android.material.R.attr.colorOnSurfaceVariant
            );
        }

        @Override
        public void afterTextChanged(Editable string) {
            List<TextView> fields = Arrays.asList(binding.etTitle, binding.etContent);
            boolean isNoteEmpty = fields.stream().allMatch(
                    view -> view.getText().toString().isEmpty()
            );
            ColorStateList indicatorColor = ColorStateList.valueOf(
                    isNoteEmpty ? colorOnSurfaceVariant : colorOnBackground
            );
            MenuUtils.enable(binding.toolbar.getMenu(), !isNoteEmpty);
            binding.etTitle.setBackgroundTintList(indicatorColor);
        }
    }

    public static class Args {

        public static final String NOTE_ID = "note-id";
    }
}