package com.example.josh.virtualmum.JacksHomePageCode;

import com.example.josh.virtualmum.JacksHomePageCode.weektableView.TimetableView;
import com.example.josh.virtualmum.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.josh.virtualmum.JacksHomePageCode.weektableView.Schedule;
import com.example.josh.virtualmum.JacksHomePageCode.weektableView.TimetableView;
import java.util.ArrayList;

public class weektable extends AppCompatActivity {
   //
    // private Context context;
    private TimetableView weektable;
    private Schedule schedule;
    private String [][] demo = {{"1200/1/1","1300/1/2"},
            {"1100/2/3","1400/3/4"},
            {"1200/1/4","1500/2/5"},
            {"1100/2/6","1600/1/7"},
            {"1200/2/8"},
            {"0800/2/9","1400/3/10"},
            {"0900/1/11","1200/1/12","1300/1/13"}};

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weektable);
        weektable = findViewById(R.id.timetable);
        weektable.setHeaderHighlight(1);
        weektable.removeAll();
        //init();

        for (int i = 0;i<demo.length;i++){

            for (int j = 0;j<demo[i].length;j++){
                int[] tem = new int[5];
                tem = getData(demo[i][j]);
                add(i+1,tem[0],tem[1],tem[2],tem[3],String.valueOf(tem[4]));
            }
        }


    }

    private int[] getData (String x){
        int[] result = new int[5];
        String[] tem = new String[3] ;
        int t ;
        tem = x.split("/");
        result[4]=Integer.parseInt(tem[2]);
        t=Integer.parseInt(tem[1]);
        result[1]=Integer.parseInt(tem[0].substring(2,4));
        result[0]=Integer.parseInt(tem[0].substring(0,2));
        result[2] = result[0]+((t*60)/60);
        result[3] = result[1]+((t*60)%60);

        return result;
    }

    public void add(int d, int StartHour, int StartM,int endh,int endm,String z){
       // if (d==0){
         //   d = 7;
        //}
        schedule = new Schedule();
        schedule.setDay(d-1);
        schedule.setClassTitle(z);
        schedule.getStartTime().setHour(StartHour);
        schedule.getStartTime().setMinute(StartM);
        schedule.getEndTime().setHour(endh);
        schedule.getEndTime().setMinute(endm);
        Intent i = new Intent();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);
        i.putExtra("schedules",schedules);
        onActivityResult(i);



    }



    protected void onActivityResult( @Nullable Intent data) {

                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");
                    weektable.add(item);
       /* if (i  ==1 ) {
            weektable.setStickerColor(Color.GRAY);
        }
        else if(i == 2){
            weektable.setStickerColor(Color.GREEN);


        }
        else{
            weektable.setStickerColor(Color.BLUE);
        }
*/
    }

}
