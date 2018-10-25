package com.example.komputer.to_dolistapplication.Model;

public class TaskClass {

    public String task;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public  TaskClass(String name){
       task = name;
   }
}
