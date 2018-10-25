package com.example.komputer.to_dolistapplication.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.komputer.to_dolistapplication.R;
import com.example.komputer.to_dolistapplication.Model.TaskClass;

import java.util.List;

class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView taskName;
    public ImageView deleteBtn;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        taskName = itemView.findViewById(R.id.task_name_txt);
        deleteBtn = itemView.findViewById(R.id.delete_btn);
    }
}


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    public List<TaskClass> taskList;

    public RecyclerAdapter(List<TaskClass> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.task, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, final int i) {

            recyclerViewHolder.taskName.setText(taskList.get(i).getTask());

            recyclerViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(i);
                }
            });
    }

    @Override
    public int getItemCount() {
       return taskList.size();
    }

    private void deleteItem(int position){
        taskList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}

