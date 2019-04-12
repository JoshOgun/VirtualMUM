package com.example.josh.virtualmum;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.josh.virtualmum.JacksHomePageCode.TimetableActivity;
import com.example.josh.virtualmum.JacksHomePageCode.WeekTable;

import java.text.Format;

public class GCAPIActivity extends AppCompatActivity implements View.OnClickListener,  NavigationView.OnNavigationItemSelectedListener{

    private Cursor mCursor = null;
    private static final String[] COLS = new String[]
            { CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_gcapi);





        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GCAPIActivity.this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    1);        }
        else {

            mCursor = getContentResolver().query(


                    CalendarContract.Events.CONTENT_URI, COLS, null, null, null);
            mCursor.moveToFirst();


            Button b = (Button) findViewById(R.id.next);


            b.setOnClickListener(this);
            b = (Button) findViewById(R.id.previous);


            b.setOnClickListener(this);
            onClick(findViewById(R.id.previous));
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        TextView tv = (TextView)findViewById(R.id.data);


        String title = "N/A";


        Long start = 0L;


        switch(v.getId()) {
            case R.id.next:
                if(!mCursor.isLast()) mCursor.moveToNext();
                break;
            case R.id.previous:
                if(!mCursor.isFirst()) mCursor.moveToPrevious();
                break;
        }


        Format df = DateFormat.getDateFormat(this);
        Format tf = DateFormat.getTimeFormat(this);
        try {
            title = mCursor.getString(0);


            start = mCursor.getLong(1);


        } catch (Exception e) {
//ignore
        }
        tv.setText(title+" on "+df.format(start)+" at "+tf.format(start));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(GCAPIActivity.this, "Permission denied to read your Calendar", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

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
            Intent intent = new Intent(this, WeekTable.class);
            startActivity(intent);

        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_import) {
            drawer.closeDrawer(GravityCompat.START);
            return true;



        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
