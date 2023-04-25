package com.rskopyl.notes.data.local;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class AppDatabaseConverters {

    @NonNull
    @TypeConverter
    public static String fromLocalDateTime(@NonNull LocalDateTime dateTime) {
        return dateTime.toString();
    }

    @NonNull
    @TypeConverter
    public static LocalDateTime toLocalDateTime(@NonNull String isoString) {
        return LocalDateTime.parse(isoString);
    }
}