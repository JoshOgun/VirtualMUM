package com.example.josh.virtualmum;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import Database.Progress.Progress;
import Database.Task.Task;
import Database.VMDbHelper;

public class ProgressActivity extends AppCompatActivity {


    //progress for each activity
    double progress[][];

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_graph);
        /*
        testing();
        //get data on each task progress

        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());
        int taskCount = db.getTasksCount();
        List<Task> allTasks = db.getAllTasks();

        //display every task or just ones not completed yet?
        //ideally it would be progress for each individual task

        progress = new double[taskCount][2];

        for (Task task : allTasks) {
            progress[task.getId()][0] = task.getEstimatedHours();
            Progress taskProgress = db.getProgress(task.getId());
            progress[task.getId()][1] = taskProgress.getHoursSpent();
            progress[task.getId()][2] = taskProgress.getProgress();
        }
        */


        //maybe need to get context here instead of (GraphView)
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        series.appendData(new DataPoint(1, 4), true, 1);
        /*
        for (Task task : allTasks) {
            series.appendData(new DataPoint(task.getId(), progress[task.getId()][2]), true, taskCount);
        }
        */



        //everything below customises the graph
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLUE);

        Viewport viewport = graph.getViewport();
        viewport.setMaxX(7.5);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(110);
        //enables scrolling as well as scaling on the graph
        viewport.setScalable(true);
        viewport.setScalableY(true);

        //label axis
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Tasks");
        gridLabel.setVerticalAxisTitle("Completion percent");


        //changes the name for the data along the x axis
        //need to get this to be the actual name of the task rather than task 1 etc.
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    if(value == 0) {
                        return "";
                    } else {
                        return "task" + super.formatLabel(value, isValueX);
                    }
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });


        graph.addSeries(series);

    }

    public void testing(){
        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());

        db.insertTask("courswork 2", "110219","110318", 5, 3, 7.5, 0);
        db.insertProgress(80, 5);
        db.close();
    }


}
