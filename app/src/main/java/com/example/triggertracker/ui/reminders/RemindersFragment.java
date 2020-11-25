package com.example.triggertracker.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.triggertracker.R;

public class RemindersFragment extends Fragment {

    private RemindersViewModel remindersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remindersViewModel =
                ViewModelProviders.of(this).get(RemindersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reminders, container, false);
        return root;
    }
}