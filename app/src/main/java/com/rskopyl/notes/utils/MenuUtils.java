package com.rskopyl.notes.utils;

import android.graphics.drawable.Drawable;
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

    public static void setIconTint(Menu menu, int color) {
        for (int index = 0; index < menu.size(); index++) {
            Drawable menuIcon = menu.getItem(index).getIcon();
            if (menuIcon != null) menuIcon.setTint(color);
        }
    }

    public static void enable(Menu menu, boolean isEnabled) {
        for (int index = 0; index < menu.size(); index++) {
            menu.getItem(index).setEnabled(isEnabled);
        }
    }
}