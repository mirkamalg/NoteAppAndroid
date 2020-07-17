package com.mirkamal.noteapp.ui.activities.main;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirkamal.noteapp.R;

public class ItemNoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textViewHeader;
    private TextView textViewDate;
    private TextView textViewTime;

    private NoteClickListener listener;

    public ItemNoteViewHolder(@NonNull View itemView, NoteClickListener listener) {
        super(itemView);

        this.listener = listener;

        itemView.setOnClickListener(this);

        setUpViews();
    }

    private void setUpViews() {
        textViewHeader = itemView.findViewById(R.id.text_view_header);
        textViewDate = itemView.findViewById(R.id.text_view_date);
        textViewTime = itemView.findViewById(R.id.text_view_time);
    }

    public void bindItem(Note note) {
        textViewHeader.setText(note.getHeader());
        textViewDate.setText(note.getEditedDate());
        textViewTime.setText(note.getEditedTime());
    }

    @Override
    public void onClick(View view) {
        listener.onNoteClicked(getAdapterPosition());
    }
}
