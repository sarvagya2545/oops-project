package com.example.triggertracker.ui.notes;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.triggertracker.Note;
import com.example.triggertracker.ShoppingItem;
import com.example.triggertracker.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NoteViewModel extends ViewModel {

    private MutableLiveData<List<Note>> mNotes;
    private String TAG = "TAG";

    public NoteViewModel() {
        mNotes = new MutableLiveData<>();
    }

    public MutableLiveData<List<Note>> getNotes() {
        FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null)  {
                            Log.e(TAG, "onEvent: ", e);
                            return;
                        }
                        if(queryDocumentSnapshots != null) {
                            ArrayList<Note> notes = new ArrayList<>();
                            for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                note.setDocumentId(documentSnapshot.getId());
                                Log.d(TAG, "onEvent: note: " + note.getDocumentId());
                                notes.add(note);
                            }
                            mNotes.setValue(notes);
                        } else {
                            Log.e(TAG, "query document snapshots was null");
                        }
                    }
                });

        return mNotes;
    }
}
