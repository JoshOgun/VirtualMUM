package com.example.josh.virtualmum.JacksHomePageCode;

import com.example.josh.virtualmum.JacksHomePageCode.weektableView.TimetableView;
import com.example.josh.virtualmum.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class weektable extends AppCompatActivity {
   //
    // private Context context;
    private TimetableView weektable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weektable);
        weektable = findViewById(R.id.timetable);
        weektable.setHeaderHighlight(1);
        //init();
    }
   // private void init() {
        //this.context = this;
   // }
}
