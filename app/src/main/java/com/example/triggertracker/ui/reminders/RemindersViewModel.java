package com.example.triggertracker.ui.reminders;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.triggertracker.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RemindersViewModel extends ViewModel {

    private MutableLiveData<List<Task>> mReminders;
    private String TAG = "TAG";

    public RemindersViewModel() {
        mReminders = new MutableLiveData<>();
    }

    public MutableLiveData<List<Task>> getmReminders() {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("hasReminder", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null)  {
                            Log.e(TAG, "onEvent: ", e);
                            return;
                        }
                        if(queryDocumentSnapshots != null) {
                            List<Task> items = queryDocumentSnapshots.toObjects(Task.class);
                            mReminders.setValue(items);
                        } else {
                            Log.e(TAG, "query document snapshots was null");
                        }
                    }
                });

        return mReminders;
    }
}