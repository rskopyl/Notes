package com.rskopyl.notes.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class MultiViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels;

    @Inject
    public MultiViewModelFactory(
            Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels
    ) {
        this.viewModels = viewModels;
    }

    @NonNull
    @Override
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) viewModels.get(modelClass).get();
    }
}