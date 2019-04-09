package Database.Task;

import android.provider.BaseColumns;


public class Task {

    private int id;
    private String name;
    private String startDate;
    private String dueDate;
    private int difficulty;
    private int priority;
    private double estimatedHours;
    private int completed;

    public int getWeight() {
        weight = priority * difficulty;
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private int weight;

    public Task(String name, String startDate, String dueDate, int difficulty, int priority, double estimatedHours, int completed) {
        this.name = name;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.difficulty = difficulty;
        this.priority = priority;
        this.estimatedHours = estimatedHours;
        this.completed = completed;
        weight = priority * difficulty;
    }

    public Task(){

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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        weight = priority * difficulty;

    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
        weight = priority * difficulty;

    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
