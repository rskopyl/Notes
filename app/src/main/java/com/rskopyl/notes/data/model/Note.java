package com.rskopyl.notes.data.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Note {

    public long id;

    public String title;

    public String content;

    public LocalDateTime dateTime;

    public boolean isPinned;

    public Note(long id,
                String title,
                String content,
                LocalDateTime dateTime,
                boolean isPinned) {
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