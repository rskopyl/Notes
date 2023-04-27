package com.rskopyl.notes.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory<T extends ViewModel> implements ViewModelProvider.Factory {

    private final Producer<T> producer;

    public ViewModelFactory(Producer<T> producer) {
        this.producer = producer;
    }

    @NonNull
    @Override
    @SuppressWarnings({"unchecked", "TypeParameterHidesVisibleType"})
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) producer.produce();
    }

    public interface Producer<T extends ViewModel> {

        T produce();
    }
}