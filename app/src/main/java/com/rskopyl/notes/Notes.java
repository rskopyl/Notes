package com.rskopyl.notes;

import android.app.Application;

import com.rskopyl.notes.di.AppComponent;
import com.rskopyl.notes.di.DaggerAppComponent;

public class Notes extends Application {

    public AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.factory().create(this);
    }
}