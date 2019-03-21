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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Database.AllocationAlgorithm.User;
import Database.Event.Event;
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

        FloatingActionButton reloadFab = findViewById(R.id.reloadfab);
        reloadFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskList.clear();
                tAdapter.notifyDataSetChanged();
                prepareTaskData();
                Toast.makeText(getApplicationContext(), "Tasks Refreshed!", Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(tAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Task task = taskList.get(position);
                Toast.makeText(getApplicationContext(), task.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                int taskId = task.getId();
                Intent intent = new Intent(TaskListActivity.this, EditTaskActivity.class);
                intent.putExtra("TASK", Integer.toString(taskId));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareTaskData();

        User u = new User();
        //u.printTimetable();
        //u.updateEvents(getApplicationContext());
        //u.updateTasks(getApplicationContext());

//        // ADDING A BUNCH OF EVENTS
//        VMDbHelper db;
//        db = new VMDbHelper(getApplicationContext());
//
////        long event_id = db.insertEvent("Lecture 1", "180320191415", "180320191515", "1E.39");
////         event_id = db.insertEvent("Lecture 2", "190320191115", "190320191315", "1E.39");
////         event_id = db.insertEvent("Lecture 3", "190320191415", "190320191615", "1E.39");
////         event_id = db.insertEvent("Lecture 4", "200320191415", "200320191415", "1E.39");
////         event_id = db.insertEvent("Lecture 5", "200320191115", "200320191415", "1E.39");
////         event_id = db.insertEvent("Lecture 6", "210320191015", "210320191215", "1E.39");
////         event_id = db.insertEvent("Lecture 7", "210320191415", "210320191615", "1E.39");
////         event_id = db.insertEvent("Lecture 8", "220320190915", "220320191015", "1E.39");
////         event_id = db.insertEvent("Lecture 9", "220320191115", "220320191315", "1E.39");
////         event_id = db.insertEvent("Lecture 10", "220320191315", "220320191715", "1E.39");
//
//        List<Event> allEvents = db.getAllEvents();
//        for (Event event : allEvents) {
//            event.setLocation("1W.39");
//            db.updateEvent(event);
//            Log.d(" Events", event.getId() + "\t" + event.getName() + "\t" + event.getStartDate()  + "\t" +
//                    event.getEndDate() + "\t" + event.getLocation());
//        }
//
//        db.closeDB();

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
