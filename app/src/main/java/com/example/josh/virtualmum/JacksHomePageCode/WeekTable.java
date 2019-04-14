package com.example.josh.virtualmum.JacksHomePageCode;

import com.example.josh.virtualmum.EventListActivity;
import com.example.josh.virtualmum.JacksHomePageCode.weektableView.TimetableView;
import com.example.josh.virtualmum.ProfileActivity;
import com.example.josh.virtualmum.ProgressActivity;
import com.example.josh.virtualmum.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.josh.virtualmum.JacksHomePageCode.weektableView.Schedule;
import com.example.josh.virtualmum.TaskListActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Database.AllocationAlgorithm.User;
import Database.VMDbHelper;

public class WeekTable extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
   //
    // private Context context;
    private TimetableView weekTable;
    private Schedule schedule;
    private String [][] demo;



//            = {{"1200/1/1","1300/1/2"},
//            {"1159/2/3","1400/3/4"},
//            {"1200/1/4","1500/2/5"},
//            {"1100/2/6","1600/1/7"},
//            {"1200/2/8"},
//            {"0800/2/9","1400/3/10"},
//            {"0900/1/11","1200/1/12","1300/1/13"}};
    private String [][] demo1 ;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Calendar cal = Calendar.getInstance();

        demo = new String[1000][1000];
        demo1 = new String[1000][1000];
        setContentView(R.layout.activity_navigate_weektable);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        User u = new User();
        u.updateEvents(getApplicationContext());
        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());
        demo = u.getTasks();
        demo1 = u.getEvents();
        weekTable = findViewById(R.id.timetable);
       int  k = cal.get(Calendar.DAY_OF_WEEK)-1;
       if(k==0){
           k=7;
       }
        weekTable.setHeaderHighlight(k);
        weekTable.removeAll();

     for (int i = 0;i<demo.length;i++){
         for (int j = 0;j<demo[i].length;j++){
             int[] tem = new int[5];
             if (demo[i][j] != null){
             tem = getData(demo[i][j]);
             add(i,db.getTask(tem[4]).getName(),tem[0],tem[1],tem[2],tem[3],0);
             }
             else{
                 break;
             }

         }
     }

        for (int i = 0;i<demo1.length;i++){
            for (int j = 0;j<demo1[i].length;j++){
                int[] tem = new int[5];
                if(demo1[i][j]!=null) {
                    tem = getData1(demo1[i][j]);
                    add(i, getData2(demo1[i][j]), tem[0], tem[1], tem[2], tem[3], 1);
                }
                else{
                    break;
                }
            }
        }

    }

    public void add(int d,String k, int StartHour, int StartM,int endh,int endm,int t){
        schedule = new Schedule();
        schedule.setDay(d);

        schedule.setClassTitle(k);
        schedule.getStartTime().setHour(StartHour);
        schedule.getStartTime().setMinute(StartM);
        schedule.getEndTime().setHour(endh);
        schedule.getEndTime().setMinute(endm);
        Intent i = new Intent();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);
        i.putExtra("schedules",schedules);
        onActivityResult(i,t);



    }

    public int[] getData (String demo){
        int[] result = new int[5];
        String[] tem = new String[3];
        int t = 0;
        tem = demo.split("/");
        result[4]=Integer.parseInt(tem[2]);
        t=Integer.parseInt(tem[1]);
        result[1]=Integer.parseInt(tem[0].substring(2,4));
        result[0]=Integer.parseInt(tem[0].substring(0,2));
        result[2] = result[0]+((t*60)/60);
        result[3] = result[1]+((t*60)%60);

        return result;
    }

    public int[] getData1 (String demo){
        int[] result = new int[5];
        String[] tem = new String[3];
        int t = 0;
        tem = demo.split("/");

        t=Integer.parseInt(tem[1]);
        result[1]=Integer.parseInt(tem[0].substring(2,4));
        result[0]=Integer.parseInt(tem[0].substring(0,2));
        result[2] = result[0]+((t*60)/60);
        result[3] = result[1]+((t*60)%60);

        return result;
    }

    public String getData2 (String demo){
        String result=" ";
        String[] tem = new String[3];
        int t = 0;
        tem = demo.split("/");
        result = tem[2];


        return result;
    }

    public int getWeekOfDate(Date date, int i) {
        int[] weekDays = {0, 1, 2, 3, 4, 5, 6 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        w = w+i;
        if (w >=7)
            w=w-7;
        return weekDays[w];
    }

    protected void onActivityResult( @Nullable Intent data,int t) {

                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");
                    weekTable.add(item);
                    weekTable.setStickerColor(t);

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
            drawer.closeDrawer(GravityCompat.START);
            return true;

        }  else if (id == R.id.nav_events) {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
