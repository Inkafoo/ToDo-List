package com.example.komputer.to_dolistapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
    public List selectedTask;
    public Context context;


    public RecyclerAdapter(List<TaskClass> taskList,  List selectedTask, Context context) {
        this.taskList = taskList;
        this.selectedTask = selectedTask;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.task, viewGroup, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }


    @Override
    public void onBindViewHolder(@NonNull final  RecyclerViewHolder recyclerViewHolder, final int i) {

            final TaskClass myTask = taskList.get(i);


            recyclerViewHolder.taskName.setText(myTask.getTask());

            recyclerViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(i, myTask);
                }
            });



            if(!myTask.isSelected()){
                recyclerViewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGreen));
            }else{
                recyclerViewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.lightRed));
            }



            recyclerViewHolder.taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskToSelected(myTask, recyclerViewHolder);
            }
        });

    }



    private void addTaskToSelected(TaskClass myTask, RecyclerViewHolder recyclerViewHolder){

        myTask.setSelected(!myTask.isSelected());
        recyclerViewHolder.itemView.setBackgroundColor(myTask.isSelected() ? ContextCompat.getColor(context, R.color.lightRed) :
                                                                             ContextCompat.getColor(context, R.color.lightGreen));
        if(myTask.isSelected()){
            selectedTask.add(myTask);
        }else{
            selectedTask.remove(myTask);
        }

    }


    private void deleteItem(int position, TaskClass myTask){

        if(selectedTask.contains(myTask)){
            selectedTask.remove(myTask);
        }

        taskList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();

    }
}

