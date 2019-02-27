package Database.Progress;


import android.provider.BaseColumns;

public class Progress {

    private int id;
    private float progress;
    private double hoursSpent;

    public Progress(){}

    public Progress(float progress, double hoursSpent) {
        this.hoursSpent = hoursSpent;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(double hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    /* Inner class that defines the table contents */
    public static class VMProgress implements BaseColumns {
        public static final String TABLE_NAME = "Progress_Data";
        public static final String COLUMN_NAME_TITLE2 = "Hours_Spent";
        public static final String COLUMN_NAME_TITLE3 = "Estimated_Hours";

    }

    public static final String SQL_CREATE_PROGRESS =
            "CREATE TABLE " + VMProgress.TABLE_NAME + " (" +
                    VMProgress._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    VMProgress.COLUMN_NAME_TITLE2 + " INTEGER," +
                    VMProgress.COLUMN_NAME_TITLE3 + " INTEGER)";

    public static final String SQL_DELETE_PROGRESS =
            "DROP TABLE IF EXISTS " + VMProgress.TABLE_NAME;

}
