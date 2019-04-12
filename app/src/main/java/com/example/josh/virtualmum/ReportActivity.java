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
import java.util.Arrays;
import java.util.List;

import Database.Task.Task;
import Database.Timetable.Timetable;
import Database.VMDbHelper;

public class ReportActivity extends AppCompatActivity {

    String dates[];

    //point graph for every completed task hours spent
    //2 lines, estimated hours we thought they would spend, estimated hours they thought they would spend
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_graph);

        getIntent();
        //passed in??
        long taskID = 1;
        //taskID = getIntent().getIntExtra("taskID" , 0);


        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());

        //get timetable
        List<Timetable> allTimetables = db.getFullTimetable();
        //only look at one task - assume this gets passed in??
        dates = new String[allTimetables.size()];
        float hoursCompleted[] = new float[allTimetables.size()];
        float assignedHours[] = new float[allTimetables.size()];

        int x = -1;
        String date = "";
        for(Timetable timetable : allTimetables){
            //check it is a task and not an event
            if(timetable.getEventID() == 0 && timetable.getTaskID() == taskID){
                date = timetable.getDate().substring(0, 8);
                //check if date is already there if so dont add another to dates
                if(!(Arrays.asList(dates).contains(date)) ) {
                    x++;
                    dates[x] = date;
                }
                if(timetable.getCompleted() == 1 ) {
                    hoursCompleted[x] = hoursCompleted[x] + timetable.getDuration();
                }
                assignedHours[x] = assignedHours[x] + timetable.getDuration();
            }
        }

     
        //maybe need to get context here instead of (GraphView)
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> WorkDone = new BarGraphSeries<>();
     
        BarGraphSeries<DataPoint> ExpectedWork = new BarGraphSeries<>();

        int n = 0;
        for(float i : hoursCompleted){
            n++;
            WorkDone.appendData(new DataPoint(n,i),true, hoursCompleted.length);
        }

        int m = 0;
        for(float j : assignedHours){
            n++;
            ExpectedWork.appendData(new DataPoint(m,j),true, assignedHours.length);
        }
     
         //customise
        WorkDone.setSpacing(50);
        WorkDone.setDrawValuesOnTop(true);
        WorkDone.setValuesOnTopColor(Color.BLUE);
        WorkDone.setColor(Color.BLUE);
     
        ExpectedWork.setSpacing(50);
        ExpectedWork.setDrawValuesOnTop(true);
        ExpectedWork.setValuesOnTopColor(Color.RED);
        ExpectedWork.setColor(Color.RED);

        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        //viewport.setMinimalViewport(0f, 7.5f,0f,110);
        viewport.setMinX(0);
        viewport.setMaxX(10.5);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        //this may need to be dynamic, max of 24, as only 24 hours in a day
        viewport.setMaxY(10);

        //enables scrolling as well as zooming on the graph
        viewport.setScalable(true);
        viewport.setScalableY(true);

        //labels
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Tasks");
        gridLabel.setVerticalAxisTitle("Hours");

        //legend
        WorkDone.setTitle("Hours Completed");
        WorkDone.setTitle("Total Hours Assigned");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setMargin(50);


        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX) {
                    if(value != 0 && value == (int) value && value <= dates.length ){
                        return dates[(int) value -1];
                    }else {
                        return "";
                    }
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }

        });

        //add data to graph
        graph.addSeries(WorkDone);
        graph.addSeries(ExpectedWork);
    }


}
