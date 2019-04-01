package com.example.josh.virtualmum;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import Database.Task.Task;
import Database.Timetable.Timetable;
import Database.VMDbHelper;

public class ReportActivity extends AppCompatActivity {

 
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_graph);


        //passed in??
        long taskID;

        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());
        //array for dates, amount of work done , expected amount of work done - needs to be part of 2d array

        //get timetable
        List<Timetable> allTimetables = db.getAllTimetables();
        //only look at one task - assume this gets passed in??
        String dates[] = new String[allTimetables.size()];
        float hoursCompleted[] = new float[allTimetables.size()];
        float assignedHours[] = new float[allTimetables.size()];
        //count how much work has been done and how much should have been done on each given day
        //so check which days this task was on
        //get the amount it should have been done
        //get the amount that was actually done

        int x = 0;
        for(Timetable timetable : allTimetables){
            //check it is a task and not an event
            if(timetable.getTaskID() != -1 && timetable.getTaskID() == taskID){
                if(timetable.getCompleted() == 1 ) {
                    hoursCompleted[x] = hoursCompleted[x] + timetable.getDuration()
                }
                assignedHours[x] = assignedHours[x] + timetable.getDuration();
                dates[x] = timetable.getDate()
            }
        }

     
        //maybe need to get context here instead of (GraphView)
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> WorkDone = new BarGraphSeries<>();
     
        BarGraphSeries<DataPoint> ExpectedWork = new BarGraphSeries<>();

        int n = 0;
        for(float i : hoursCompleted){
            n++;
            WorkDone.appendData(new DataPoint(n,i),true,hoursCompleted.length);
        }

        int m = 0;
        for(float j : assignedHours){
            n++;
            ExpectedWork.appendData(new DataPoint(m,j),true,assignedHours.length);
        }
     
         //customise
        WorkDone.setSpacing(50);
        WorkDone.setDrawValuesOnTop(true);
        WorkDone.setValuesOnTopColor(Color.BLUE);
     
        ExpectedWork.setSpacing(50);
        ExpectedWork.setDrawValuesOnTop(true);
        ExpectedWork.setValuesOnTopColor(Color.RED);
     
    }


}
