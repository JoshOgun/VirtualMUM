package com.example.josh.virtualmum;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import Database.VMDbHelper;

public class SetUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        Spinner spinner = findViewById(R.id.WPspinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
       adapter = ArrayAdapter.createFromResource(this,
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

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VMDbHelper db;
                db = new VMDbHelper(getApplicationContext());

                TextView tv = findViewById(R.id.nameTxt);
                String name = tv.getText().toString();

                Spinner sp = findViewById(R.id.WPspinner);
                String workPref = sp.getSelectedItem().toString();

                sp = findViewById(R.id.DPspinner);
                String dayPrefs = sp.getSelectedItem().toString();

                sp = findViewById(R.id.DPspinner2);
                dayPrefs += "|" + sp.getSelectedItem().toString();

                long upId = db.insertUserPref(name, workPref, dayPrefs);

                db.closeDB();
                Intent myIntent = new Intent(getBaseContext(), ProfileActivity.class);
                myIntent.putExtra("USERPREF", Long.toString(upId));
                startActivity(myIntent);
            }
        });
    }
}
