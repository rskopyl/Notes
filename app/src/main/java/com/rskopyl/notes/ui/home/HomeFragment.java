package com.rskopyl.notes.ui.home;

import static androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.rskopyl.notes.Notes;
import com.rskopyl.notes.R;
import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.data.preferences.AppPreferences;
import com.rskopyl.notes.data.preferences.AppPreferences.Rendering;
import com.rskopyl.notes.databinding.FragmentHomeBinding;
import com.rskopyl.notes.ui.MultiViewModelFactory;
import com.rskopyl.notes.ui.details.DetailsFragment;
import com.rskopyl.notes.ui.search.SearchFragment;
import com.rskopyl.notes.utils.MenuUtils;
import com.rskopyl.notes.utils.ThemeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class HomeFragment extends Fragment {

    @Inject
    public MultiViewModelFactory viewModelFactory;

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;

    private final NoteAdapter noteAdapter = new NoteAdapter((note) -> {
        Bundle args = new Bundle();
        args.putLong(DetailsFragment.Args.NOTE_ID, note.id);
        getParentFragmentManager().beginTransaction()
                .add(R.id.fcv_main, DetailsFragment.class, args)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    });

    private final Map<Rendering, String> renderingToText = new HashMap<>(2);

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    private void setupUi() {
        Menu menu = binding.amvToolbar.getMenu();
        requireActivity().getMenuInflater().inflate(R.menu.home, menu);
        int colorOnBackground = ThemeUtils.resolveAttribute(
                requireActivity().getTheme(),
                com.google.android.material.R.attr.colorOnBackground
        );
        MenuUtils.setIconTint(menu, colorOnBackground);
        MenuUtils.setOnMenuItemClickListener(
                menu,
                menuItem -> {
                    if (menuItem.getItemId() == R.id.mi_search) {
                        getParentFragmentManager().beginTransaction()
                                .add(R.id.fcv_main, SearchFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    } else if (menuItem.getItemId() == R.id.mi_appearance) {
                        viewModel.toggleRendering();
                    } else {
                        return false;
                    }
                    return true;
                }
        );

        binding.rvNotes.setHasFixedSize(true);
        binding.rvNotes.setAdapter(noteAdapter);
        binding.fabCreate.setOnClickListener(
                view -> getParentFragmentManager().beginTransaction()
                        .add(R.id.fcv_main, DetailsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit()
        );
    }

    private void setupObservers() {
        viewModel.notes.observe(
                getViewLifecycleOwner(),
                this::showNotes
        );
        viewModel.rendering.observe(
                getViewLifecycleOwner(),
                this::showRendering
        );
    }

    private void showNotes(@NonNull List<Note> notes) {
        noteAdapter.submitList(notes);
        boolean hasPinned = notes.stream().anyMatch(note -> note.isPinned);
        binding.tvPinned.setVisibility(hasPinned ? View.VISIBLE : View.GONE);
    }

    private void showRendering(@NonNull AppPreferences.Rendering rendering) {
        RecyclerView.LayoutManager manager;
        if (rendering == Rendering.LIST) {
            manager = new LinearLayoutManager(requireContext());
        } else {
            StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL
            );
            staggered.setGapStrategy(GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            manager = staggered;
        }
        binding.rvNotes.setLayoutManager(manager);
        noteAdapter.setLayoutManager(manager);
        String appearance = renderingToText.get(rendering);
        binding.amvToolbar.getMenu().findItem(R.id.mi_appearance).setTitle(appearance);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Notes) context.getApplicationContext()).appComponent.inject(this);
        renderingToText.put(Rendering.LIST, getString(R.string.grid));
        renderingToText.put(Rendering.GRID, getString(R.string.list));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHomeBinding.bind(view);
        ViewModelProvider provider = new ViewModelProvider(
                getViewModelStore(), viewModelFactory
        );
        viewModel = provider.get(HomeViewModel.class);
        setupUi();
        setupObservers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}