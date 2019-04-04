package Database.AllocationAlgorithm;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import Database.Event.Event;
import Database.Task.Task;
import Database.VMDbHelper;

public class User {

    //public String questionnaireAnswers  - decide on format for this later
    public List<Task> taskList;
    //something to hold 'events' maybe another arraylist?
    public String googleURL;  //for integration with google calendar
    public List<Event> eventList;
    public assignTimetable timetableHandler;
    public String[][] timeTable;
    public int startOfDay;
    public int endOfDay;
    public int startOfWeek;
    public int endOfWeek;
    public formatter formatter;

    VMDbHelper db;

    public User() {
        taskList = new ArrayList<Task>();
        eventList = new ArrayList<Event>();
        timetableHandler = new assignTimetable(this);
        this.startOfDay = 8;
        this.endOfDay = 17;
        this.startOfWeek = 1;
        this.endOfWeek = 6;
        this.formatter = new formatter(startOfDay, endOfDay, timeTable);


    }

    public void setGoogleURL(String url) { //adds google url to user
        this.googleURL = url;
    }

    public void updateTasks(Context context){

        db = new VMDbHelper(context);
        taskList = db.getAllTasks();
        timetableHandler.orderTasks();
        formatter.timeTable = this.timeTable;
        saveToDb(context);
        db.closeDB();
    }


    public void saveToDb(Context context){
        List<String> toDbList = formatter.convertor();
        for (String str : toDbList){
            String[] components  = str.split("/");
            String date = components[0];
            int taskId = Integer.parseInt(components[1]);
            int eventId = 0;
            float duration = (float)Integer.parseInt(components[2]);
            int completed = 0;
            db.insertTimetable(date, taskId, eventId, duration, completed);
        }
    }

    public void updateEvents(Context context){

        db = new VMDbHelper(context);
        eventList = db.getAllEvents();
        populateNumbers();
        timetableHandler.updateEvents();
        db.closeDB();
        updateTasks(context);
    }

    public void populateNumbers(){
        for(Event e : eventList){
            e.calculateDay();
            e.startTimeNumber = Integer.parseInt(e.getStartDate().substring(8,10));
            e.endTimeNumber = Integer.parseInt(e.getEndDate().substring(8,10));
    }
    }




    //test method please ignore
    public void printTimetable() {
        timetableHandler.print();
    }

}