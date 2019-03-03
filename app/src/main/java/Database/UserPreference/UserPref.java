package Database.UserPreference;

import android.provider.BaseColumns;

public class UserPref {

    private int id;
    private String workPref;
    private String noDayPref;

    public UserPref(String workPref, String noDayPref) {
        this.workPref = workPref;
        this.noDayPref = noDayPref;
    }

    public UserPref(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkPref() {
        return workPref;
    }

    public void setWorkPref(String workPref) {
        this.workPref = workPref;
    }

    public String getNoDayPref() {
        return noDayPref;
    }

    public void setNoDayPref(String noDayPref) {
        this.noDayPref = noDayPref;
    }

    /* Inner class that defines the table contents */
    public static class VMUserPref implements BaseColumns {
        public static final String TABLE_NAME = "UserPref";
        public static final String COLUMN_NAME_TITLE2 = "WorkPref";
        public static final String COLUMN_NAME_TITLE3 = "NoDayPref";
    }

    public static final String SQL_CREATE_TASKS =
            "CREATE TABLE " + VMUserPref.TABLE_NAME + " (" +
                    VMUserPref._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    VMUserPref.COLUMN_NAME_TITLE2 + " TEXT," +
                    VMUserPref.COLUMN_NAME_TITLE3 + " TEXT)";

    public static final String SQL_DELETE_TASKS =
            "DROP TABLE IF EXISTS " + VMUserPref.TABLE_NAME;
}
