package Database.Completion;

import android.provider.BaseColumns;

public class Completion {

    private int id;
    private float hoursForCompletion;

    public Completion(){}

    public Completion( float hoursForCompletion) {
        this.hoursForCompletion = hoursForCompletion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getHoursForCompletion() {
        return hoursForCompletion;
    }

    public void setHoursForCompletion(float hoursForCompletion) {
        this.hoursForCompletion = hoursForCompletion;
    }

    /* Inner class that defines the table contents */
    public static class VMCompletion implements BaseColumns {
        public static final String TABLE_NAME = "Completion_Data";
        public static final String COLUMN_NAME_TITLE2 = "Hours_For_Completion";

    }

    public static final String SQL_CREATE_COMPLETIONS =
            "CREATE TABLE " + VMCompletion.TABLE_NAME + " (" +
                    VMCompletion._ID + " INTEGER, " +
                    VMCompletion.COLUMN_NAME_TITLE2 + " INTEGER, " +
                    " FOREIGN KEY (_id) REFERENCES Task_Data(_id))";

    public static final String SQL_DELETE_COMPLETIONS =
            "DROP TABLE IF EXISTS " + VMCompletion.TABLE_NAME;
}
