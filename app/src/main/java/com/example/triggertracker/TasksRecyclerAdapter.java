package com.example.triggertracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        Task task = tasks.get(position);
//        Log.d("TAG", "onBindViewHolder: reminder time: " + task.getReminderTime().toString());
        holder.taskItemName.setText(task.getName());
        if(task.getHasReminder()) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            CharSequence dateCharSeq = df.format(task.getReminderTime().toDate());
            holder.taskItemTime.setText(dateCharSeq);
        } else {
            holder.taskItemTime.setText("");
        }
//        holder.taskItemTime.setText(task.getReminderTime().toString());
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

        private ImageView taskItemNotification;
        private TextView taskItemName, taskItemTime;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskItemNotification = itemView.findViewById(R.id.task_notification);
            taskItemName = itemView.findViewById(R.id.task_name);
            taskItemTime = itemView.findViewById(R.id.task_time);
        }
    }
}
