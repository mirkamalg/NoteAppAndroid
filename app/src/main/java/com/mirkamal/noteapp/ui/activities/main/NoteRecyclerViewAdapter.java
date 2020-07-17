package com.mirkamal.noteapp.ui.activities.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirkamal.noteapp.R;

import java.util.ArrayList;
import java.util.List;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<ItemNoteViewHolder> {

    private List<Note> notes = new ArrayList<>();

    private NoteClickListener listener;

    public NoteRecyclerViewAdapter(NoteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_notes, parent, false);

        return new ItemNoteViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemNoteViewHolder holder, int position) {
        holder.bindItem(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void submitList(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
        this.notifyDataSetChanged();
    }
}
