package com.example.josh.virtualmum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.*;
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
        //get data on each task progress
        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());
        int taskCount = db.getTasksCount();
        List<Task> allTasks = db.getAllTasks();

        //display every task or just ones not completed yet?
        //ideally it would be progress for each individual task

        progress = new double[taskCount][2];
        /*
        for (int x = 0; x<taskCount;x++){
            progress[x][0] =
        }
        */
        for (Task task : allTasks) {
            progress[task.getId()][0] = task.getEstimatedHours();
            Progress taskProgress = db.getProgress(task.getId());
            progress[task.getId()][1] = taskProgress.getHoursSpent();
            progress[task.getId()][2] = taskProgress.getProgress();
        }
        DataPoint points[] = new DataPoint[taskCount];
        for (Task task : allTasks) {
            points[task.getId()] = new DataPoint(task.getId(), progress[task.getId()][2]);
        }

        //maybe need to get context here instead of (GraphView)
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        graph.addSeries(series);

    }


}
