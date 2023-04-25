package com.rskopyl.notes.data.preferences;

import androidx.annotation.NonNull;

public class AppPreferences {

    public final Rendering rendering;

    public AppPreferences(@NonNull Rendering rendering) {
        this.rendering = rendering;
    }

    public enum Rendering {
        LIST, GRID;

        Rendering opposite() {
            return this == LIST ? GRID : LIST;
        }
    }
}