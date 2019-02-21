package Database.Task;

import android.provider.BaseColumns;


public class Task_Data {

    private int id;
    private String name;
    private String startDate;
    private String dueDate;
    private String difficulty;
    private String priority;
    private String estimatedHours;
    private String completed;

    public Task_Data(int id, String name, String startDate, String dueDate, String difficulty, String priority, String estimatedHours, String completed) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.difficulty = difficulty;
        this.priority = priority;
        this.estimatedHours = estimatedHours;
        this.completed = completed;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(String estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

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

    public static final String SQL_CREATE_TASKS =
            "CREATE TABLE " + VMTask.TABLE_NAME + " (" +
                    VMTask._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    VMTask.COLUMN_NAME_TITLE2 + " TEXT," +
                    VMTask.COLUMN_NAME_TITLE3 + " TEXT," +
                    VMTask.COLUMN_NAME_TITLE4 + " TEXT," +
                    VMTask.COLUMN_NAME_TITLE5 + " INTEGER," +
                    VMTask.COLUMN_NAME_TITLE6 + " INTEGER," +
                    VMTask.COLUMN_NAME_TITLE7 + " REAL," +
                    VMTask.COLUMN_NAME_TITLE8 + " INTEGER)";
    public static final String SQL_DELETE_TASKS =
            "DROP TABLE IF EXISTS " + VMTask.TABLE_NAME;

}
