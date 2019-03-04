package AllocationAlgorithm;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
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
    }

    public void updateEvents(Context context){

        db = new VMDbHelper(context);
        eventList = db.getAllEvents();
    }

//    public boolean addEvent(String name, Date startDate, Date endDate, String recurrance) {  //adds an event to the user's event list
//        if(!eventList.isEmpty()) {
//            for(Event e:eventList) {
//                if(e.getName().equals(name)){
//                    return false;
//                }
//            }
//        }
//        Event e = new Event(name, startDate, endDate, recurrance);
//        eventList.add(e);
//        timetableHandler.updateEvents();
//        return true;
//    }

//    public boolean addTask(String name, int priority, int difficulty, Date dueDate) { //method that adds a new task to the taskList
//        if (!taskList.isEmpty()){
//            for (Task t:taskList){ //checks to see if another task has the same name, in which case it rejects the addition, (task name as primary key)
//                if(t.getName().equals(name)) {
//                    return false;
//                }
//            }
//        }
//        Task t = new Task(name, priority, difficulty, dueDate);
//        taskList.add(t);
//        timetableHandler.orderTasks();
//        return true; //successful adding of task
//    }

    //test method please ignore
    public void printTimetable() {
        timetableHandler.print();
    }

}