package Database.Timetable;

import java.util.Date;
import android.provider.BaseColumns;

public class Timetable {

    private int id;
    private String date;
    private int taskID;
    private int eventID;
    private float duration;
    private int completed;

    public Timetable(){}

    public Timetable(String date, int taskID, int eventID , float duration, int completed){
        this.date = date;
        this.taskID = taskID;
        this.eventID = eventID;
        this.duration = duration;
        this.completed = completed;
    }

    /*
    public Timetable(String date, int eventID, float duration, int completed){
        this.date = date;
        this.eventID = eventID;
        this.duration = duration;
        this.completed = completed;
    }
    */

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public int getTaskID(){
        return taskID;
    }

    public void setTaskID(int taskID){
        this.taskID = taskID;
    }

    public int getEventID(){
        return eventID;
    }

    public void setEventID(int eventID){
        this.eventID = eventID;
    }

    public float getDuration(){
        return duration;
    }

    public void setDuration(float duration){
        this.duration = duration;
    }

    public int getCompleted(){
        return completed;
    }

    public void setCompleted(int completed){
        this.completed = completed;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }


    /* Inner class that defines the table contents */
    public static class VMTimetable implements BaseColumns {
        public static final String TABLE_NAME = "Timetable_Data";
        public static final String COLUMN_NAME_TITLE2 = "Date";
        public static final String COLUMN_NAME_TITLE3 = "Task_ID";
        public static final String COLUMN_NAME_TITLE4 = "Event_ID";
        public static final String COLUMN_NAME_TITLE5 = "Duration";
        public static final String COLUMN_NAME_TITLE6 = "Completed";

    }

    public static final String SQL_CREATE_TIMETABLES =
            "CREATE TABLE " + VMTimetable.TABLE_NAME + " (" +
                    VMTimetable._ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                    VMTimetable.COLUMN_NAME_TITLE2 + " STRING," +
                    VMTimetable.COLUMN_NAME_TITLE3 + " INTEGER," +
                    VMTimetable.COLUMN_NAME_TITLE4 + " INTEGER," +
                    VMTimetable.COLUMN_NAME_TITLE5 + " REAL," +
                    VMTimetable.COLUMN_NAME_TITLE6 + " INTEGER " +
                    " FOREIGN KEY (Task_ID) REFERENCES Task_Data(_ID) " +
                    " FOREIGN KEY (Event_ID) REFERENCES Event_Data(_ID))";

    public static final String SQL_DELETE_TIMETABLES =
            "DROP TABLE IF EXISTS " + VMTimetable.TABLE_NAME;

}
