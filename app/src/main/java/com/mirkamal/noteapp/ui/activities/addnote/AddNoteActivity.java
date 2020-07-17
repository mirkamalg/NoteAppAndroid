package com.mirkamal.noteapp.ui.activities.addnote;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mirkamal.noteapp.R;
import com.mirkamal.noteapp.ui.activities.main.DataBase;
import com.mirkamal.noteapp.ui.activities.main.Note;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextHeader, editTextBody;
    private MaterialButton buttonAdd;

    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        dataBase = DataBase.getInstance(getFilesDir());
        setUpUI();
        setUpLogic();
    }

    private void setUpUI() {
        editTextHeader = findViewById(R.id.edit_text_header);
        editTextBody = findViewById(R.id.edit_text_body);
        buttonAdd = findViewById(R.id.button_add);
    }

    private void setUpLogic() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEverythingValid()) {

                    String[] dateTime = getDateTime();

                    dataBase.addNote(new Note(editTextHeader.getText().toString(), editTextBody.getText().toString(), dateTime[0], dateTime[1]));

                    FileOutputStream fos;
                    try {
                        fos = openFileOutput(editTextHeader.getText().toString(), MODE_PRIVATE);

                        String writtenNoteBody = editTextBody.getText().toString() + "!=!" + dateTime[0] + "=" + dateTime[1];

                        fos.write(writtenNoteBody.getBytes());
                        fos.close();

                        Toast.makeText(AddNoteActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AddNoteActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEverythingValid() {
        if (!editTextHeader.getText().toString().isEmpty() && !editTextBody.getText().toString().isEmpty()) {
            return !dataBase.getNotes().containsKey(editTextHeader.getText().toString());
        }
        return false;
    }

    private String[] getDateTime() {
        Date currentTime = Calendar.getInstance().getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormatForTime = new SimpleDateFormat("HH:mm");

        String date = simpleDateFormat.format(currentTime);
        String time = simpleDateFormatForTime.format(currentTime);

        return new String[] {date, time};
    }
}