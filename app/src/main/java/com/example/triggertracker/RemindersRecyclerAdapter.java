package com.example.triggertracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RemindersRecyclerAdapter extends RecyclerView.Adapter<RemindersRecyclerAdapter.ReminderViewHolder> {
    private List<Task> tasks = new ArrayList<Task>();
    private Context context;

    public RemindersRecyclerAdapter(Context context) { this.context = context; }

    @NonNull
    @Override
    public RemindersRecyclerAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent,false);
        RemindersRecyclerAdapter.ReminderViewHolder holder = new RemindersRecyclerAdapter.ReminderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersRecyclerAdapter.ReminderViewHolder holder, final int position) {
        final Task taskItem = tasks.get(position);

        holder.taskItemName.setText(taskItem.getName());
        if(taskItem.getHasReminder()) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            CharSequence dateCharSeq = df.format(taskItem.getReminderTime().toDate());
            holder.taskItemTime.setText(dateCharSeq);
        } else {
            holder.taskItemTime.setText("");
            holder.taskItemNotification.setVisibility(View.INVISIBLE);
        }

        holder.taskItemDelete.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder{

        private ImageView taskItemNotification, taskItemDelete;
        private TextView taskItemName, taskItemTime;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            taskItemDelete = itemView.findViewById(R.id.task_delete);
            taskItemNotification = itemView.findViewById(R.id.task_notification);
            taskItemName = itemView.findViewById(R.id.task_name);
            taskItemTime = itemView.findViewById(R.id.task_time);
        }
    }
}
