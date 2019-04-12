package com.example.josh.virtualmum.JacksHomePageCode;

import com.example.josh.virtualmum.JacksHomePageCode.weektableView.TimetableView;
import com.example.josh.virtualmum.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.josh.virtualmum.JacksHomePageCode.weektableView.Schedule;

import java.util.ArrayList;

import Database.AllocationAlgorithm.User;
import Database.VMDbHelper;

public class WeekTable extends AppCompatActivity {
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
        demo = new String[1000][1000];
        demo1 = new String[1000][1000];
        setContentView(R.layout.activity_weektable);
        User u = new User();
        u.updateEvents(getApplicationContext());
        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());
        demo = u.getTasks();
        demo1 = u.getEvents();
        weekTable = findViewById(R.id.timetable);
        weekTable.setHeaderHighlight(1);
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


    protected void onActivityResult( @Nullable Intent data,int t) {

                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");
                    weekTable.add(item);
                    weekTable.setStickerColor(t);

    }

}
