package com.example.triggertracker.ui.shopping_list;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.triggertracker.ShoppingItem;
import com.example.triggertracker.UserActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ShoppingListViewModel extends ViewModel {

    private String TAG = "TAG";
    private MutableLiveData<List<ShoppingItem>> mShoppingList;

    public ShoppingListViewModel() {
        mShoppingList = new MutableLiveData<>();

//        Log.d(TAG, "ShoppingListViewModel: Called");

        // TODO: fetch shopping list items
        FirebaseFirestore.getInstance()
                .collection("ShopListItems")
//                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<ShoppingItem> items = queryDocumentSnapshots.toObjects(ShoppingItem.class);
                        mShoppingList.setValue(items);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    public LiveData<List<ShoppingItem>> getShoppingItems() {
        return mShoppingList;
    }
}