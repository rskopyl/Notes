package com.rskopyl.notes.utils;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

public class MenuUtils {

    public static void setOnMenuItemClickListener(
            @NonNull Menu menu,
            @NonNull MenuItem.OnMenuItemClickListener listener
    ) {
        for (int index = 0; index < menu.size(); index++) {
            MenuItem menuItem = menu.getItem(index);
            menuItem.setOnMenuItemClickListener(listener);
        }
    }
}