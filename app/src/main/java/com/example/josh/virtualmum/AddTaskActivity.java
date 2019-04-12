package com.example.josh.virtualmum;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Database.Task.Task;
import Database.VMDbHelper;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        FloatingActionButton eFab = findViewById(R.id.exitFab);
        eFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VMDbHelper db;
                db = new VMDbHelper(getApplicationContext());

                TextView textView = findViewById(R.id.NameField);
                String taskName = textView.getText().toString().toUpperCase();

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
                dateDue  = dateDue + spinners.getSelectedItem().toString() + 120000;


                SeekBar prioritySB = findViewById(R.id.PrioritySeekBar);
                int priority = prioritySB.getProgress() + 1;

                SeekBar difficultySB = findViewById(R.id.DifficultySeekBar);
                int difficulty = difficultySB.getProgress() + 1;

                textView = findViewById(R.id.LocationField);
                double estimatedHours = Double.valueOf(textView.getText().toString());

                long task_id = db.insertTask(taskName, formattedDateTime, dateDue, difficulty, priority, estimatedHours, 0);

                List<Task> allTasks = db.getAllTasks();
                for (Task task : allTasks) {
                    Log.d(" Tasks", task.getId() + "\t" + task.getName() + "\t" + task.getDueDate()  + "\t" +
                            task.getDifficulty() + "\t" + task.getPriority() + "\t" + task.getEstimatedHours());
                }

                db.closeDB();


                Snackbar.make(view, "Task Added!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });



    }
}
