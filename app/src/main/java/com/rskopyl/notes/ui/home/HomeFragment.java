package com.rskopyl.notes.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rskopyl.notes.Notes;
import com.rskopyl.notes.R;
import com.rskopyl.notes.data.preferences.AppPreferences;
import com.rskopyl.notes.data.preferences.AppPreferences.Rendering;
import com.rskopyl.notes.databinding.FragmentHomeBinding;
import com.rskopyl.notes.ui.MultiViewModelFactory;
import com.rskopyl.notes.utils.MenuUtils;
import com.rskopyl.notes.utils.SpacingItemDecoration;
import com.rskopyl.notes.utils.ThemeUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class HomeFragment extends Fragment {

    @Inject
    public MultiViewModelFactory viewModelFactory;

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;

    private final NoteAdapter noteAdapter = new NoteAdapter();

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
        for (int index = 0; index < menu.size(); index++) {
            Drawable menuIcon = menu.getItem(index).getIcon();
            if (menuIcon != null) menuIcon.setTint(colorOnBackground);
        }
        MenuUtils.setOnMenuItemClickListener(
                menu,
                menuItem -> {
                    if (menuItem.getItemId() == R.id.mi_appearance) {
                        viewModel.toggleRendering();
                    } else {
                        return false;
                    }
                    return true;
                }
        );
        binding.rvNotes.setHasFixedSize(true);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNotes.setAdapter(noteAdapter);
        binding.rvNotes.addItemDecoration(
                new SpacingItemDecoration(
                        getResources().getDimensionPixelSize(R.dimen.small_125)
                )
        );
    }

    private void setupObservers() {
        viewModel.notes.observe(
                getViewLifecycleOwner(),
                noteAdapter::submitList
        );
        viewModel.rendering.observe(
                getViewLifecycleOwner(),
                this::showRendering
        );
    }

    private void showRendering(@NonNull AppPreferences.Rendering rendering) {
        RecyclerView.LayoutManager manager = rendering == Rendering.LIST ?
                new LinearLayoutManager(requireContext()) :
                new GridLayoutManager(requireContext(), 2);
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