package com.example.triggertracker.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.triggertracker.ShoppingItem;
import com.example.triggertracker.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mNoOfReminders, mNoOfTasks, mNoOfShoppingItems;
    private String TAG = "TAG";

    public MutableLiveData<String> getNoOfReminders() {
        return mNoOfReminders;
    }

    public MutableLiveData<String> getNoOfTasks() {
        return mNoOfTasks;
    }

    public MutableLiveData<String> getNoOfShoppingItems() {
        return mNoOfShoppingItems;
    }

    public HomeViewModel() {
        mNoOfReminders = new MutableLiveData<>();
        mNoOfShoppingItems = new MutableLiveData<>();
        mNoOfTasks = new MutableLiveData<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            FirebaseFirestore.getInstance()
                    .collection("ShopListItems")
                    .whereEqualTo("userId", user.getUid())
                    .whereEqualTo("bought", false)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if(e!=null)  {
                                Log.e(TAG, "onEvent: ", e);
                                return;
                            }
                            if(queryDocumentSnapshots != null) {
                                List<ShoppingItem> items = queryDocumentSnapshots.toObjects(ShoppingItem.class);
                                mNoOfShoppingItems.setValue(String.valueOf(items.size()));
                            } else {
                                Log.e(TAG, "query document snapshots was null");
                            }
                        }
                    });

            FirebaseFirestore.getInstance()
                    .collection("tasks")
                    .whereEqualTo("userId", user.getUid())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if(e!=null)  {
                                Log.e(TAG, "onEvent: ", e);
                                return;
                            }
                            if(queryDocumentSnapshots != null) {
                                List<Task> tasks = queryDocumentSnapshots.toObjects(Task.class);
                                mNoOfTasks.setValue(String.valueOf(tasks.size()));

                                ArrayList<Task> reminders = new ArrayList<>();
                                for(Task task: tasks) {
                                    if(task.getHasReminder()) {
                                        reminders.add(task);
                                    }
                                }
                                mNoOfReminders.setValue(String.valueOf(reminders.size()));
                            } else {
                                Log.e(TAG, "query document snapshots was null");
                            }
                        }
                    });
        }
    }

}