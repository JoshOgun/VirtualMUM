package com.example.josh.virtualmum;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import Database.UserPreference.UserPref;
import Database.VMDbHelper;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UserPref userPref;




        VMDbHelper db;
        db = new VMDbHelper(getApplicationContext());

        try {
            String activityUPIDstr = getIntent().getStringExtra("USERPREF");
            int activityUPID = Integer.parseInt(activityUPIDstr);
            userPref = db.getUserPref(activityUPID);
        } catch (Exception e) {
            userPref = db.getTopUP();
        }
        Log.d(" UP", userPref.getId() + "\t" + userPref.getName() + "\t" + userPref.getWorkPref() + "\t" +
                    userPref.getNoDayPref() );


        db.closeDB();

        TextView tv = findViewById(R.id.NameET);
        tv.setText(userPref.getName());
        tv = findViewById(R.id.tv_name);
        tv.setText(userPref.getName());


        tv = findViewById(R.id.WTPTxt);
        tv.setText(userPref.getWorkPref());

        tv = findViewById(R.id.DOPTxt);
        tv.setText(userPref.getNoDayPref());

        FloatingActionButton fab = findViewById(R.id.EditFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), SetUpActivity.class);
                startActivity(myIntent);

            }
        });
    }
}
