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
    public int startOfDay = 8;
    public int endOfDay = 17;
    public int startOfWeek = 1;
    public int endOfWeek = 6;

    VMDbHelper db;

    public User() {
        taskList = new ArrayList<Task>();
        eventList = new ArrayList<Event>();
        timetableHandler = new assignTimetable(this);

    }

    public void setGoogleURL(String url) { //adds google url to user
        this.googleURL = url;
    }

    public void updateTasks(Context context){

        db = new VMDbHelper(context);
        taskList = db.getAllTasks();
        timetableHandler.orderTasks();
        db.closeDB();
    }

    public void updateEvents(Context context){

        db = new VMDbHelper(context);
        eventList = db.getAllEvents();
        populateNumbers();
        timetableHandler.updateEvents();
        db.closeDB();
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