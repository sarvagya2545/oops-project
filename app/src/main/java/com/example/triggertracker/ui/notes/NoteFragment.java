package com.example.triggertracker.ui.notes;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.triggertracker.AddNoteActivity;
import com.example.triggertracker.AddTaskItemActivity;
import com.example.triggertracker.Note;
import com.example.triggertracker.NotesRecyclerAdapter;
import com.example.triggertracker.R;
import com.example.triggertracker.Task;
import com.example.triggertracker.TasksRecyclerAdapter;
import com.example.triggertracker.ui.tasks.TasksViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteFragment extends Fragment {
    private NoteViewModel noteViewModel;
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private RecyclerView noteRecView;
    private FloatingActionButton fabAddNote;

    private String TAG = "TAG";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        final View view = inflater.inflate(R.layout.fragment_notes, container, false);
        noteRecView = view.findViewById(R.id.notes_recyclerView);

        fabAddNote = view.findViewById(R.id.fabAddNote);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                startActivity(intent);
            }
        });

        noteViewModel.getNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesRecyclerAdapter = new NotesRecyclerAdapter(requireActivity());
                notesRecyclerAdapter.setNotes(notes);
                noteRecView.setAdapter(notesRecyclerAdapter);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
