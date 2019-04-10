package com.example.josh.virtualmum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.josh.virtualmum.JacksHomePageCode.TimetableActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Database.VMDbHelper;

public class SetUpEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_events);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         Button button = findViewById(R.id.DoneBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimetableActivity.class);
                startActivity(intent);
            }
        });

        Spinner spinner = findViewById(R.id.daySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,
                R.array.dayDate_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.monSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.monDate_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.yearSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.yearDate_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.fromHourSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.hours_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.toHourSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.hours_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.fromMinutesSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.mins_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.toMinutesSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.mins_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.AddFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VMDbHelper db;
                db = new VMDbHelper(getApplicationContext());

                TextView textView = findViewById(R.id.NameField);
                String eventName = textView.getText().toString().toUpperCase();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("ddMMMyyyyHHmmss");
                String formattedDateTime = df.format(c);

                String dateDue;
                Spinner spinners = findViewById(R.id.daySpinner);
                dateDue  = spinners.getSelectedItem().toString();
                spinners = findViewById(R.id.monSpinner);
                if(spinners.getSelectedItemPosition() + 1 < 10){
                    dateDue  = dateDue + "0" +(spinners.getSelectedItemPosition() + 1);
                }
                else{
                    dateDue  = dateDue + (spinners.getSelectedItemPosition() + 1);
                }
                spinners = findViewById(R.id.yearSpinner);
                dateDue  = dateDue + spinners.getSelectedItem().toString();

                String startTime, endTime;
                spinners = findViewById(R.id.fromHourSpinner);
                startTime = spinners.getSelectedItem().toString();
                spinners = findViewById(R.id.fromMinutesSpinner);
                startTime = startTime.concat(spinners.getSelectedItem().toString());

                spinners = findViewById(R.id.toHourSpinner);
                endTime = spinners.getSelectedItem().toString();
                spinners = findViewById(R.id.toMinutesSpinner);
                endTime = endTime.concat(spinners.getSelectedItem().toString());

                textView = findViewById(R.id.LocationField);
                String location = textView.getText().toString();

                long event_id = db.insertEvent(eventName, dateDue.concat(startTime), dateDue.concat(endTime), location);

                db.closeDB();

                Snackbar.make(view, "Event Added!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
