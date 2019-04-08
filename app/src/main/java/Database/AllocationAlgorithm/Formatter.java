package Database.AllocationAlgorithm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class Formatter {
// you just call user.convertor() and it will do the rest; the return is a list with all the dates and the tasks on them

    public int startOfDay;
    public int endOfDay;
    public String[][] timeTable;

    public Formatter(int end, int start, String[][] table){
        this.endOfDay = end;
        this.startOfDay = start;
        this.timeTable = table;
    }

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
                            sa[1] = timeTable[row][index].substring(6);

                            // event ID
                            sa[2] = "0";

                            // duration
                            sa[3] = "" + count;
                            count = 0;

                            // completed
                            sa[4] = "0";

                            // could put slashes or something if you want an easy way to use .split on the
                            // strings later for storing
                            list.add(sa[0] +  "/" + sa[1] + "/" + sa[2] + "/" + sa[3] + "/" + sa[4]);
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
                    sa[1] = timeTable[row][index].substring(6);

                    // event ID
                    sa[2] = "0";

                    // duration
                    sa[3] = "" + count;
                    count = 0;

                    // completed
                    sa[4] = "0";

                    // could put slashes or something if you want an easy way to use .split on the
                    // strings later for storing
                    list.add(sa[0] +  "/" + sa[1] + "/" + sa[2] + "/" + sa[3] + "/" + sa[4]);
                    index--;
                }
            }
        }

        return list;
    }

}


