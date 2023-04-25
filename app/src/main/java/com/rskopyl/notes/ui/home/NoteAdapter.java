package com.rskopyl.notes.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.databinding.ItemNoteBinding;
import com.rskopyl.notes.utils.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    private final SimpleDateFormat dateTimeFormat =
            new SimpleDateFormat("EEEE hh:mm a", Locale.getDefault());

    private final OnItemClickListener<Note> onItemClick;

    private RecyclerView.LayoutManager layoutManager;

    public NoteAdapter() {
        this(null);
    }

    public NoteAdapter(@Nullable OnItemClickListener<Note> onItemClick) {
        super(new NoteDiffCallback());
        this.onItemClick = onItemClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.layoutManager = recyclerView.getLayoutManager();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                ItemNoteBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        holder.bind(note);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private final ItemNoteBinding binding;

        public NoteViewHolder(@NonNull ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull Note note) {
            Date date = new Date(
                    note.dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
            );
            boolean isExpanded = layoutManager instanceof GridLayoutManager;
            boolean hasContent = !note.content.isEmpty();
            binding.tvTitle.setText(note.title);
            binding.tvTitle.setMaxLines(isExpanded ? 2 : 1);
            binding.tvDateTime.setText(dateTimeFormat.format(date));
            binding.tvContent.setText(note.content);
            binding.tvContent.setVisibility(
                    isExpanded && hasContent ? View.VISIBLE : View.GONE
            );
            if (onItemClick != null) {
                binding.getRoot().setOnClickListener((view) ->
                        onItemClick.onClick(note)
                );
            }
        }
    }

    public static class NoteDiffCallback extends DiffUtil.ItemCallback<Note> {

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.equals(newItem);
        }
    }
}