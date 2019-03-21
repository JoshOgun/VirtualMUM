package com.example.josh.virtualmum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Database.Task.Task;
import Database.VMDbHelper;

public class EditTaskActivity extends AppCompatActivity {

    String activityTaskIDstr;
    int activityTaskID;
    Task newTask;
    VMDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

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


        activityTaskIDstr = getIntent().getStringExtra("TASK");
        activityTaskID = Integer.parseInt(activityTaskIDstr);


        db = new VMDbHelper(getApplicationContext());

        newTask = db.getTask(activityTaskID);

        db.closeDB();

        TextView textView = findViewById(R.id.NameField);
        textView.setText(newTask.getName());

        String dateTime = newTask.getDueDate();
        String floatTime = dateTime.substring(0,2);
        int id;

        Spinner spinners = findViewById(R.id.daySpinner);
        id = Integer.parseInt(floatTime) - 1;
        spinners.setSelection(id);

        spinners = findViewById(R.id.monSpinner);
        floatTime = dateTime.substring(2,4);
        id = Integer.parseInt(floatTime) - 1;
        spinners.setSelection(id);


        spinners = findViewById(R.id.yearSpinner);
        floatTime = dateTime.substring(4,8);
        id = Integer.parseInt(floatTime) - 2018;
        spinners.setSelection(id);


        SeekBar prioritySB = findViewById(R.id.PrioritySeekBar);
        int priority = newTask.getPriority() - 1;
        prioritySB.setProgress(priority );

        SeekBar difficultySB = findViewById(R.id.DifficultySeekBar);
        int difficulty = newTask.getDifficulty() - 1;
        difficultySB.setProgress(difficulty);

        textView = findViewById(R.id.LocationField);
        double estimatedHours =  newTask.getEstimatedHours();
        textView.setText(Double.toString(estimatedHours));

         Switch completed = findViewById(R.id.completedSwitch);
        if(newTask.getCompleted() == 0){
            completed.setChecked(false);
        }
        else{
            completed.setChecked(true);
        }

        final Button updateButton = findViewById(R.id.updateBtn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db = new VMDbHelper(getApplicationContext());

                TextView textView = findViewById(R.id.NameField);
                String taskName = textView.getText().toString().toUpperCase();
                newTask.setName(taskName);

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
                newTask.setDueDate(dateDue);


                SeekBar prioritySB = findViewById(R.id.PrioritySeekBar);
                int priority = prioritySB.getProgress() + 1;
                newTask.setPriority(priority);

                SeekBar difficultySB = findViewById(R.id.DifficultySeekBar);
                int difficulty = difficultySB.getProgress() + 1;
                newTask.setDifficulty(difficulty);

                textView = findViewById(R.id.LocationField);
                double estimatedHours = Double.valueOf(textView.getText().toString());
                newTask.setEstimatedHours(estimatedHours);

                 Switch completed = findViewById(R.id.completedSwitch);
                if(completed.isChecked()){
                    newTask.setCompleted(1);
                }
                else{
                    newTask.setCompleted(0);
                }

//                task = db.getTask(activityTaskID);
                List<Task> allTasks = db.getAllTasks();
                for (Task task : allTasks) {
                    if(task.getId() == activityTaskID) {
                        task.setName(newTask.getName());
                        task.setDueDate(newTask.getDueDate());
                        task.setPriority(newTask.getPriority());
                        task.setDifficulty(newTask.getDifficulty());
                        task.setEstimatedHours(newTask.getEstimatedHours());
                        task.setCompleted(newTask.getCompleted());
                        db.updateTask(task);
                    }

                }


                db.closeDB();

                Toast.makeText(getApplicationContext(), "Tasks Updated! " + newTask.getName(), Toast.LENGTH_SHORT).show();
                allTasks = db.getAllTasks();
                Log.d(" Tasks","TASKS");
                for (Task task : allTasks) {
                    Log.d(" Tasks", task.getId() + "\t" + task.getName() + "\t" + task.getDueDate() + "\t" +
                            task.getDifficulty() + "\t" + task.getPriority() + "\t" + task.getEstimatedHours() + "\t" + task.getCompleted());
                }
            }
        });


        final Button deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                db = new VMDbHelper(getApplicationContext());

//                task = db.getTask(activityTaskID);

                List<Task> allTasks = db.getAllTasks();
                for (Task task : allTasks) {
                    if(task.getId() == activityTaskID) {
                        db.deleteTask(task);
                    }
                }

                db.closeDB();

                Toast.makeText(getApplicationContext(), "Task Deleted!", Toast.LENGTH_SHORT).show();
                 allTasks = db.getAllTasks();
                Log.d(" Tasks","TASKS");
                for (Task task : allTasks) {
                    Log.d(" Tasks", task.getId() + "\t" + task.getName() + "\t" + task.getDueDate() + "\t" +
                            task.getDifficulty() + "\t" + task.getPriority() + "\t" + task.getEstimatedHours());
                }

            }
        });
    }
}
