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

import Database.Progress.Progress;
import Database.Task.Task;
import Database.VMDbHelper;

public class ProgressActivity extends AppCompatActivity {


    //progress for each activity
    float progress[];

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_graph);

        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());

        db.insertTask("coursework 2", "110219","110318", 5, 3, 7.5, 0);
        db.insertProgress(80, 5);

        int taskCount = db.getTasksCount();
        List<Task> allTasks = db.getAllTasks();
        List<Task> ableTasks = new ArrayList<>();
        //display every task or just ones not completed yet?
        //ideally it would be progress for each individual task

        for (Task task : allTasks) {
            //filter out tasks that are completed
            //could be filtered so that if is completed but not handed in it still shows??
            if(task.getCompleted() == 0){
                ableTasks.add(task);
            }

        }
        progress = new float[ableTasks.size()];

        int m = 0;
        for(Task task : ableTasks){
            //db.getProgress(task.getId());
            Progress taskProgress = db.getProgress(task.getId());
            progress[m] = taskProgress.getProgress();
            //Log.d("m value", "value: " + m);
            m++;
        }

        db.close();

        //maybe need to get context here instead of (GraphView)
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();

        //seems like there is a bug on the graph when there is only one datapoint, it messes up the graph a little bit
        //series.appendData(new DataPoint(1, 4), true, 2);

        int n = 0;
        for(float y : progress){
            n++;
            series.appendData(new DataPoint(n, y) , true, progress.length);
        }
        //Log.d("Progress length", "value: " + progress.length);

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


}
