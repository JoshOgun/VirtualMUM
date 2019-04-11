package Database.UserPreference;

import android.provider.BaseColumns;

public class UserPref {

    private int id;
    private String name;
    private String workPref;
    private String noDayPref;

    public UserPref(String name, String workPref, String noDayPref ) {
        this.workPref = workPref;
        this.noDayPref = noDayPref;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Inner class that defines the table contents */
    public static class VMUserPref implements BaseColumns {
        public static final String TABLE_NAME = "UserPref";
        public static final String COLUMN_NAME_TITLE2 = "Name";
        public static final String COLUMN_NAME_TITLE3 = "WorkPref";
        public static final String COLUMN_NAME_TITLE4 = "NoDayPref";
    }

    public static final String SQL_CREATE_USERPREF =
            "CREATE TABLE " + VMUserPref.TABLE_NAME + " (" +
                    VMUserPref._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    VMUserPref.COLUMN_NAME_TITLE2 + " TEXT," +
                    VMUserPref.COLUMN_NAME_TITLE3 + " TEXT," +
                    VMUserPref.COLUMN_NAME_TITLE4 + " TEXT)";

    public static final String SQL_DELETE_USERPREF =
            "DROP TABLE IF EXISTS " + VMUserPref.TABLE_NAME;
}
