package com.example.triggertracker.ui.shopping_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.triggertracker.R;

public class ShoppingListFragment extends Fragment {

    private ShoppingListViewModel shoppingListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shoppingListViewModel =
                ViewModelProviders.of(this).get(ShoppingListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        return root;
    }
}