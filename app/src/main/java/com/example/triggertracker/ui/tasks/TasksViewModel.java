package com.example.triggertracker.ui.tasks;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.triggertracker.ShoppingItem;
import com.example.triggertracker.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class TasksViewModel extends ViewModel {

    private MutableLiveData<List<Task>> mTasks;
    private String TAG = "TAG";

    public TasksViewModel() {
        mTasks = new MutableLiveData<>();
    }

    public MutableLiveData<List<Task>> getTasks() {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null)  {
                            Log.e(TAG, "onEvent: ", e);
                            return;
                        }
                        if(queryDocumentSnapshots != null) {
                            List<Task> items = queryDocumentSnapshots.toObjects(Task.class);
                            mTasks.setValue(items);
                        } else {
                            Log.e(TAG, "query document snapshots was null");
                        }
                    }
                });

        return mTasks;
    }
}
