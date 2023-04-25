package com.rskopyl.notes.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.rskopyl.notes.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(
                Constants.PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE
        );
    }
}