package com.example.josh.virtualmum;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.josh.virtualmum.JacksHomePageCode.TimetableActivity;

import Database.UserPreference.UserPref;
import Database.VMDbHelper;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_profile);

        final UserPref userPref;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());


        userPref = db.getTopUP();


        Log.d(" UP", userPref.getId() + "\t" + userPref.getName() + "\t" + userPref.getWorkPref() + "\t" +
                    userPref.getNoDayPref() );


        db.closeDB();

        TextView tv = findViewById(R.id.NameET);
        tv.setText(userPref.getName());
        tv = findViewById(R.id.tv_name);
        tv.setText(userPref.getName());


        tv = findViewById(R.id.WTPTxt);
        tv.setText(userPref.getWorkPref());

        tv = findViewById(R.id.DOPTxt);
        tv.setText(userPref.getNoDayPref());

        FloatingActionButton fab = findViewById(R.id.EditFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VMDbHelper db;
                db = new VMDbHelper(getApplicationContext());


                db.deleteUserPref(userPref);


                db.closeDB();
                Intent myIntent = new Intent(getBaseContext(), SetUpActivity.class);
                startActivity(myIntent);

            }
        });
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
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);

        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_setting) {


            drawer.closeDrawer(GravityCompat.START);
            return true;

        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
