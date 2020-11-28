package com.example.triggertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class AddNoteActivity extends AppCompatActivity {

    private EditText mNoteTitle, mNoteContent;
    private Button mAddNote;
    private String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mNoteTitle = findViewById(R.id.editNoteTitle);
        mNoteContent = findViewById(R.id.editNoteContent);
        mAddNote = findViewById(R.id.btnAddNote);

        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = mNoteTitle.getText().toString();
                String noteContent = mNoteContent.getText().toString();

                if(Pattern.matches("", noteTitle)) {
                    mNoteTitle.setError("Please enter something");
                }
                else {
                    Note note = new Note(noteTitle, noteContent, FirebaseAuth.getInstance().getCurrentUser().getUid());

                    FirebaseFirestore.getInstance()
                            .collection("notes")
                            .add(note)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "onSuccess: Added the item to firebase");
                                    Toast.makeText(AddNoteActivity.this, "Saved the note!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "onFailure: ", e);
                                }
                            });
                }

            }
        });
    }
}