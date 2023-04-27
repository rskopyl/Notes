package com.rskopyl.notes.di;

import androidx.lifecycle.ViewModel;

import com.rskopyl.notes.ui.home.HomeViewModel;
import com.rskopyl.notes.ui.search.SearchViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    ViewModel bindHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    ViewModel bindSearchViewModel(SearchViewModel viewModel);
}