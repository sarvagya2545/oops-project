package com.example.triggertracker.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.triggertracker.ExtraFeatureActivity;
import com.example.triggertracker.R;
import com.example.triggertracker.ui.shopping_list.ShoppingListViewModel;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView vNoOfReminders, vNoOfTasks, vNoOfShoppingItems;
    private ImageButton extraFeature;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);


        extraFeature = root.findViewById(R.id.btnExtraFeature);
        extraFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ExtraFeatureActivity.class);
                startActivity(intent);
            }
        });

        vNoOfReminders = root.findViewById(R.id.noOfReminders);
        vNoOfShoppingItems = root.findViewById(R.id.noOfShoppingItems);
        vNoOfTasks = root.findViewById(R.id.noOfTasks);

        homeViewModel.getNoOfTasks().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                vNoOfTasks.setText(s);
                vNoOfTasks.invalidate();
            }
        });

        homeViewModel.getNoOfReminders().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                vNoOfReminders.setText(s);
                vNoOfReminders.invalidate();
            }
        });

        homeViewModel.getNoOfShoppingItems().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                vNoOfShoppingItems.setText(s);
                vNoOfShoppingItems.invalidate();
            }
        });

        return root;
    }
}