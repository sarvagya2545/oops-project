package com.example.triggertracker.ui.shopping_list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triggertracker.AddShoppingItemActivity;
import com.example.triggertracker.R;
import com.example.triggertracker.ShoppingItem;
import com.example.triggertracker.ShoppingItemsRecyclerAdapter;
import com.example.triggertracker.UserActivity;
import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class ShoppingListFragment extends Fragment {

    private ShoppingListViewModel shoppingListViewModel;
    private ShoppingItemsRecyclerAdapter shoppingListAdapter;
    private RecyclerView shoppingRecyclerView;
    private FloatingActionButton fabAddShoppingItem;

    private String TAG = "TAG";

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
            public void onChanged(List<ShoppingItem> shoppingList) {
                shoppingListAdapter = new ShoppingItemsRecyclerAdapter(getContext());
                shoppingListAdapter.setShoppingItems(shoppingList);
                shoppingRecyclerView.setAdapter(shoppingListAdapter);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}