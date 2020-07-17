package com.mirkamal.noteapp.ui.activities.viewnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mirkamal.noteapp.R;
import com.mirkamal.noteapp.ui.activities.addnote.AddNoteActivity;
import com.mirkamal.noteapp.ui.activities.main.DataBase;
import com.mirkamal.noteapp.ui.activities.main.Note;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ViewNoteActivity extends AppCompatActivity {

    private AppCompatButton buttonEdit, buttonDelete;
    private Button buttonSave;
    private EditText editTextHeader, editTextBody;
    private TextView textViewDateTime;

    private DataBase dataBase;

    String viewedNoteHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        initializeUI();
        setUpUI();
        configureLogic();
    }

    private void setUpUI() {
        viewedNoteHeader = getIntent().getStringExtra("header");

        editTextHeader.setText(viewedNoteHeader);
        editTextBody.setText(Objects.requireNonNull(dataBase.getNotes().get(viewedNoteHeader)).getBody());

        textViewDateTime.setText(getString(R.string.viewedatetime, Objects.requireNonNull(dataBase.getNotes().get(viewedNoteHeader)).getEditedTime().trim(), Objects.requireNonNull(dataBase.getNotes().get(viewedNoteHeader)).getEditedDate()).trim());
    }

    private void initializeUI() {
        buttonEdit = findViewById(R.id.button_edit);
        buttonDelete = findViewById(R.id.button_delete);
        buttonSave = findViewById(R.id.button_save);
        editTextHeader = findViewById(R.id.edit_text_header);
        editTextBody = findViewById(R.id.edit_text_body);
        textViewDateTime = findViewById(R.id.text_view_date_time);

        editTextHeader.setInputType(InputType.TYPE_NULL);
        editTextBody.setInputType(InputType.TYPE_NULL);

        dataBase = DataBase.getInstance(getFilesDir());
    }

    private void configureLogic() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextHeader.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextBody.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBase.getNotes().remove(viewedNoteHeader);

                deleteFile(viewedNoteHeader);

                Toast.makeText(ViewNoteActivity.this, "Note is deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldBody = Objects.requireNonNull(dataBase.getNotes().get(viewedNoteHeader)).getBody();

                if (!viewedNoteHeader.equals(editTextHeader.getText().toString()) && !oldBody.equals(editTextBody.getText().toString()) && isEverythingValid()) {
                    deleteFile(viewedNoteHeader);

                    String[] dateTime = getDateTime();

                    dataBase.getNotes().remove(viewedNoteHeader);
                    dataBase.getNotes().put(editTextHeader.getText().toString(), new Note(editTextHeader.getText().toString(), editTextBody.getText().toString(), dateTime[0], dateTime[1]));

                    FileOutputStream fos;
                    try {
                        fos = openFileOutput(editTextHeader.getText().toString(), MODE_PRIVATE);

                        String writtenNoteBody = editTextBody.getText().toString() + "!=!" + dateTime[0] + "=" + dateTime[1];

                        fos.write(writtenNoteBody.getBytes());
                        fos.close();

                        Toast.makeText(ViewNoteActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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