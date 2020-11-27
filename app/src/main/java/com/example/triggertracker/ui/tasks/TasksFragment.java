package com.example.triggertracker.ui.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triggertracker.AddTaskItemActivity;
import com.example.triggertracker.R;
import com.example.triggertracker.ShoppingItemsRecyclerAdapter;
import com.example.triggertracker.Task;
import com.example.triggertracker.TasksRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;
    private TasksRecyclerAdapter tasksRecyclerAdapter;
    private RecyclerView taskRecView;
    private FloatingActionButton fabAddTask;

    private String TAG = "TAG";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        final View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        taskRecView = view.findViewById(R.id.task_list_recyclerView);

        fabAddTask = view.findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTaskItemActivity.class);
                startActivity(intent);
            }
        });

        tasksViewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                tasksRecyclerAdapter = new TasksRecyclerAdapter(requireActivity());
                tasksRecyclerAdapter.setTasks(tasks);
                taskRecView.setAdapter(tasksRecyclerAdapter);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
