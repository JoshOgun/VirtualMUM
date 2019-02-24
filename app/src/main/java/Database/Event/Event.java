package Database.Event;

import android.provider.BaseColumns;


public class Event {

    private int id;
    private String name;
    private String startDate;
    private String endDate;
    private String Location;

    public Event(String name, String startDate, String endDate, String location) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        Location = location;
    }

    public Event(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    /* Inner class that defines the table contents */
    public static class VMEvent implements BaseColumns {
        public static final String TABLE_NAME = "Event_Data";
        public static final String COLUMN_NAME_TITLE2 = "Name";
        public static final String COLUMN_NAME_TITLE3 = "Start_Date";
        public static final String COLUMN_NAME_TITLE4 = "End_Date";
        public static final String COLUMN_NAME_TITLE5 = "Location";
    }

    public static final String SQL_CREATE_EVENTS =
            "CREATE TABLE " + Event.VMEvent.TABLE_NAME + " (" +
                    Event.VMEvent._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Event.VMEvent.COLUMN_NAME_TITLE2 + " TEXT," +
                    Event.VMEvent.COLUMN_NAME_TITLE3 + " TEXT," +
                    Event.VMEvent.COLUMN_NAME_TITLE4 + " TEXT," +
                    Event.VMEvent.COLUMN_NAME_TITLE5 + " TEXT)";

    public static final String SQL_DELETE_EVENTS =
            "DROP TABLE IF EXISTS " + VMEvent.TABLE_NAME;
}
