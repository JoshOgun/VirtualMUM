package com.example.josh.virtualmum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Database.Event.Event;
import Database.VMDbHelper;

public class EditEventActivity extends AppCompatActivity {

    String activityEventIDstr;
    int activityEventID;
    VMDbHelper db;
    Event newEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

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

        activityEventIDstr = getIntent().getStringExtra("EVENT");
        activityEventID = Integer.parseInt(activityEventIDstr);

        db = new VMDbHelper(getApplicationContext());

        newEvent = db.getEvent(activityEventID);

        db.closeDB();

        TextView textView = findViewById(R.id.NameField);
        textView.setText(newEvent.getName());

        String dateTime = newEvent.getStartDate();
        String floatNumber = dateTime.substring(0,2);
        int id;

        Spinner spinners = findViewById(R.id.daySpinner);
        id = Integer.parseInt(floatNumber) - 1;
        spinners.setSelection(id);

        spinners = findViewById(R.id.monSpinner);
        floatNumber = dateTime.substring(2,4);
        id = Integer.parseInt(floatNumber) - 1;
        spinners.setSelection(id);

        spinners = findViewById(R.id.yearSpinner);
        floatNumber = dateTime.substring(4,8);
        id = Integer.parseInt(floatNumber) - 2018;
        spinners.setSelection(id);

        spinners = findViewById(R.id.fromHourSpinner);
        floatNumber = dateTime.substring(8,10);
        id = Integer.parseInt(floatNumber) - 1;
        spinners.setSelection(id);

        spinners = findViewById(R.id.fromMinutesSpinner);
        floatNumber = dateTime.substring(10,12);
        id = (Integer.parseInt(floatNumber) / 5);
        spinners.setSelection(id);

         dateTime = newEvent.getEndDate();

        spinners = findViewById(R.id.toHourSpinner);
        floatNumber = dateTime.substring(8,10);
        id = Integer.parseInt(floatNumber) - 1;
        spinners.setSelection(id);

        spinners = findViewById(R.id.toMinutesSpinner);
        floatNumber = dateTime.substring(10,12);
        id = (Integer.parseInt(floatNumber) / 5);
        spinners.setSelection(id);

        textView = findViewById(R.id.LocationField);
        textView.setText(newEvent.getLocation());

        final Button deleteBtn = findViewById(R.id.deleteBtnU);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                db = new VMDbHelper(getApplicationContext());

                List<Event> allEvents = db.getAllEvents();
                for (Event event : allEvents) {
                    if(event.getId() == activityEventID) {
                        db.deleteEvent(event);
                    }
                }

                db.closeDB();

                Toast.makeText(getApplicationContext(), "Event Deleted!", Toast.LENGTH_SHORT).show();

            }
        });

        final Button updateButton = findViewById(R.id.updateBtnU);
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                db = new VMDbHelper(getApplicationContext());

                TextView textView = findViewById(R.id.NameField);
                newEvent.setName(textView.getText().toString().toUpperCase());

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
                newEvent.setStartDate(dateDue.concat(startTime.concat(spinners.getSelectedItem().toString())));

                spinners = findViewById(R.id.toHourSpinner);
                endTime = spinners.getSelectedItem().toString();
                spinners = findViewById(R.id.toMinutesSpinner);
                newEvent.setEndDate(dateDue.concat(endTime.concat(spinners.getSelectedItem().toString())));

                textView = findViewById(R.id.LocationField);
                newEvent.setLocation(textView.getText().toString());

                List<Event> allEvents = db.getAllEvents();
                for (Event event : allEvents) {
                    if(event.getId() == activityEventID) {
                        event.setName(newEvent.getName());
                        event.setStartDate(newEvent.getStartDate());
                        event.setEndDate(newEvent.getEndDate());
                        event.setLocation(newEvent.getLocation());
                        db.updateEvent(event);
                    }

                }


                db.closeDB();

                Toast.makeText(getApplicationContext(), "Events Updated!", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
