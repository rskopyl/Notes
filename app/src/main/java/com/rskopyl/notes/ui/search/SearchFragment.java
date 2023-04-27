package com.rskopyl.notes.ui.search;

import static com.rskopyl.notes.data.preferences.AppPreferences.Rendering;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.rskopyl.notes.databinding.FragmentSearchBinding;
import com.rskopyl.notes.ui.MultiViewModelFactory;
import com.rskopyl.notes.ui.details.DetailsFragment;
import com.rskopyl.notes.utils.ContextUtils;
import com.rskopyl.notes.utils.DefaultTextWatcher;
import com.rskopyl.notes.utils.SpacingItemDecoration;

import javax.inject.Inject;

public class SearchFragment extends Fragment {

    @Inject
    public MultiViewModelFactory viewModelFactory;

    private FragmentSearchBinding binding;

    private SearchViewModel viewModel;

    private SearchAdapter searchAdapter;

    private boolean isOpenedFirstTime;

    private final TextWatcher searchTextWatcher = new SearchTextWatcher();

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    private void setupUi() {
        binding.toolbar.setNavigationOnClickListener((view) -> {
            ContextUtils.hideSoftInput(requireContext(), binding.getRoot());
            getParentFragmentManager().popBackStack();
        });
        binding.etFilter.addTextChangedListener(searchTextWatcher);
        if (isOpenedFirstTime) {
            ContextUtils.showSoftInput(requireContext(), binding.etFilter);
        }
        binding.ivClear.setOnClickListener((view) -> {
            binding.etFilter.getText().clear();
            viewModel.clear();
        });

        binding.rvNotes.setHasFixedSize(true);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvNotes.setAdapter(searchAdapter);
        binding.rvNotes.addItemDecoration(
                new SpacingItemDecoration(
                        getResources().getDimensionPixelSize(R.dimen.small_125)
                )
        );
    }

    private void setupObservers() {
        viewModel.notes.observe(
                getViewLifecycleOwner(),
                searchAdapter::submitList
        );
        viewModel.pattern.observe(
                getViewLifecycleOwner(),
                searchAdapter::submitPattern
        );
        viewModel.rendering.observe(
                getViewLifecycleOwner(),
                this::showRendering
        );
    }

    private void showRendering(AppPreferences.Rendering rendering) {
        RecyclerView.LayoutManager manager = rendering == Rendering.LIST ?
                new LinearLayoutManager(requireContext()) :
                new GridLayoutManager(requireContext(), 2);
        binding.rvNotes.setLayoutManager(manager);
        searchAdapter.setLayoutManager(manager);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Notes) context.getApplicationContext()).appComponent.inject(this);
        searchAdapter  = new SearchAdapter(
                requireContext(),
                (note) -> {
                    Bundle args = new Bundle();
                    args.putLong(DetailsFragment.Args.NOTE_ID, note.id);
                    ContextUtils.hideSoftInput(requireContext(), binding.getRoot());
                    getParentFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fcv_main, DetailsFragment.class, args)
                            .addToBackStack(null)
                            .commit();
                }
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isOpenedFirstTime = savedInstanceState == null;
        binding = FragmentSearchBinding.bind(view);
        viewModel = new ViewModelProvider(
                getViewModelStore(), viewModelFactory
        ).get(SearchViewModel.class);
        setupUi();
        setupObservers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class SearchTextWatcher implements DefaultTextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            String filter = binding.etFilter.getText().toString().trim();
            binding.ivClear.setVisibility(!filter.isEmpty() ? View.VISIBLE : View.GONE);
            viewModel.search(filter);
        }
    }
}