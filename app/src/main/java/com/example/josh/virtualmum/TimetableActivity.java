package com.example.josh.virtualmum;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Database.Task.Task;
import Database.VMDbHelper;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class TimetableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ListView listview;
    ListView listview1;
    TextView textView;

    private String[] data = {"Mango"};
    private String[] data1 = {"Apple"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.lecture);
        listview1 = findViewById(R.id.coursework);
        textView = findViewById(R.id.textView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                    Date cur = new Date(2019 - 1900, 2, 5);

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TimetableActivity.this, android.R.layout.simple_list_item_1, data);
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(TimetableActivity.this, android.R.layout.simple_list_item_1, data1);


                        if (isSameDate(date, cur) == false) {

                            listview.setAdapter(adapter);

                            listview1.setAdapter(adapter);
                        } else if (isSameDate(date, cur) == true) {

                            listview.setAdapter(adapter1);

                            listview1.setAdapter(adapter1);
                        }
                    }

                }
        );
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());

        long task_id = db.insertTask("Coursework 6", "110219", "110318", 5, 3, 7.5, 0);

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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_progress) {

        } else if (id == R.id.nav_task) {

        } else if (id == R.id.nav_weektable) {

        } else if (id == R.id.nav_reward) {

        } else if (id == R.id.nav_calendar) {

        }else if (id == R.id.nav_highscore) {

        }
        else if (id == R.id.nav_setting) {

        }


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
