package com.mirkamal.noteapp.ui.activities.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mirkamal.noteapp.R;
import com.mirkamal.noteapp.ui.activities.addnote.AddNoteActivity;
import com.mirkamal.noteapp.ui.activities.viewnote.ViewNoteActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private MaterialToolbar materialToolbar;
    private FloatingActionButton buttonAdd;

    private DataBase dataBase;
    private List<Note> notesList = new ArrayList<>();

    private NoteRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = DataBase.getInstance(getFilesDir());
        setUpUI();
        setUpRecyclerView();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || requestCode == 2) {
            notesList.clear();
            notesList.addAll(dataBase.getNotes().values());

            adapter.submitList(notesList);
        }
    }

    private void setUpUI() {
        recyclerViewNotes = findViewById(R.id.recycler_view_notes);
        materialToolbar = findViewById(R.id.tool_bar);
        buttonAdd = findViewById(R.id.floating_action_button_add);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Re1r0/NoteAppAndroid"));
                startActivity(browserIntent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);

                startActivityForResult(intent, 1);
            }
        });
    }

    private void setUpRecyclerView() {
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new NoteRecyclerViewAdapter(new NoteClickListener() {
            @Override
            public void onNoteClicked(int position) {
                Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);

                intent.putExtra("header", notesList.get(position).getHeader());

                startActivityForResult(intent, 2);
            }
        });
        recyclerViewNotes.setAdapter(adapter);

        notesList.clear();
        notesList.addAll(dataBase.getNotes().values());
        adapter.submitList(notesList);
    }
}