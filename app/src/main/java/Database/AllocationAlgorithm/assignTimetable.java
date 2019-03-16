package Database.AllocationAlgorithm;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Database.Event.Event;
import Database.Task.Task;

public class assignTimetable {
    // the working array used for the task hours of the current day.
    int[] taskHours;

    // the original values for the task hours that should be fitted into each day
    // (but not always will be).
    int[] origTaskHours;

    // array for hours missed for each task;
    int[] missedTaskHours;

    // start and end of working day
    // (run with a 24 hours clock so 8am becomes 8, 6pm becomes 18)
    int startOfDay;
    int endOfDay;

    // start and end of working week
    // (starts from Sunday and ends with Saturday (0 - 6))
    int startOfWeek;
    int endOfWeek;

    // the current user of the system
    User user;

    // ordered array of task references
    Task[] taskArray;

    // working version of the final timetable for the user's working week
    String[][] testTimetable;

    /**
     * Constructor.
     *
     *
     *            the user object the system will use. It contains all the
     *            preferences which this class needs to make an accurate timetable.
     *
     *            the saved values of the missed task hours.
     */
    public assignTimetable(User u) {
        this.user = u;
        this.testTimetable = new String[7][24];
        this.startOfDay = u.startOfDay;
        this.endOfDay = u.endOfDay;
        this.startOfWeek = u.startOfWeek;
        this.endOfWeek = u.endOfWeek;
        populateTimetable();
    }

    /**
     * Populates a 7x24 string matrix representation of a weekly timetable.
     * Currently outputs 'free' for each free hour and the name of the event for
     * each event in the user's event list, week structured Sunday - Saturday
     */
    private void populateTimetable() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 24; j++) {
                testTimetable[i][j] = "free";
            }
        }

        // adds events to timetable
        for (Event e : user.eventList) {
            for (int i = e.startTimeNumber; i < e.endTimeNumber; i++) { // populates each hour the event is running for
                testTimetable[e.dayNumber][i] = e.getName(); // currently only works in hour chunks as discussed
            }
        }
    }

    /**
     * Called when a new event is added. It adds the event to the timetable, so that
     * the algorithm can calculate the timetable with tasks in it without any
     * clashes.
     */
    public void updateEvents() {
        for (Event e : user.eventList) {
            for (int i = e.startTimeNumber; i < e.endTimeNumber; i++) {
                testTimetable[e.dayNumber][i] = e.getName(); // puts event into correct slot in timetable
            }
        }
    }

    /**
     * Prints out the timetable in the current moment. Used for testing only.
     */
    public void print() {
        for (int i = 0; i < 7; i++) {
            Log.d("Timetable", startOfDay + "am: "); // marks the user determined start of the day
            for (int j = startOfDay; j < endOfDay; j++) { // working day visualisation
                Log.d("Timetable","[" + testTimetable[i][j] + "]" + " ");
            }
            Log.d("Gap", "\n");
        }
    }

    /**
     * Orders the tasks in the taskList based on their "weight". Here weight means
     * the value obtained by multiplying priority and difficulty.
     */
    public void orderTasks() {
        List<Task> t = user.taskList; // grabs user's list of tasks
        Collections.sort(t, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                if (t1.getWeight() > t2.getWeight())
                    return -1;
                if (t1.getWeight() < t2.getWeight())
                    return 1;
                return 0;
            }
        });
        // the code above sorts the arrayList descendingly by weight
        // i.e. most weighted -> least weighted
        Task[] taskArray = new Task[t.size()];
        for (int i = 0; i < t.size(); i++) {
            taskArray[i] = t.get(i);
        }
        this.taskArray = taskArray;
        // dumps tasks into array so they're easier to work with
        // (will remain static until a task is added/edited)
        setHours(taskArray); // implicitly calculates the hours for each task after sorting
        work(this.startOfDay, this.endOfDay, this.startOfWeek, this.endOfWeek);
    }

    /**
     * Assigns a certain number of hours for each task and puts them in the order
     * given by the taskArray. priority x difficulty values: time allocated for that
     * task: 1x1 - 3x3 1 hour 3x4 - 4x5 2 hours 5x5 3 hours
     *
     * @param taskArray
     *            the ordered array of tasks
     */
    public void setHours(Task[] taskArray) {
        // assigns correct amount of space
        int[] hours = new int[taskArray.length];
        for (int i = 0; i < taskArray.length; i++) {
            if (0 <= taskArray[i].getWeight() && taskArray[i].getWeight() <= 10) {
                hours[i] = 1;
            } else if (11 <= taskArray[i].getWeight() && taskArray[i].getWeight() <= 20) {
                hours[i] = 2;
            } else {
                hours[i] = 3;
            }
        }

        this.taskHours = hours;
        origTaskHours = new int[hours.length];
        System.arraycopy(hours, 0, origTaskHours, 0, origTaskHours.length);

        missedTaskHours = new int[hours.length];
    }

    /**
     * Essentially the main method of this class. It sets up a lot of the variables
     * and makes a loop to organize each day one at a time. Parameters also allow
     * this method to be called for a single day and after a certain point in that
     * day.
     *
     * @param startOfDay
     *            the time of the start of the working day (e.g. 8am would be 8)
     * @param endOfDay
     *            the time of the end of the working day (e.g. 6pm would be 18)
     * @param startOfWeek
     *            the start of the working week (e.g. Monday would be 1)
     * @param endOfWeek
     *            the end of the working week (e.g. Sunday would be 0 (it goes 0 - 6
     *            and loops back))
     */
    public void work(int startOfDay, int endOfDay, int startOfWeek, int endOfWeek) {
        cleanTimetable(startOfWeek, endOfWeek); // removes all currently assigned tasks
        int counter = 0; // counter variable for no of free hours at once
        // why the % in the for??
        for (int i = startOfWeek; i != endOfWeek; i = (i + 1) % 7) { // iterates through working week
            freeTimeInDay(i);
            int index = 0;
            // [length of interval] [start time of interval] [end time of interval]
            int[][] freeTimes = new int[7][3];
            boolean startSet = false;
            for (int j = startOfDay; j < endOfDay + 1; j++) { // iterates through working day
                if (testTimetable[i][j].equals("free") && j != endOfDay) { // if free that hour
                    counter++; // add to cumulative count of the current free interval
                    if (!startSet) {
                        freeTimes[index][1] = j;
                        startSet = true;
                    }
                } else if (counter != 0) {
                    // first part keeps the length of the interval, second part keeps the time of
                    // the day where that interval happens. Need this as when sorted I lose track of
                    // when in the day the intervals are
                    freeTimes[index][0] = counter;
                    freeTimes[index][2] = j;
                    counter = 0;
                    index++;
                    startSet = false;
                }
            }

            // gonna need a method to clear the 0's of the array, or some methods to change
            // the size of the array constantly, this is just for now so it's not too hard
            // for the arrange day method
            int number = 0;
            for (int x = 0; x < freeTimes.length; x++) {
                if (freeTimes[x][0] != 0) {
                    number++;
                }
            }

            int[][] temp = new int[number][3];
            for (int y = 0; y < number; y++) {
                temp[y][0] = freeTimes[y][0];
                temp[y][1] = freeTimes[y][1];
                temp[y][2] = freeTimes[y][2];
            }

            // this is how you sort 2d arrays apparently
            java.util.Arrays.sort(temp, new java.util.Comparator<int[]>() {
                public int compare(int[] a, int[] b) {
                    if (a[0] > b[0]){
                        return 1;
                    }
                    else if(a[0] < b[0]){
                        return -1;
                    }
                    else{
                        return 0;
                    }
                }
            });

            arrangeDay(i, temp);

            if (workDone()) {
                user.timeTable = testTimetable;
            }

            taskHours = new int[origTaskHours.length];
            System.arraycopy(origTaskHours, 0, taskHours, 0, taskHours.length);
        }

        // if any task could not fit in the week, add it to the weekend
        int hoursLeft = 0;
        for (int i : missedTaskHours) {
            hoursLeft += i;
        }
        if (hoursLeft > 0) {
            fixTable(6);
        }
    }

    /**
     * Arrange the day by adding in tasks ("highest" to "lowest" task) in 1 hour
     * chunks. Start with the task with the most hours needed and search through the
     * array of free times until you find a big enough gap, i.e. look at the
     * smallest free time and see if that's enough, then the next biggest etc up
     * until you find a big enough slot or run out of spaces in the day.
     *
     * @param day
     *            the day that is being organized
     * @param freeTimes
     *            a 2d array for the start and duration of every free interval of
     *            the day
     */
    private void arrangeDay(int day, int[][] freeTimes) {
        // freeTimes are sorted increasingly while task hours are sorted decreasingly
        // (if confused)
        for (int i = 0; i < taskHours.length; i++) {
            int j = 0;
            while (taskHours[i] > 0 && j < freeTimes.length) {
                if (taskHours[i] <= freeTimes[j][0]) {
                    for (int time = freeTimes[j][1]; time < freeTimes[j][2]; time++) {
                        if (taskHours[i] > 0) {
                            testTimetable[day][time] = "TASK: " + taskArray[i].getWeight();
                            // decrease interval length
                            freeTimes[j][0]--;
                            // increase interval start time
                            freeTimes[j][1]++;
                            // decrease length of task
                            taskHours[i]--;
                        }
                    }

                    // if a free time interval is gone, remove it from the array
                    // if there is only 1 record in the freeTimes array and it has an interval of 0,
                    // then reset the array and end method
                    if (freeTimes[j][0] == 0 && freeTimes.length > 1) {
                        int[][] temp = new int[freeTimes.length - 1][3];

                        int y = 0;
                        for (int x = 0; x < freeTimes.length; x++) {
                            if (x != j) {
                                temp[y][0] = freeTimes[x][0];
                                temp[y][1] = freeTimes[x][1];
                                temp[y][2] = freeTimes[x][2];
                                y++;
                            }
                        }

                        freeTimes = temp;
                    } else {
                        break;
                    }
                }
                j++;
            }

            // if it goes through the while and it doesn't find a big enough slot, fit as
            // much as possible into the biggest slot, take that away from the task in
            // taskHours array, then run again.
            if (taskHours[i] > 0) {
                for (int time = freeTimes[j - 1][1]; time < freeTimes[j - 1][2]; time++) {
                    testTimetable[day][time] = "TASK: " + taskArray[i].getName();
                    // decrease interval length
                    freeTimes[j - 1][0]--;
                    // increase interval start time
                    freeTimes[j - 1][1]++;
                    // decrease length of task
                    taskHours[i]--;
                }

                // as the task hours is bigger than the free time interval, it has to remove it
                // from the freeTimes array
                int[][] temp = new int[freeTimes.length - 1][3];
                int y = 0;
                for (int x = 0; x < freeTimes.length; x++) {
                    if (x != j - 1) {
                        temp[y][0] = freeTimes[x][0];
                        temp[y][1] = freeTimes[x][1];
                        temp[y][2] = freeTimes[x][2];
                        y++;
                    }
                }

                freeTimes = temp;
                // i-- because the hours for this task are not yet zero
                i--;
            }
        }
    }

    /**
     * Calculates the amount of free time in the given day and changes the taskHours
     * array accordingly so that it can be fitted into the day. Anything removed
     * from the taskHours array goes into the missedTaskHours array so it can be
     * assigned for the weekends.
     *
     * @param day
     *            the given day to do the calculation for
     * @return always returns true, kept boolean as it is used in our if statement
     */
    private void freeTimeInDay(int day) {
        // get total hours of free time in the day
        int freeTimeCount = 0;
        for (int h = startOfDay; h < endOfDay; h++) {
            if (testTimetable[day][h].equals("free")) {
                freeTimeCount++;
            }
        }

        // get total hours of tasks to assign
        int totalHours = 0;
        for (int i = 0; i < origTaskHours.length; i++) {
            totalHours += origTaskHours[i];
        }

        // if there is more free time than hours of work return true; else fix the
        // taskHours array so it fits
        while (totalHours > freeTimeCount) {
            int[] temp = new int[taskHours.length - 1];
            int i = 0;
            for (i = 0; i < temp.length; i++) {
                temp[i] = taskHours[i];
            }

            // take away last (least important) task
            totalHours -= taskHours[i];
            // add it as a missed task
            missedTaskHours[i] += taskHours[i];
            // change the taskHours array so that we have a smaller one that might fit in
            // the current day
            taskHours = temp;
        }
    }

    /**
     * Checks if there is any tasks that haven't been assigned yet for the current
     * day.
     *
     * @return true if there are any left; false otherwise
     */
    private boolean workDone() {
        for (int i = 0; i < taskHours.length; i++) {
            if (taskHours[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes tasks from a certain section of the timetable so it can be
     * recalculated.
     *
     * @param startOfWeek
     *            the start of the section
     * @param endOfWeek
     *            the end of the section
     */

    private void cleanTimetable(int startOfWeek, int endOfWeek) {
        for (int i = startOfWeek; i < endOfWeek; i++) {
            for (int j = startOfDay; j < endOfDay; j++) {
                if (isTask(testTimetable[i][j])) {
                    testTimetable[i][j] = "free";
                }
            }
        }
    }

    /**
     * Method do determine whether a slot is taken up by a task.
     *
     *            the name of the task
     * @return true if it is a task; false otherwise
     */
    private boolean isTask(String name) {
        if (name.startsWith("TASK: ")) {
            return true;
        }
        return false;
    }

    /**
     * Could have reused the code from work() but it would have made it very messy
     * because of the cleanTimetable() method so I just made a new method with just
     * the things I needed. It does shorten the code a little. This method is for
     * just Saturday and Sunday and will not be called for other days.
     *
     * @param day
     *            the day (it will be either 6 - Saturday OR 0 - Sunday, it will
     *            never take other values)
     */
    private void fixTable(int day) {
        cleanTimetable(day, day + 1);
        int index = 0;
        int counter = 0;
        // [length of interval] [start time of interval] [end time of interval]
        int[][] freeTimes = new int[7][3];
        boolean startSet = false;
        for (int j = startOfDay; j < endOfDay + 1; j++) { // iterates through working day
            if (testTimetable[day][j].equals("free") && j != endOfDay) { // if free that hour
                counter++; // add to cumulative count of the current free interval
                if (!startSet) {
                    freeTimes[index][1] = j;
                    startSet = true;
                }
            } else if (counter != 0) {
                // first part keeps the length of the interval, second part keeps the time of
                // the day where that interval happens. Need this as when sorted I lose track of
                // when in the day the intervals are
                freeTimes[index][0] = counter;
                freeTimes[index][2] = j;
                counter = 0;
                index++;
                startSet = false;
            }
        }

        int number = 0;
        for (int x = 0; x < freeTimes.length; x++) {
            if (freeTimes[x][0] != 0) {
                number++;
            }
        }

        int[][] temp = new int[number][3];
        for (int y = 0; y < number; y++) {
            temp[y][0] = freeTimes[y][0];
            temp[y][1] = freeTimes[y][1];
            temp[y][2] = freeTimes[y][2];
        }

        // this is how you sort 2d arrays apparently
        java.util.Arrays.sort(temp, new java.util.Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                if (a[0] > b[0]){
                    return 1;
                }
                else if(a[0] < b[0]){
                    return -1;
                }
                else{
                    return 0;
                }
                //return Integer.compare(a[0], b[0]);
            }
        });

        freeTimes = temp;

        for (int i = 0; i < missedTaskHours.length; i++) {
            int j = 0;
            while (missedTaskHours[i] > 0 && j < freeTimes.length) {
                if (missedTaskHours[i] <= freeTimes[j][0]) {
                    for (int time = freeTimes[j][1]; time < freeTimes[j][2]; time++) {
                        if (missedTaskHours[i] > 0) {
                            testTimetable[day][time] = "TASK: " + taskArray[i].getName();
                            // decrease interval length
                            freeTimes[j][0]--;
                            // increase interval start time
                            freeTimes[j][1]++;
                            // decrease length of task
                            missedTaskHours[i]--;
                        }
                    }

                    // if a free time interval is gone, remove it from the array
                    // if there is only 1 record in the freeTimes array and it has an interval of 0,
                    // then reset the array and end method
                    if (freeTimes[j][0] == 0 && freeTimes.length > 1) {
                        temp = new int[freeTimes.length - 1][3];

                        int y = 0;
                        for (int x = 0; x < freeTimes.length; x++) {
                            if (x != j) {
                                temp[y][0] = freeTimes[x][0];
                                temp[y][1] = freeTimes[x][1];
                                temp[y][2] = freeTimes[x][2];
                                y++;
                            }
                        }

                        freeTimes = temp;
                    } else {
                        break;
                    }
                }
                j++;
            }

            // if it goes through the while and it doesn't find a big enough slot, fit as
            // much as possible into the biggest slot, take that away from the task in
            // taskHours array, then run again.
            if (missedTaskHours[i] > 0) {
                for (int time = freeTimes[j][1]; time < freeTimes[j][2]; time++) {
                    testTimetable[day][time] = "TASK: " + taskArray[i].getName();
                    // decrease interval length
                    freeTimes[j][0]--;
                    // increase interval start time
                    freeTimes[j][1]++;
                    // decrease length of task
                    missedTaskHours[i]--;
                }

                // as the task hours is bigger than the free time interval, it has to remove it
                // from the freeTimes array
                temp = new int[freeTimes.length - 1][3];
                int y = 0;
                for (int x = 0; x < freeTimes.length; x++) {
                    if (x != j) {
                        temp[y][0] = freeTimes[x][0];
                        temp[y][1] = freeTimes[x][1];
                        temp[y][2] = freeTimes[x][2];
                        y++;
                    }
                }

                freeTimes = temp;
                // i-- because the hours for this task are not yet zero
                i--;
            }
        }

        // if it didn't fit on Saturday, call the method again on Sunday.
        int hoursLeft = 0;
        for (int i : missedTaskHours) {
            hoursLeft += i;
        }
        if (hoursLeft > 0) {
            fixTable(0);
        }
    }

}

