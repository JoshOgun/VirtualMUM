package com.example.josh.virtualmum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class TimetableActivity extends AppCompatActivity {

    TextView textView;
    int originalPosition, counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        textView =  findViewById(R.id.textView);

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);



        final HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .datesNumberOnScreen(5)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {

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
                    textView.setText("Selected Date: " +  date);
            }


        });
    }
}
