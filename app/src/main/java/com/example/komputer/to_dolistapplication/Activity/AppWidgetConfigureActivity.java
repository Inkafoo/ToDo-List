package com.example.komputer.to_dolistapplication.Activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.komputer.to_dolistapplication.Adapter.ListWidgetAdapter;
import com.example.komputer.to_dolistapplication.Model.TaskClass;
import com.example.komputer.to_dolistapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.example.komputer.to_dolistapplication.Activity.MainActivity.FILENAME;
import static com.example.komputer.to_dolistapplication.Activity.MainActivity.SPKEY;


public class AppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "prefs_name_package";
    private static final String PREF_PREFIX_KEY = "prefs_key";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    ListView myListView;
    List<TaskClass> taskList;
    ListWidgetAdapter listAdapter;


    public AppWidgetConfigureActivity() {
        super();
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.app_widget_configure);

        loadData();

        //Find a list to display an items
        myListView = findViewById(R.id.list_view_widget);

        listAdapter = new ListWidgetAdapter(getApplicationContext(), R.id.list_view_widget, taskList);
        myListView.setAdapter(listAdapter);

        if(taskList.isEmpty()){
            Toasty.info(getApplicationContext(), "Task list is empty", Toast.LENGTH_SHORT).show();
        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addWidget(position);
            }
        });


        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }


    }


    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }


    public static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }


    public static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }


    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(FILENAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String mJson = sharedPreferences.getString(SPKEY, null);
        Type type = new TypeToken<ArrayList<TaskClass>>() {}.getType();
        taskList = gson.fromJson(mJson, type);

        if(taskList == null){
            taskList = new ArrayList<>();
        }
    }


    private void addWidget(int position) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String buttonText = taskList.get(position).getTask();

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.app_widget);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        views.setCharSequence(R.id.appwidget_text, "setText", buttonText);

        saveTitlePref(getApplicationContext(), mAppWidgetId, buttonText);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);


        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

