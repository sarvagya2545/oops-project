package com.example.triggertracker.ui.shopping_list;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.triggertracker.ShoppingItem;
import com.example.triggertracker.UserActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingListViewModel extends ViewModel {

    private String TAG = "TAG";
    private MutableLiveData<List<ShoppingItem>> mShoppingList;

    public ShoppingListViewModel() {
        mShoppingList = new MutableLiveData<>();
    }

    public LiveData<List<ShoppingItem>> getShoppingItems() {
        FirebaseFirestore.getInstance()
                .collection("ShopListItems")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null)  {
                            Log.e(TAG, "onEvent: ", e);
                            return;
                        }
                        if(queryDocumentSnapshots != null) {
                            ArrayList<ShoppingItem> items = new ArrayList<>();
//                            List<ShoppingItem> items = queryDocumentSnapshots.toObjects(ShoppingItem.class);
                            for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                                ShoppingItem shoppingItem = documentSnapshot.toObject(ShoppingItem.class);

                                shoppingItem.setDocumentId(documentSnapshot.getId());
                                items.add(shoppingItem);
                            }

                            mShoppingList.setValue(items);
                        } else {
                            Log.e(TAG, "query document snapshots was null");
                        }
                    }
                });

        return mShoppingList;
    }
}