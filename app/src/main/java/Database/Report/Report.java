package Database.Report;

import android.provider.BaseColumns;

public class Report {

    private int id;
    private double hoursSpent;
    private float estimatedHours;

    public Report(){}

    public Report(double hoursSpent, float estimatedHours) {
        this.hoursSpent = hoursSpent;
        this.estimatedHours = estimatedHours;
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

    public float getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(float estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    /* Inner class that defines the table contents */
    public static class VMReport implements BaseColumns {
        public static final String TABLE_NAME = "Report_Data";
        public static final String COLUMN_NAME_TITLE2 = "Hours_Spent";
        public static final String COLUMN_NAME_TITLE3 = "Estimated_Hours";

    }

    public static final String SQL_CREATE_REPORTS =
            "CREATE TABLE " + VMReport.TABLE_NAME + " (" +
                    VMReport._ID + " INTEGER, " +
                    VMReport.COLUMN_NAME_TITLE2 + " INTEGER," +
                    VMReport.COLUMN_NAME_TITLE3 + " INTEGER" +
                    " FOREIGN KEY (_ID) REFERENCES Task_Data(_ID))";

    public static final String SQL_DELETE_REPORTS =
            "DROP TABLE IF EXISTS " + VMReport.TABLE_NAME;
}
