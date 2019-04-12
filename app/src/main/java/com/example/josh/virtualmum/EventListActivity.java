package com.example.josh.virtualmum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.josh.virtualmum.JacksHomePageCode.TimetableActivity;

import java.util.ArrayList;
import java.util.List;

import Database.Event.Event;
import Database.VMDbHelper;
import DividerDetails.MyDividerItemDecoration;

public class EventListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventAdapter eAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_event_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), AddEventActivity.class);
                startActivity(myIntent);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.eventRV);

        eAdapter = new EventAdapter(eventList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));

//        FloatingActionButton reloadFab = findViewById(R.id.reloadFab);
//        reloadFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                eventList.clear();
//                eAdapter.notifyDataSetChanged();
//                prepareEventData();
//                Toast.makeText(getApplicationContext(), "Events Refreshed!", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        recyclerView.setAdapter(eAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Event event = eventList.get(position);
                Toast.makeText(getApplicationContext(), event.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                int eventId = event.getId();
                Intent intent = new Intent(EventListActivity.this, EditEventActivity.class);
                intent.putExtra("EVENT", Integer.toString(eventId));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareEventData();
    }


    // @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, TimetableActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_progress) {
            Intent intent = new Intent(this, ProgressActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_task) {
            Intent intent = new Intent(this, TaskListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_weektable) {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_events) {
            drawer.closeDrawer(GravityCompat.START);
            return true;

        }
        else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void prepareEventData() {


        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());



        List<Event> allEvents = db.getAllEvents();
        for (Event event : allEvents) {
            eventList.add(event);
        }

        db.closeDB();

        eAdapter.notifyDataSetChanged();
    }

}
