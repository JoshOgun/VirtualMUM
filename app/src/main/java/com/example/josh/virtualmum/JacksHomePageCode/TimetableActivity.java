package com.example.josh.virtualmum.JacksHomePageCode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Database.Timetable.Timetable;
import Database.VMDbHelper;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

import com.example.josh.virtualmum.R;
import com.example.josh.virtualmum.JacksHomePageCode.TimetableView.Schedule;
import com.example.josh.virtualmum.JacksHomePageCode.TimetableView.TimetableView;

public class TimetableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TimetableView timetable;
    private Schedule schedule;
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        this.context = this;
        timetable = findViewById(R.id.timetable);
        schedule = new Schedule();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, EditActivity.class);
//                i.putExtra("mde",REQUEST_ADD);
//                startActivityForResult(i,REQUEST_ADD);

            }
        });
        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
//                Intent i = new Intent(context, EditActivity.class);
//                i.putExtra("mode",REQUEST_EDIT);
//                i.putExtra("idx", idx);
//                i.putExtra("schedules", schedules);
//                startActivityForResult(i,REQUEST_EDIT);
            }
        });

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        //startDate.add(3,-1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        //endDate.add(3, 1);




        final HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                //.endDate(endDate.add())
                .startDate(startDate.getTime())
                .datesNumberOnScreen(5)
                .build();

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);
        final Date today = c.getTime();

        horizontalCalendar.setCalendarListener(
                new HorizontalCalendarListener() {



                    @Override
                    public void onDateSelected(Date date, int position) {
                        // Something Josh is working on
//                    if(counter == 0){
//                        originalPosition = position;
//                        counter++;
//                    }
//                    if(position > originalPosition + 5 ){
//                        date = Calendar.getInstance().getTime();
//                        position = originalPosition;
//                    }


                        //loadSavedData();
                        //timetable.removeAll();

                        VMDbHelper db;
                        db = new VMDbHelper(getApplicationContext());


                        //long timetable_id = db.insertTimetable("100420191200", 0, 2, 1,0);

                        SimpleDateFormat simpleDate =  new SimpleDateFormat("ddMMyyyy");
                        String todayStr = simpleDate.format(today);

                        // We only work in hours so i add the duration to the hour.
                        if (isSameDate(today,date)){
                           timetable.removeAll();
                            List<Timetable> allT = db.getFullTimetable();

                            for (Timetable t : allT) {
                                //db.deleteTimetable(t);
                                Log.d("TimetableTable", "\t" + t.getId()+ "\t" + t.getDate() + "\t" + t.getEventID() +  "\t" + t.getTaskID() +  "\t" + t.getDuration() + "\t" + t.getCompleted());
                                String eDate = t.getDate().substring(0,8);
                                if(todayStr.equals(eDate) ){
                                    if(t.getTaskID() == 0){
                                        String name = db.getEvent(t.getEventID()).getName();
                                        String fullDate = t.getDate();
                                        int startH, startM, endH;
                                        startH = Integer.parseInt(fullDate.substring(8,10));
                                        startM = Integer.parseInt(fullDate.substring(10,12));
                                        endH =  (Integer.parseInt(fullDate.substring(8,10))) + Math.round(t.getDuration());

                                        add(name, startH, startM, endH, startM);
                                    }
                                    else{
                                        String name = db.getTask(t.getTaskID()).getName();
                                        String fullDate = t.getDate();
                                        int startH, startM, endH;
                                        startH = Integer.parseInt(fullDate.substring(8,10));
                                        startM = Integer.parseInt(fullDate.substring(10,12));
                                        endH =  (Integer.parseInt(fullDate.substring(8,10))) + Math.round(t.getDuration());

                                        add(name, startH, startM, endH, startM);
                                    }
                                }
                            }

                        }
                         else{
                             timetable.removeAll();
                         }

                        db.closeDB();
                    }

                }
        );
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    public static boolean isSameDate(Date date1, Date date2) {

        if (date1.getYear() == date2.getYear()) {
            if (date1.getMonth() == date2.getMonth()) {
                if (date1.getDate() == date2.getDate()) {
                    return true;
                }
            }
        }

        return false;


    }
   // @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_progress) {

        } else if (id == R.id.nav_task) {


        } else if (id == R.id.nav_weektable) {
//             Intent i = new Intent(context,weektable.class);
//             startActivity(i);
        }
        else if (id == R.id.nav_setting) {

        }


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void add(String name, int startHour, int startMin,int endh,int endm){
        schedule = new Schedule();

        schedule.setTitle(name);
        schedule.getStartTime().setHour(startHour);
        schedule.getStartTime().setMinute(startMin);
        schedule.getEndTime().setHour(endh);
        schedule.getEndTime().setMinute(endm);
        Intent i = new Intent();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);
        i.putExtra("schedules",schedules);
        onActivityResult(1,1,i);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        switch (requestCode){
//            case REQUEST_ADD:
//                if(resultCode == EditActivity.RESULT_OK_ADD){
//                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");
//                    timetable.add(item);
//                    //saveByPreference(timetable.createSaveData());
//                }
//                break;
//            case REQUEST_EDIT:
//                /** Edit -> Submit */
//                if(resultCode == EditActivity.RESULT_OK_EDIT){
//                    int idx = data.getIntExtra("idx",-1);
//                    ArrayList<Schedule> item = (ArrayList<Schedule>)data.getSerializableExtra("schedules");
//                    timetable.edit(idx,item);
//                  //  saveByPreference(timetable.createSaveData());
//
//                }
//                /** Edit -> Delete */
//                else if(resultCode == EditActivity.RESULT_OK_DELETE){
//                    int idx = data.getIntExtra("idx",-1);
//                    timetable.remove(idx);
//                //    saveByPreference(timetable.createSaveData());
//
//                }
//                break;
//        }
//    }

    /** save timetableView's data to SharedPreferences in json format */
    private void saveByPreference(String data){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("timetable_demo",data);
        editor.commit();
        Toast.makeText(this,"saved!",Toast.LENGTH_SHORT).show();
    }

    /** get json data from SharedPreferences and then restore the timetable */
    private void loadSavedData(){
        timetable.removeAll();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        String savedData = mPref.getString("timetable_demo","");
        if(savedData == null && savedData.equals("")) return;
        //timetable.load(savedData);
        Toast.makeText(this,"loaded!",Toast.LENGTH_SHORT).show();
    }
}
