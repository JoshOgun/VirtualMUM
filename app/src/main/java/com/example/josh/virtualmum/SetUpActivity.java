package com.example.josh.virtualmum;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.josh.virtualmum.JacksHomePageCode.TimetableActivity;

import java.util.List;

import Database.AllocationAlgorithm.User;
import Database.UserPreference.UserPref;
import Database.VMDbHelper;

public class SetUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        VMDbHelper db = new VMDbHelper(getApplicationContext());

        // FOR TESTING
//        List<UserPref> ups = db.getAllUserPref();
//        for(UserPref u : ups){
//            Log.d(" UserPREF", "\t" + u.getId() + "\t" + u.getName());
//            db.deleteUserPref(u);
//        }

        UserPref up = db.getTopUP();
//        // FOR TESTING
//        db.deleteUserPref(up);
//        up = db.getTopUP();
        db.closeDB();

        if(up.getName() != null){
            Intent myIntent = new Intent(getBaseContext(), TimetableActivity.class);
            startActivity(myIntent);
        }


        Spinner spinner = findViewById(R.id.WPspinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.DPspinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.day_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.DPspinner2);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.day_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Button button = findViewById(R.id.NextBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VMDbHelper db;
                db = new VMDbHelper(getApplicationContext());

                TextView tv = findViewById(R.id.nameTxt);
                String name = tv.getText().toString();

                Spinner sp = findViewById(R.id.WPspinner);
                String workPref = sp.getSelectedItem().toString();

                sp = findViewById(R.id.DPspinner);
                String dayPrefs = sp.getSelectedItem().toString();

                sp = findViewById(R.id.DPspinner2);
                dayPrefs += "\t|\t" + sp.getSelectedItem().toString();

                long upId = db.insertUserPref(name, workPref, dayPrefs);

                db.closeDB();
                Intent myIntent = new Intent(getBaseContext(), SetUpEventsActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
