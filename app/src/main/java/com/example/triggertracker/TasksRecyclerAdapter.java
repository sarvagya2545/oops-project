package com.example.triggertracker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<Task>();
    private Context context;

    public TasksRecyclerAdapter(Context context) { this.context = context; }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent,false);
        TaskViewHolder holder = new TaskViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, final int position) {
        final Task taskItem = tasks.get(position);
        final String taskId = taskItem.getDocumentId();

        holder.taskItemName.setText(taskItem.getName());
        if(taskItem.getHasReminder()) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            CharSequence dateCharSeq = df.format(taskItem.getReminderTime().toDate());
            holder.taskItemTime.setText(dateCharSeq);
        } else {
            holder.taskItemTime.setText("");
            holder.taskItemNotification.setVisibility(View.INVISIBLE);
        }

        holder.taskItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("tasks")
                        .document(taskId)
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Something went wrong, check your connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{

        private ImageView taskItemNotification, taskItemDelete;
        private TextView taskItemName, taskItemTime;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskItemDelete = itemView.findViewById(R.id.task_delete);
            taskItemNotification = itemView.findViewById(R.id.task_notification);
            taskItemName = itemView.findViewById(R.id.task_name);
            taskItemTime = itemView.findViewById(R.id.task_time);
        }
    }
}
