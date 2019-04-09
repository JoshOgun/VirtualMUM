package com.example.josh.virtualmum.JacksHomePageCode.TimetableView;

import android.content.Context;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Database.Task.Task;
import Database.VMDbHelper;

public class Sticker implements Serializable {
    private ArrayList<TextView> view;
    private ArrayList<Schedule> schedules;


    public Sticker() {
        this.view = new ArrayList<TextView>();
        this.schedules = new ArrayList<Schedule>();
    }

    public void addTextView(TextView v){
        view.add(v);
    }

    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
    }

    public ArrayList<TextView> getView() {
        return view;
    }

    public ArrayList<Schedule> getSchedules() {
//        VMDbHelper db = new VMDbHelper(context);
//        List<Task> allTasks;
//        allTasks = db.getAllTasks();
        return schedules;
    }
}
