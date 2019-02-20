package Database;

import android.provider.BaseColumns;

public final class VMContract {

    private VMContract() {}

    /* Inner class that defines the table contents */
    public static class VMTask implements BaseColumns {
        public static final String TABLE_NAME = "Task_Data";
        public static final String COLUMN_NAME_TITLE2 = "Name";
        public static final String COLUMN_NAME_TITLE3 = "Start_Date";
        public static final String COLUMN_NAME_TITLE4 = "Due_Date";
        public static final String COLUMN_NAME_TITLE5 = "Difficulty";
        public static final String COLUMN_NAME_TITLE6 = "Priority";
        public static final String COLUMN_NAME_TITLE7 = "Estimated_Hours";
        public static final String COLUMN_NAME_TITLE8 = "Completed";
    }

    private static final String SQL_CREATE_TASKS =
            "CREATE TABLE " + VMTask.TABLE_NAME + " (" +
                    VMTask._ID + " INTEGER PRIMARY KEY," +
                    VMTask.COLUMN_NAME_TITLE2 + " TEXT," +
                    VMTask.COLUMN_NAME_TITLE3 + " TEXT," +
                    VMTask.COLUMN_NAME_TITLE4 + " TEXT," +
                    VMTask.COLUMN_NAME_TITLE5 + " INTEGER," +
                    VMTask.COLUMN_NAME_TITLE6 + " INTEGER," +
                    VMTask.COLUMN_NAME_TITLE7 + " REAL," +
                    VMTask.COLUMN_NAME_TITLE8 + " INTEGER)";

    private static final String SQL_DELETE_TASKS =
            "DROP TABLE IF EXISTS " + VMTask.TABLE_NAME;


    /* Inner class that defines the table contents */
    public static class VMEvent implements BaseColumns {
        public static final String TABLE_NAME = "Event_Data";
        public static final String COLUMN_NAME_TITLE2 = "Name";
        public static final String COLUMN_NAME_TITLE3 = "Desc";
        public static final String COLUMN_NAME_TITLE4 = "Start_Date";
        public static final String COLUMN_NAME_TITLE5 = "Due_Date";
        public static final String COLUMN_NAME_TITLE6 = "Location";
    }

    // LOCATION IS STORED AS TEXT FOR NOW
    private static final String SQL_CREATE_EVENTS =
            "CREATE TABLE " + VMEvent.TABLE_NAME + " (" +
                    VMEvent._ID + " INTEGER PRIMARY KEY," +
                    VMEvent.COLUMN_NAME_TITLE2 + " TEXT," +
                    VMEvent.COLUMN_NAME_TITLE3 + " TEXT," +
                    VMEvent.COLUMN_NAME_TITLE4 + " TEXT," +
                    VMEvent.COLUMN_NAME_TITLE5 + " TEXT," +
                    VMEvent.COLUMN_NAME_TITLE6 + " TEXT)";

    private static final String SQL_DELETE_EVENTS =
            "DROP TABLE IF EXISTS " + VMEvent.TABLE_NAME;
}
