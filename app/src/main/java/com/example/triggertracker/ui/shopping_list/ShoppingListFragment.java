package com.example.triggertracker.ui.shopping_list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triggertracker.AddShoppingItemActivity;
import com.example.triggertracker.R;
import com.example.triggertracker.ShoppingItem;
import com.example.triggertracker.ShoppingItemsRecyclerAdapter;
import com.example.triggertracker.UserActivity;
import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ShoppingListFragment extends Fragment {

    private ShoppingListViewModel shoppingListViewModel;
    private ShoppingItemsRecyclerAdapter shoppingListAdapter;
    private RecyclerView shoppingRecyclerView;
    private FloatingActionButton fabAddShoppingItem;

    private String TAG = "TAG";

    ShoppingItem deletedShoppingItem = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shoppingListViewModel = new ViewModelProvider(getActivity()).get(ShoppingListViewModel.class);
        final View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        shoppingRecyclerView = view.findViewById(R.id.shopping_list_recyclerView);
        fabAddShoppingItem = view.findViewById(R.id.fabAddShoppingItem);
        fabAddShoppingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddShoppingItemActivity.class);
                startActivity(intent);
            }
        });

        shoppingListViewModel.getShoppingItems().observe(getViewLifecycleOwner(), new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(final List<ShoppingItem> shoppingList) {
                shoppingListAdapter = new ShoppingItemsRecyclerAdapter(getContext());
                shoppingListAdapter.setShoppingItems(shoppingList);
                shoppingRecyclerView.setAdapter(shoppingListAdapter);
                ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        final int position = viewHolder.getAdapterPosition();
                        ShoppingItem shoppingItem = shoppingList.get(position);
                        String docId = shoppingItem.getDocumentId();

                        switch(direction) {
                            case ItemTouchHelper.LEFT:
                                Toast.makeText(getContext(), "LEFT", Toast.LENGTH_SHORT).show();
                                break;
                            case ItemTouchHelper.RIGHT:
                                deletedShoppingItem = shoppingItem;
                                FirebaseFirestore.getInstance()
                                        .collection("ShopListItems")
                                        .document(docId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                deleteFromUI(shoppingListAdapter, position);
                                                shoppingListAdapter.notifyItemRemoved(position);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: ", e);
                                            }
                                        });

                                break;
                        }
                    }

                    public void deleteFromUI(final ShoppingItemsRecyclerAdapter shoppingListAdapter, final int position) {
                        shoppingListAdapter.notifyItemRemoved(position);
                        Snackbar.make(shoppingRecyclerView, "Item deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        restoreItem(position);
                                    }
                                }).show();
                    }

                    private void restoreItem(final int position) {
                        FirebaseFirestore.getInstance()
                                .collection("ShopListItems")
                                .add(deletedShoppingItem)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()) {
                                            Log.d(TAG, "onComplete: Added item back!");
                                            shoppingListAdapter.notifyItemInserted(position);
                                        } else {
                                            Toast.makeText(getContext(), "Restore failed! Create new item.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorDanger))
                                .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                                .create()
                                .decorate();

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                };

                ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
                helper.attachToRecyclerView(shoppingRecyclerView);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}