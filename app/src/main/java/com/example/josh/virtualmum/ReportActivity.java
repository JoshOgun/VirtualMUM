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
        
        long taskID;
        
        //array for dates, amount of work done , expected amount of work done - needs to be part of 2d array
     
     //maybe need to get context here instead of (GraphView)
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> WorkDone = new BarGraphSeries<>();
     
        BarGraphSeries<DataPoint> ExpectedWork = new BarGraphSeries<>();
     
     
     //customise
        WorkDone.setSpacing(50);
        WorkDone.setDrawValuesOnTop(true);
        WorkDone.setValuesOnTopColor(Color.BLUE);
     
        ExpectedWork.setSpacing(50);
        ExpectedWork.setDrawValuesOnTop(true);
        ExpectedWork.setValuesOnTopColor(Color.RED);
     
    }


}
