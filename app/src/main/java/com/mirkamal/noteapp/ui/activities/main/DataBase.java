package com.mirkamal.noteapp.ui.activities.main;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataBase {

    private Map<String, Note> notes;
    private static DataBase dataBase;

    private DataBase() throws IOException {
        notes = new HashMap<>();
    }

    public static DataBase getInstance(File folder) {
        if (dataBase == null) {
            try {
                dataBase = new DataBase();

                for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                    String header = fileEntry.getName();

                    String rawBody = "";
                    try (BufferedReader reader = new BufferedReader(new FileReader(fileEntry))) {
                        String line;
                        StringBuilder stringBuilder = new StringBuilder();
                        String ls = System.getProperty("line.separator");
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                            stringBuilder.append(ls);
                        }

                        rawBody = stringBuilder.toString();

                        String[] parts = rawBody.split("!=!");
                        String[] dateTime = parts[1].split("=");

                        if (dataBase != null) {
                            dataBase.getNotes().put(header, new Note(header, parts[0], dateTime[0], dateTime[1]));
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return dataBase;
        }

        return dataBase;
    }

    public void addNote(Note note) {
        dataBase.getNotes().put(note.getHeader(), note);
    }

    public Map<String, Note> getNotes() {
        return notes;
    }
}
