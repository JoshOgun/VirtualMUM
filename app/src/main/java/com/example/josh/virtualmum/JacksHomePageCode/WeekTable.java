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

public class WeekTable extends AppCompatActivity {
   //
    // private Context context;
    private TimetableView weekTable;
    private Schedule schedule;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weektable);
        weekTable = findViewById(R.id.timetable);
        weekTable.setHeaderHighlight(1);
        weekTable.removeAll();
        //init();
        add(0,"1","Math","CB5.14",11,15,12,05,1);
        add(0,"2","AI","CB1.11",14,15,15,05,1);
        add(0,"1","Math","CB5.14",8,15,9,05,2);
        add(0,"2","AI","CB1.11",10,15,11,05,3);
        add(1,"3","FP","EB1.1",16,15,17,05,3);
        add(2,"1","ALGE","CB5.14",10,15,11,05,3);
        add(3,"2","AI","CB1.11",12,15,13,05,1);

        add(4,"3","FPLab","EB1.1",16,15,18,05,2);
        add(5,"1","ALGE","CB5.14",10,15,11,05,2);
        add(6,"2","AI","CB1.11",12,15,13,05,2);


    }

    public void add(int d,String z,String j,String k, int StartHour, int StartM,int endh,int endm,int p){
        schedule = new Schedule();
        schedule.setDay(d);
        schedule.setClassTitle(z);
        schedule.setClassPlace(j);
        schedule.setProfessorName(k);
        schedule.getStartTime().setHour(StartHour);
        schedule.getStartTime().setMinute(StartM);
        schedule.getEndTime().setHour(endh);
        schedule.getEndTime().setMinute(endm);
        Intent i = new Intent();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);
        i.putExtra("schedules",schedules);
        onActivityResult(i,p);



    }



    protected void onActivityResult( @Nullable Intent data,int i) {

                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");
                    weekTable.add(item);
        if (i  ==1 ) {
            weekTable.setStickerColor(Color.YELLOW);
        }
        else if(i == 2){
            weekTable.setStickerColor(Color.GREEN);


        }
        else{
            weekTable.setStickerColor(Color.BLUE);
        }

    }

}