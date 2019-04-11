package com.example.josh.virtualmum;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

public class ProgressActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_progress_graph);

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

        start = db.getTask(taskID).getStartDate().substring(0,8);
        end = db.getTask(taskID).getDueDate().substring(0,8);
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
            //curDate = sdf.parse(cur);
        } catch(ParseException e){
            e.printStackTrace();
        }

        timeLength = (int) (TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS));
       // timeLength = (int)( (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        timeLengthCur = (int)(TimeUnit.DAYS.convert(curDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS));
        //double theirEstimated = 22;
        double ourEstimated = 20;
        //double actualSpent = 10;

        double theirEstimated = db.getTask(taskID).getEstimatedHours();
       // float ourEstimated = db.getReport(taskID).getEstimatedHours();
        double actualSpent = db.getProgress(taskID).getHoursSpent();

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


}
