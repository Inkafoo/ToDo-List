package com.example.komputer.to_dolistapplication.Model;

public class TaskClass {

    public String task;
    private boolean isSelected = false;


    public  TaskClass(String name){
        task = name;
    }


    public void setTask(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }


    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
