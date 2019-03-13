package com.example.josh.virtualmum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import Database.Task.Task;
import Database.VMDbHelper;
import DividerDetails.MyDividerItemDecoration;

public class TaskListActivity extends AppCompatActivity {

    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter tAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), AddTaskActivity.class);
                startActivity(myIntent);
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.taskRV);

        tAdapter = new TaskAdapter(taskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));

        recyclerView.setAdapter(tAdapter);

        prepareTaskData();

    }

    private void prepareTaskData() {


        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());

        List<Task> allTasks = db.getAllTasks();
        for (Task task : allTasks) {
            taskList.add(task);
        }

        db.closeDB();

        tAdapter.notifyDataSetChanged();
    }

}
