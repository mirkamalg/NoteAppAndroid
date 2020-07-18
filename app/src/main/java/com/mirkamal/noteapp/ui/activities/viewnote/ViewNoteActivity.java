package com.mirkamal.noteapp.ui.activities.viewnote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.mirkamal.noteapp.R;
import com.mirkamal.noteapp.ui.activities.main.DataBase;
import com.mirkamal.noteapp.ui.activities.main.Note;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ViewNoteActivity extends AppCompatActivity {

    private AppCompatButton buttonEdit, buttonDelete, buttonShare;
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
        editTextBody.setInputType(InputType.TYPE_NULL);
        editTextBody.setSingleLine(false);

        textViewDateTime.setText(getString(R.string.viewedatetime, Objects.requireNonNull(dataBase.getNotes().get(viewedNoteHeader)).getEditedTime().trim(), Objects.requireNonNull(dataBase.getNotes().get(viewedNoteHeader)).getEditedDate()).trim());
    }

    private void initializeUI() {
        buttonEdit = findViewById(R.id.button_edit);
        buttonDelete = findViewById(R.id.button_delete);
        buttonShare = findViewById(R.id.button_share);
        buttonSave = findViewById(R.id.button_save);
        editTextHeader = findViewById(R.id.edit_text_header);
        editTextBody = findViewById(R.id.edit_text_body);
        textViewDateTime = findViewById(R.id.text_view_date_time);

        editTextHeader.setInputType(InputType.TYPE_NULL);

        dataBase = DataBase.getInstance(getFilesDir());
    }

    private void configureLogic() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing();
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeSharing();
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

                if (isChanged()) {
                    if (!viewedNoteHeader.equals(editTextHeader.getText().toString())) {
                        if (isEverythingValid()) {
                            save();
                        } else {
                            Toast.makeText(ViewNoteActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else if (!dataBase.getNotes().get(viewedNoteHeader).getBody().equals(editTextBody.getText().toString())) {
                        if (isEverythingValid()) {
                            save();
                        } else {
                            Toast.makeText(ViewNoteActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ViewNoteActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewNoteActivity.this, "Note isn't changed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        editTextHeader.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                enableEditing();
                Toast.makeText(ViewNoteActivity.this, "You can edit the note", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    private boolean isEverythingValid() {
        if (!editTextHeader.getText().toString().isEmpty() && !editTextBody.getText().toString().isEmpty()) {
            if (editTextHeader.getText().toString().equals(viewedNoteHeader))
                return true;
            return !dataBase.getNotes().containsKey(editTextHeader.getText().toString());
        }
        return false;
    }

    private void enableEditing() {
        editTextHeader.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextBody.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editTextBody.setSingleLine(false);
        editTextBody.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);

        editTextHeader.requestFocus();

        InputMethodManager inputMethodManager =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(editTextHeader.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        editTextHeader.setSelection(editTextHeader.getText().length());
    }

    private void initializeSharing() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = editTextBody.getText().toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, editTextHeader.getText().toString());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private boolean isChanged() {
        return !editTextHeader.getText().toString().equals(viewedNoteHeader) || !editTextBody.getText().toString().equals(Objects.requireNonNull(dataBase.getNotes().get(viewedNoteHeader)).getBody());
    }

    private String[] getDateTime() {
        Date currentTime = Calendar.getInstance().getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormatForTime = new SimpleDateFormat("HH:mm");

        String date = simpleDateFormat.format(currentTime);
        String time = simpleDateFormatForTime.format(currentTime);

        return new String[]{date, time};
    }

    private void save() {
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