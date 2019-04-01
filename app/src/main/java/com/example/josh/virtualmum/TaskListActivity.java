package com.example.josh.virtualmum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
        u.updateEvents(getApplicationContext());
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 24; j++){
                u.printTimetable();
            }

        }
        //u.printTimetable();
        //u.updateEvents(getApplicationContext());
        //u.updateTasks(getApplicationContext());



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

    // @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
//            Intent intent = new Intent(this, TimetableActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_progress) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        } else if (id == R.id.nav_task) {

            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_weektable) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        } else if (id == R.id.nav_reward) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_highscore) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        }
        else if (id == R.id.nav_setting) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    }


