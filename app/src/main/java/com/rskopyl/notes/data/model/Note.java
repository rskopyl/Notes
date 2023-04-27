package com.rskopyl.notes.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Entity(tableName = "note")
public class Note {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "date_time")
    public LocalDateTime dateTime;

    @ColumnInfo(name = "is_pinned")
    public boolean isPinned;

    public static long EMPTY_ID = 0;

    public Note() {
        this(
                EMPTY_ID,
                "", "",
                LocalDateTime.now(ZoneId.systemDefault()),
                false
        );
    }

    public Note(
            long id,
            String title,
            String content,
            LocalDateTime dateTime,
            boolean isPinned
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.isPinned = isPinned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                isPinned == note.isPinned &&
                Objects.equals(title, note.title) &&
                Objects.equals(content, note.content) &&
                Objects.equals(dateTime, note.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, dateTime, isPinned);
    }
}