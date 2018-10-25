package com.example.komputer.to_dolistapplication.Activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.komputer.to_dolistapplication.Adapter.RecyclerAdapter;
import com.example.komputer.to_dolistapplication.Helper.NotificationHelper;
import com.example.komputer.to_dolistapplication.Model.TaskClass;
import com.example.komputer.to_dolistapplication.R;
import com.example.komputer.to_dolistapplication.Receiver.AlarmNotificationReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    MainActivity context = this;
    private static String FILENAME = "listTask";

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List<TaskClass> listTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStart", true);

        loadData();
        buildRecyclerView();


        if(firstStart)              //firstStart = true
        registerNotification();
    }


    private void buildRecyclerView(){

        mRecyclerView = findViewById(R.id.list_tasks);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new RecyclerAdapter(listTasks);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addItemDialog(){

        //BUILD DIALOG
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_task_alert_dialog, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();


        //ELEMENTS IN DIALOG
        final EditText editText = alertDialog.findViewById(R.id.editText);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel);
        Button btnAdd = alertDialog.findViewById(R.id.btn_add);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameTaskString = editText.getText().toString();

                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(context, "Enter the task", Toast.LENGTH_SHORT).show();
                }else{
                    addTask(nameTaskString);
                    alertDialog.dismiss();
                }
            }
        });

    }

    private void addTask(String task){

        TaskClass taskClass = new TaskClass(task);
        listTasks.add(0, taskClass);
        mAdapter.notifyDataSetChanged();

        //sendNotification();
        saveData();
    }

    private void deleteAllItems(){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete all tasks")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listTasks.clear();
                        mAdapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view
                        saveData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void checkListBeforeClear(){
        if(listTasks.size() == 0){
            Toast.makeText(context, "To-do list is empty. Add tasks", Toast.LENGTH_SHORT).show();
        }else{
            deleteAllItems();
        }
    }

    private void loadData(){

        SharedPreferences sharedPreferences = getSharedPreferences(FILENAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String mJson = sharedPreferences.getString("list task", null);
        Type type = new TypeToken<ArrayList<TaskClass>>() {}.getType();
        listTasks = gson.fromJson(mJson, type);

        if(listTasks == null){
            listTasks = new ArrayList<>();
        }
    }

    private void saveData(){

        SharedPreferences sharedPreferences = getSharedPreferences(FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String mJson = gson.toJson(listTasks);
        editor.putString("list task", mJson);
        editor.apply();
    }

    private void sendNotification(){

     NotificationHelper helperNotification = new NotificationHelper(this);

       Intent intent = new Intent(MainActivity.this, MainActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

       String text = AlarmNotificationReceiver.getNotificationText();

      //API >=26 (OREO)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

          PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
          Notification.Builder builder = helperNotification.getNotification(pendingIntent, text);
          helperNotification.getNotificationManager().notify(new Random().nextInt(), builder.build());

      //API <26
      }else{

              Notification notification = new Notification.Builder(this)
                      .setContentTitle("ToDo List")
                      .setContentText(text)
                      .setSmallIcon(R.drawable.notification_icon)
                      .setAutoCancel(true)
                      .setPriority(Notification.PRIORITY_HIGH)
                      .build();

          helperNotification.getNotificationManager().notify(new Random().nextInt(), notification);
      }
    }

    private void registerNotification(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                60 * 60 * 24 + (new Random().nextInt((7200 - 600)+ 1 + 600)),
                //,AlarmManager.INTERVAL_DAY
                pendingIntent);



       SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putBoolean("firstStart", false);
       editor.apply();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        Drawable addIcon = menu.getItem(1).getIcon();
        Drawable deleteIcon = menu.getItem(0).getIcon();

        addIcon.mutate();
        addIcon.setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);

        deleteIcon.mutate();
        deleteIcon.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_item:
                addItemDialog();
                break;
            case R.id.delete_item:
                checkListBeforeClear();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }
}