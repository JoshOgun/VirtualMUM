package com.example.josh.virtualmum.JacksHomePageCode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Database.Task.Task;
import Database.VMDbHelper;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

import com.example.josh.virtualmum.EventListActivity;
import com.example.josh.virtualmum.R;
import com.example.josh.virtualmum.JacksHomePageCode.TimetableView.Schedule;
import com.example.josh.virtualmum.JacksHomePageCode.TimetableView.TimetableView;
import com.example.josh.virtualmum.TaskListActivity;

public class TimetableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TimetableView timetable;

    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_timetable);
        this.context = this;
        timetable = findViewById(R.id.timetable);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditActivity.class);
                i.putExtra("mde",REQUEST_ADD);
                startActivityForResult(i,REQUEST_ADD);

            }
        });
        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                Intent i = new Intent(context, EditActivity.class);
                i.putExtra("mode",REQUEST_EDIT);
                i.putExtra("idx", idx);
                i.putExtra("schedules", schedules);
                startActivityForResult(i,REQUEST_EDIT);
            }
        });

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);


        final HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(
                new HorizontalCalendarListener() {
                    Date cur = new Date(2019 - 1900, 2, 22);

                    @Override
                    public void onDateSelected(Date date, int position) {
                        // Something Josh is working on
//                    if(counter == 0){
//                        originalPosition = position;
//                        counter++;
//                    }
//                    if(position > originalPosition + 5 ){
//                        date = Calendar.getInstance().getTime();
//                        position = originalPosition;
//                    }
                        loadSavedData();
                       if (isSameDate(cur,date)){
                           timetable.removeAll();

                 }

                    }

                }
        );
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());

//        long task_id = db.insertTask("Coursework 6", "110219", "110318", 5, 3, 7.5, 0);

        List<Task> allTasks = db.getAllTasks();
        for (Task task : allTasks) {
            Log.d("Task Name", task.getName());
        }

        db.closeDB();

    }

    public static boolean isSameDate(Date date1, Date date2) {

        if (date1.getYear() == date2.getYear()) {
            if (date1.getMonth() == date2.getMonth()) {
                if (date1.getDate() == date2.getDate()) {
                    return true;
                }
            }
        }

        return false;


    }
   // @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_progress) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        } else if (id == R.id.nav_task) {
            Intent intent = new Intent(this, TaskListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_weektable) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        } else if (id == R.id.nav_reward) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_highscore) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        }
        else if (id == R.id.nav_setting) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ADD:
                if(resultCode == EditActivity.RESULT_OK_ADD){
                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");
                    timetable.add(item);
                    saveByPreference(timetable.createSaveData());
                }
                break;
            case REQUEST_EDIT:
                /** Edit -> Submit */
                if(resultCode == EditActivity.RESULT_OK_EDIT){
                    int idx = data.getIntExtra("idx",-1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");
                    timetable.edit(idx,item);
                    saveByPreference(timetable.createSaveData());

                }
                /** Edit -> Delete */
                else if(resultCode == EditActivity.RESULT_OK_DELETE){
                    int idx = data.getIntExtra("idx",-1);
                    timetable.remove(idx);
                    saveByPreference(timetable.createSaveData());

                }
                break;
        }
    }

    /** save timetableView's data to SharedPreferences in json format */
    private void saveByPreference(String data){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("timetable_demo",data);
        editor.commit();
        Toast.makeText(this,"saved!",Toast.LENGTH_SHORT).show();
    }

    /** get json data from SharedPreferences and then restore the timetable */
    private void loadSavedData(){
        timetable.removeAll();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        String savedData = mPref.getString("timetable_demo","");
        if(savedData == null || savedData.equals("")) return;
        timetable.load(savedData);
        Toast.makeText(this,"loaded!",Toast.LENGTH_SHORT).show();
    }
}
