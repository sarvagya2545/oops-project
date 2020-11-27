package com.example.triggertracker.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triggertracker.R;
import com.example.triggertracker.Task;
import com.example.triggertracker.TasksRecyclerAdapter;
import com.example.triggertracker.ui.tasks.TasksViewModel;

import java.util.List;

public class RemindersFragment extends Fragment {

    private RemindersViewModel remindersViewModel;
    private RecyclerView RemindersRecView;
    private TasksRecyclerAdapter remindersAdapter;

    private String TAG = "TAG";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remindersViewModel = new ViewModelProvider(requireActivity()).get(RemindersViewModel.class);
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);
        RemindersRecView = view.findViewById(R.id.reminder_recyclerView);

        remindersViewModel.getmReminders().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                remindersAdapter = new TasksRecyclerAdapter(requireActivity());
                remindersAdapter.setTasks(tasks);
                RemindersRecView.setAdapter(remindersAdapter);
            }
        });

        return view;
    }
}