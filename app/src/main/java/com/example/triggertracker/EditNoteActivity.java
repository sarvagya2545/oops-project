package com.example.triggertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNoteActivity extends AppCompatActivity {

    Button btnEditNote;
    EditText mNoteContent, mNoteTitle;
    String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        btnEditNote = findViewById(R.id.btnEditNote);
        mNoteTitle = findViewById(R.id.changeNoteTitle);
        mNoteContent = findViewById(R.id.changeNoteContent);

        btnEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = mNoteTitle.getText().toString();
                String newContent = mNoteContent.getText().toString();

                editNote(newTitle, newContent, docId);
            }
        });
    }

    private void editNote(String newTitle, String newContent, String docId) {

    }
}