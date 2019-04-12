package com.example.josh.virtualmum.JacksHomePageCode;

import com.example.josh.virtualmum.JacksHomePageCode.weektableView.TimetableView;
import com.example.josh.virtualmum.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.josh.virtualmum.JacksHomePageCode.weektableView.Schedule;

import java.util.ArrayList;

import Database.AllocationAlgorithm.User;

public class WeekTable extends AppCompatActivity {
   //
    // private Context context;
    private TimetableView weekTable;
    private Schedule schedule;
    private String [][] demo = {{"1200/1/1","1300/1/2"},
            {"1159/2/3","1400/3/4"},
            {"1200/1/4","1500/2/5"},
            {"1100/2/6","1600/1/7"},
            {"1200/2/8"},
            {"0800/2/9","1400/3/10"},
            {"0900/1/11","1200/1/12","1300/1/13"}};
    private String [][] demo1 = {{"0900/2/1","1500/1/2"},
            {"0800/2/3","1600/3/4"},
            {"0700/4/4","1800/2/5"},
            {"1000/1/6","1400/1/7"},
            };
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weektable);
        User u = new User();
        u.updateEvents(getApplicationContext());
        weekTable = findViewById(R.id.timetable);
        weekTable.setHeaderHighlight(1);
        weekTable.removeAll();
        //init();
      /*  add(0,"1","Math","CB5.14",11,15,12,05,1);
        add(0,"2","AI","CB1.11",14,15,15,05,1);
        add(0,"1","Math","CB5.14",8,15,9,05,2);
        add(0,"2","AI","CB1.11",10,15,11,05,3);
        add(1,"3","FP","EB1.1",16,15,17,05,3);
        add(2,"1","ALGE","CB5.14",10,15,11,05,3);
        add(3,"2","AI","CB1.11",12,15,13,05,1);

        add(4,"3","FPLab","EB1.1",16,15,18,05,2);
        add(5,"1","ALGE","CB5.14",10,15,11,05,2);
        add(6,"2","AI","CB1.11",12,15,13,05,2);*/
     for (int i = 0;i<demo.length;i++){
         for (int j = 0;j<demo[i].length;j++){
             int[] tem = new int[5];
             tem = getData(demo[i][j]);
             add(i,String.valueOf(tem[4]),tem[0],tem[1],tem[2],tem[3],0);

         }
     }

        for (int i = 0;i<demo1.length;i++){
            for (int j = 0;j<demo1[i].length;j++){
                int[] tem = new int[5];
                tem = getData(demo1[i][j]);
                add(i,String.valueOf(tem[4]),tem[0],tem[1],tem[2],tem[3],1);

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

    protected void onActivityResult( @Nullable Intent data,int t) {

                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");
                    weekTable.add(item);
                    weekTable.setStickerColor(t);

    }

}
