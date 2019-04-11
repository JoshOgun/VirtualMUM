package Database.AllocationAlgorithm;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
        List<String> taskToDB = convertor();
        //delete timetabletasks here
        for (String str : taskToDB){
            String[] parts = str.split("/");
            String date = parts[0];
            int taskId = Integer.parseInt(parts[1]);
            float duration = (float)Integer.parseInt(parts[3]);
            db.insertTimetable(date, taskId, 0, duration, 0);
        }
        db.closeDB();

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



// you just call user.convertor() and it will do the rest; the return is a list with all the dates and the tasks on them

    // just to get rid of the slashes, doesn't do much more
    private String transform(String s) {
        // format of s is yyyy/MM/dd
        String string = s.substring(0, 2) + s.substring(3, 5) + s.substring(6);
        return string;
    }

    // if you want to do anything else with the date, here are some useful methods
    // so you don't have to search for them
    public void test() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(transform(dateFormat.format(currentDate)));

        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        // manipulate date
        c.add(Calendar.YEAR, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, 1); // same with c.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 1);
        c.add(Calendar.MINUTE, 1);
        c.add(Calendar.SECOND, 1);
        Calendar cal = Calendar.getInstance();

        // convert calendar to date
        Date currentDatePlusOne = c.getTime();
        System.out.println(transform(dateFormat.format(currentDate)));
    }

    public List<String> convertor() {
        List<String> list = new ArrayList<String>();

        // get todays date
        // since the times in the timetable are whole hours (i.e. 10am, 11am, etc.)
        // don't really need date
        // to contain the time so I just hard coded it in so here time is the hours
        // (from a 24 hour clock)
        // and minutes are always 00. Might want to change that somehow later but idk
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        // find out which day it is
        Calendar cal = Calendar.getInstance();
        // get all the dates in the right order, putting Sunday first and Saturday last
        // (regardless of how they are in the calendar)
        List<String> dates = new ArrayList<String>();

        // position 0 holds the date, position 1 holds the day of the week
        String[][] stringArray = new String[7][2];
        for (int i = 0; i < 7; i++) {
            stringArray[i][0] = transform(dateFormat.format(date));
            // for some reason Sunday is 1 and Saturday is 7???
            stringArray[i][1] = "" + (cal.get(Calendar.DAY_OF_WEEK) - 1);

            // add one to the day, set the new date and repeat
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        }

        // add the dates into the dates list (from Sunday to Saturday)
        // not sure what to do if the date has no tasks on it?
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (stringArray[j][1].equals("" + i)) {
                    dates.add(stringArray[j][0]);
                }
            }
        }

        for (int row = 0; row < 7; row++) {
            // sa is each entry for a task, hence it has 5 attributes as discussed
            String[] sa = new String[5];
            int count = 0;
            String currentTask = null;
            for (int index = startOfDay; index < endOfDay + 1; index++) {
                if (timeTable[row][index].substring(0, 4).equals("TASK")) {
                    if (currentTask == null) {
                        currentTask = timeTable[row][index].substring(6);
                        count++;
                    } else {
                        if (timeTable[row][index].substring(6).equals(currentTask)) {
                            count++;
                        } else {
                            String time = "";
                            if (index - count < 10) {
                                time += "0";
                            }
                            // date
                            sa[0] = dates.get(row) + time + (index - count) + "00";
                            currentTask = null;

                            // task ID
                            // don't know where that is stored exactly
                            sa[1] = timeTable[row][index-1].substring(6);

                            // event ID
                            sa[2] = "0";

                            // duration
                            sa[3] = "" + count;
                            count = 0;

                            // completed
                            sa[4] = "0";

                            // could put slashes or something if you want an easy way to use .split on the
                            // strings later for storing
                            list.add(sa[0] + "/" + sa[1] + "/" +  sa[2] + "/" + sa[3] + "/" + sa[4]);
                            index--;
                        }
                    }

                } else if (currentTask != null) {
                    String time = "";
                    if (index - count < 10) {
                        time += "0";
                    }
                    // date
                    sa[0] = dates.get(row) + time + (index - count) + "00";
                    currentTask = null;

                    // task ID
                    // don't know where that is stored exactly
                    sa[1] = timeTable[row][index-1].substring(6);

                    // event ID
                    sa[2] = "0";

                    // duration
                    sa[3] = "" + count;
                    count = 0;

                    // completed
                    sa[4] = "0";

                    // could put slashes or something if you want an easy way to use .split on the
                    // strings later for storing
                    list.add(sa[0] + "/" + sa[1] + "/" +  sa[2] + "/" + sa[3] + "/" + sa[4]);
                    index--;
                }
            }
        }

        return list;
    }

    // removes all the null fields in the 2D array, just to make it take less space
    private String[][] removeNull(String[][] sa) {
        int maxL = 0;
        int count = 0;

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < sa[i].length; j++) {
                if (sa[i][j] != null) {
                    count++;
                }
            }

            if (count > maxL) {
                maxL = count;
            }
            count = 0;
        }

        String[][] workArr = new String[7][maxL];

        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < maxL; y++) {
                workArr[x][y] = sa[x][y];
            }
        }

        return workArr;
    }

    // Shift all rows of the given array one "up", i.e. row 2 becomes row 1, row 1
    // becomes row 0, row 0 becomes row n - 1
    private String[][] changeOrder(String[][] sa) {
        String[][] workArr = new String[7][20];

        // Monday till Saturday
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < sa[x + 1].length; y++) {
                workArr[x][y] = sa[x + 1][y];
            }
        }

        // Sunday
        for (int i = 0; i < sa[0].length; i++) {
            workArr[6][i] = sa[0][i];
        }

        return removeNull(workArr);
    }

    // Same as getTasks but for events
    public String[][] getEvents() {
        String[][] sa = new String[7][20];
        String currEvent = "";
        int dur = 0;
        int startTime = 0;
        int time = 0;
        int index = 0;

        for (int day = 0; day < 7; day++) {
            for (String s : timeTable[day]) {
                if (!s.equals("free")) {
                    // this will fail if the event has a name which is less than 4 characters!!
                    if (currEvent.equals("") && !s.substring(0, 4).equals("TASK")) {
                        currEvent = s;
                        dur++;
                        startTime = time;
                    } else if (s.equals(currEvent)) {
                        dur++;
                    } else if (!s.equals(currEvent) && !s.substring(0, 4).equals("TASK")) {
                        // enter code for event ID later
                        sa[day][index] = startTime + "/" + dur + "/" + currEvent;
                        index++;
                        startTime += dur;
                        currEvent = s;
                        dur = 1;
                    } else if (!s.equals(currEvent) && !currEvent.equals("")) {
                        // enter code for event ID later
                        sa[day][index] = startTime + "/" + dur + "/" + currEvent;
                        index++;
                        currEvent = "";
                        dur = 0;
                    }
                }

                if (!currEvent.equals("") && s.equals("free")) {
                    // enter code for event ID later
                    sa[day][index] = startTime + "/" + dur + "/" + currEvent;
                    index++;
                    currEvent = "";
                    dur = 0;
                }

                time++;
            }

            time = 0;
            index = 0;
        }

        return changeOrder(sa);
    }

    /**
     * Converts the list of tasks into a 2D String array, where the 0th position is
     * the tasks for Monday (as requested).
     *
     * @return A 2D String array containing the tasks of the week including their
     *         start times, duration and taskID
     */
    public String[][] getTasks() {
        String[][] sa = new String[7][10];
        List<String> list = convertor();

        String compare = list.get(0).substring(0, 8);
        int row = 0;
        int index = 0;
        for (String string : list) {
            // 0 - date; 1 - taskID; 2 - eventID; 3 - duration; 4 - completed
            String[] strArr = string.split("/");

            if (compare.equals(strArr[0].substring(0, 8))) {
                sa[row][index] = strArr[0].substring(8) + "/" + strArr[3] + "/" + strArr[1];
                index++;
            } else {
                row++;
                index = 0;
                sa[row][index] = strArr[0].substring(8) + "/" + strArr[3] + "/" + strArr[1];
                index++;
                compare = strArr[0].substring(0, 8);
            }
        }

        return removeNull(sa);
    }
}