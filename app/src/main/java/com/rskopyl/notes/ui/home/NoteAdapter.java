package com.rskopyl.notes.ui.home;

import static androidx.recyclerview.widget.RecyclerView.LayoutManager;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.rskopyl.notes.data.model.Note;
import com.rskopyl.notes.databinding.ItemDividerBinding;
import com.rskopyl.notes.databinding.ItemNoteBinding;
import com.rskopyl.notes.utils.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends ListAdapter<Note, ViewHolder> {

    private final SimpleDateFormat dateTimeFormat =
            new SimpleDateFormat("EEEE hh:mm a", Locale.getDefault());

    private final OnItemClickListener<Note> onItemClick;

    private LayoutManager layoutManager;

    public NoteAdapter(@Nullable OnItemClickListener<Note> onItemClick) {
        super(new NoteDiffCallback());
        this.onItemClick = onItemClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.layoutManager = recyclerView.getLayoutManager();
    }

    private int getDividerPosition(@NonNull List<Note> notes) {
        if (notes.isEmpty()) return -1;
        Note previous = notes.get(0);
        for (int index = 1; index < notes.size(); index++) {
            Note current = notes.get(index);
            if (current.isPinned != previous.isPinned) {
                return index;
            } else {
                previous = current;
            }
        }
        return -1;
    }

    @Override
    public void submitList(@Nullable List<Note> list) {
        if (list != null) {
            List<Note> notes = new ArrayList<>(list);
            int divider = getDividerPosition(notes);
            if (divider != -1) notes.add(divider, null);
            super.submitList(notes);
        } else {
            super.submitList(null);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) != null) {
            return ViewType.NOTE.ordinal();
        } else {
            return ViewType.DIVIDER.ordinal();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ViewType.NOTE.ordinal()) {
            return new NoteViewHolder(
                    ItemNoteBinding.inflate(inflater, parent, false)
            );
        } else {
            return new DividerViewHolder(
                    ItemDividerBinding.inflate(inflater, parent, false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = getItem(position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (getItemViewType(position) == ViewType.NOTE.ordinal()) {
            ((NoteViewHolder) holder).bind(note);
        } else if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams())
                    .setFullSpan(true);
        }
    }

    public enum ViewType {NOTE, DIVIDER}

    private class NoteViewHolder extends ViewHolder {

        private final ItemNoteBinding binding;

        private NoteViewHolder(@NonNull ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull Note note) {
            Date date = new Date(note.dateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
            boolean isExpanded = layoutManager instanceof StaggeredGridLayoutManager;
            boolean hasContent = !note.content.isEmpty();

            binding.tvTitle.setText(note.title);
            binding.tvTitle.setMaxLines(isExpanded ? 2 : 1);
            binding.tvDateTime.setText(dateTimeFormat.format(date));
            binding.tvContent.setText(note.content);
            binding.tvContent.setVisibility(
                    isExpanded && hasContent ? android.view.View.VISIBLE : View.GONE
            );
            if (onItemClick != null) {
                binding.getRoot().setOnClickListener((view) ->
                        onItemClick.onClick(note)
                );
            }
        }
    }

    private static class DividerViewHolder extends ViewHolder {

        private DividerViewHolder(@NonNull ItemDividerBinding binding) {
            super(binding.getRoot());
        }
    }

    private static class NoteDiffCallback extends DiffUtil.ItemCallback<Note> {

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return newItem.equals(oldItem);
        }
    }
}