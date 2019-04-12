package com.example.josh.virtualmum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.josh.virtualmum.JacksHomePageCode.TimetableActivity;
import com.example.josh.virtualmum.JacksHomePageCode.WeekTable;
import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Database.Progress.Progress;
import Database.Task.Task;
import Database.VMDbHelper;

public class ProgressActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    String start;
    String end;
    String cur;
    Date startDate;
    Date endDate;
    Date curDate = new Date();
    int timeLength;
    int timeLengthCur;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_progress);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

        /*Intent intent = new Intent(getBaseContext(), SignoutActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", sessionId);
        startActivity(intent);
        */

        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());
        //db.insertReport(0,30);

        long taskID = 1;
        //getIntent();
        //getIntent().getLongExtra(taskID, 0);
        start = "090420190000";
        end = "150420190000";
        cur = "120420190000";

        //start = db.getTask(taskID).getStartDate().substring(0,8);
        //end = db.getTask(taskID).getDueDate().substring(0,8);
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
            curDate = sdf.parse(cur);
        } catch(ParseException e){
            e.printStackTrace();
        }

        timeLength = (int) (TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS));
       // timeLength = (int)( (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        timeLengthCur = (int)(TimeUnit.DAYS.convert(curDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS));
        double theirEstimated = 22;
        double ourEstimated = 20;
        double actualSpent = 8;

        //double theirEstimated = db.getTask(taskID).getEstimatedHours();
       // float ourEstimated = db.getReport(taskID).getEstimatedHours();
        //double actualSpent = db.getProgress(taskID).getHoursSpent();

        db.close();

        //maybe need to get context here instead of (GraphView)
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> theirEst = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> ourEst = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> actual = new LineGraphSeries<>();

        //seems like there is a bug on the graph when there is only one datapoint, it messes up the graph a little bit
        //series.appendData(new DataPoint(1, 4), true, 2);

        theirEst.appendData(new DataPoint(0,0),true, 2);
        ourEst.appendData(new DataPoint(0,0),true, 2);
        actual.appendData(new DataPoint(0,0),true, 2);

        if(timeLengthCur != 0){
            actual.appendData(new DataPoint(timeLengthCur, actualSpent), true, 2);
        } else {
            actual.appendData(new DataPoint(0.1,actualSpent), true, 2);
        }

        if(timeLength != 0) {
            theirEst.appendData(new DataPoint(timeLength, theirEstimated), true, 2);
            ourEst.appendData(new DataPoint(timeLength, ourEstimated), true, 2);
        }


        theirEst.setColor(Color.GREEN);
        ourEst.setColor(Color.RED);
        actual.setColor(Color.BLUE);


        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(timeLength + 3);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(Math.max(Math.max(theirEstimated,ourEstimated),actualSpent) + 5);
        //enables scrolling as well as zooming on the graph
        viewport.setScalable(true);
        viewport.setScalableY(true);

        //label axis
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Date");
        gridLabel.setVerticalAxisTitle("Hours");

        //legend
        theirEst.setTitle("Your Total Estimated Hours");
        ourEst.setTitle("Our Estimated Hours");
        actual.setTitle("Hours spent currently");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setMargin(50);
        
        //make list to pass in task names
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX) {
                    if(value == 0 ){
                        return convertString(start);
                    } else if(value == timeLength){
                        return convertString(end);
                    } else {
                        return "";
                    }
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }

        });


        graph.addSeries(theirEst);
        graph.addSeries(ourEst);
        graph.addSeries(actual);

        theirEst.setDrawDataPoints(true);
        ourEst.setDrawDataPoints(true);
        actual.setDrawDataPoints(true);

        theirEst.setThickness(10);
        ourEst.setThickness(10);
        actual.setThickness(10);




    }

    public String convertString(String string){
        String newString = "";
        for(int x =0 ; x< 8; x++){
            if(x ==2){
                newString = newString + "/";
            }else if(x == 4) {
                newString = newString + "/";
            }
            newString = newString + string.charAt(x);
        }
        return newString;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, TimetableActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_progress) {
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_task) {

            Intent intent = new Intent(this, TaskListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_weektable) {
            Intent intent = new Intent(this, WeekTable.class);
            startActivity(intent);

        }  else if (id == R.id.nav_events) {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
         else if (id == R.id.nav_import) {
                    Intent intent = new Intent(this, GCAPIActivity.class);
                    startActivity(intent);

         }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
