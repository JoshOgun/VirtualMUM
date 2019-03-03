package com.example.josh.virtualmum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Database.Task.Task;
import Database.VMDbHelper;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class TimetableActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        textView = findViewById(R.id.textView);



        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);


        final HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .datesNumberOnScreen(5)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {

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
                textView.setText("Selected Date: " + date);
            }


        });

         // CODE FOR DATABASE TESTING
        // Database Helper
        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());

        long task_id = db.insertTask("Coursework 6", "110219", "110318", 5, 3, 7.5, 0);

        List<Task> allTasks = db.getAllTasks();
        for (Task task : allTasks) {
            Log.d("Task Name", task.getName());
        }

        db.closeDB();

    }

}
