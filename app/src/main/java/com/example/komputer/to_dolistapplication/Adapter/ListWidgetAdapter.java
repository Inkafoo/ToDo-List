package com.example.komputer.to_dolistapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.komputer.to_dolistapplication.Model.TaskClass;
import com.example.komputer.to_dolistapplication.R;

import java.util.List;

public class ListWidgetAdapter extends ArrayAdapter<TaskClass> {


    private List<TaskClass> taskList;

    public ListWidgetAdapter(@NonNull Context context, int resource, List<TaskClass> taskList) {
        super(context, resource);
        this.taskList = taskList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TaskClass item = taskList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.widget_list_items, parent, false);
        }

        TextView itemTextView = convertView.findViewById(R.id.textView_list);
        itemTextView.setText(item.getTask());

        return convertView;
    }


    @Override
    public int getCount() {
        return taskList.size();
    }


}
