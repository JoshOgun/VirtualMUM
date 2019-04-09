package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Database.AllocationAlgorithm.User;
import Database.Completion.Completion;
import Database.Event.Event;
import Database.Progress.Progress;
import Database.Report.Report;
import Database.Task.Task;
import Database.Timetable.Timetable;
import Database.UserPreference.UserPref;

public class VMDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VM.db";
    private User user;
    private Context context;

    public VMDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        user = new User();
    }

    // All the tables must be created here when the application is downloaded and opened
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Task.SQL_CREATE_TASKS);
        db.execSQL(Event.SQL_CREATE_EVENTS);
        db.execSQL(Report.SQL_CREATE_REPORTS);
        db.execSQL(Progress.SQL_CREATE_PROGRESS);
        db.execSQL(Completion.SQL_CREATE_COMPLETIONS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
       db.execSQL(Task.SQL_DELETE_TASKS);
       db.execSQL(Event.SQL_DELETE_EVENTS);
       db.execSQL(Report.SQL_DELETE_REPORTS);
       db.execSQL(Progress.SQL_DELETE_PROGRESS);
       db.execSQL(Completion.SQL_DELETE_COMPLETIONS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /* TASK Table Methods*/
    public long insertTask(String name, String startDate, String dueDate, int difficulty, int priority, double estimatedHours, int completed ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically - no need to add them

        values.put(Task.VMTask.COLUMN_NAME_TITLE2, name);
        values.put(Task.VMTask.COLUMN_NAME_TITLE3, startDate);
        values.put(Task.VMTask.COLUMN_NAME_TITLE4, dueDate);
        values.put(Task.VMTask.COLUMN_NAME_TITLE5, difficulty);
        values.put(Task.VMTask.COLUMN_NAME_TITLE6, priority);
        values.put(Task.VMTask.COLUMN_NAME_TITLE7, estimatedHours);
        values.put(Task.VMTask.COLUMN_NAME_TITLE8, completed);


        // insert row
        long id = db.insert(Task.VMTask.TABLE_NAME, null, values);
        user.updateEvents(context);
        user.updateEvents(context);

//        insertReport(i)

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Task getTask(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Task.VMTask.TABLE_NAME,
                new String[]{Task.VMTask._ID, Task.VMTask.COLUMN_NAME_TITLE2, Task.VMTask.COLUMN_NAME_TITLE3,
                        Task.VMTask.COLUMN_NAME_TITLE4, Task.VMTask.COLUMN_NAME_TITLE5, Task.VMTask.COLUMN_NAME_TITLE6,
                        Task.VMTask.COLUMN_NAME_TITLE7, Task.VMTask.COLUMN_NAME_TITLE8},
                Task.VMTask._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare task object
        Task task = new Task(
                cursor.getString(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE2)),
                cursor.getString(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE3)),
                cursor.getString(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE4)),
                cursor.getInt(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE5)),
                cursor.getInt(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE6)),
                cursor.getDouble(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE7)),
                cursor.getInt(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE8)));

        // close the db connection
        cursor.close();

        return task;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Task.VMTask.TABLE_NAME + " ORDER BY " +
                Task.VMTask.COLUMN_NAME_TITLE4 + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(Task.VMTask._ID)));
                task.setName(cursor.getString(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE2)));
                task.setStartDate(cursor.getString(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE3)));
                task.setDueDate(cursor.getString(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE4)));
                task.setDifficulty(cursor.getInt(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE5)));
                task.setPriority(cursor.getInt(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE6)));
                task.setEstimatedHours(cursor.getDouble(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE7)));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex(Task.VMTask.COLUMN_NAME_TITLE8)));

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return tasks;
    }

    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + Task.VMTask.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Task.VMTask.COLUMN_NAME_TITLE2, task.getName());
        values.put(Task.VMTask.COLUMN_NAME_TITLE3, task.getStartDate());
        values.put(Task.VMTask.COLUMN_NAME_TITLE4, task.getDueDate());
        values.put(Task.VMTask.COLUMN_NAME_TITLE5, task.getDifficulty());
        values.put(Task.VMTask.COLUMN_NAME_TITLE6, task.getPriority());
        values.put(Task.VMTask.COLUMN_NAME_TITLE7, task.getEstimatedHours());
        values.put(Task.VMTask.COLUMN_NAME_TITLE8, task.getCompleted());

        // updating row
        return db.update(Task.VMTask.TABLE_NAME, values, Task.VMTask._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Task.VMTask.TABLE_NAME, Task.VMTask._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }

    /* EVENT Table Methods*/
    public long insertEvent(String name, String startDate, String endDate, String location) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically - no need to add them

        values.put(Event.VMEvent.COLUMN_NAME_TITLE2, name);
        values.put(Event.VMEvent.COLUMN_NAME_TITLE3, startDate);
        values.put(Event.VMEvent.COLUMN_NAME_TITLE4, endDate);
        values.put(Event.VMEvent.COLUMN_NAME_TITLE5, location);


        // insert row
        long id = db.insert(Event.VMEvent.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Event getEvent(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Event.VMEvent.TABLE_NAME,
                new String[]{Event.VMEvent._ID, Event.VMEvent.COLUMN_NAME_TITLE2, Event.VMEvent.COLUMN_NAME_TITLE3,
                        Event.VMEvent.COLUMN_NAME_TITLE4, Event.VMEvent.COLUMN_NAME_TITLE5},
                Event.VMEvent._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare task object
        Event event = new Event(
                cursor.getString(cursor.getColumnIndex(Event.VMEvent.COLUMN_NAME_TITLE2)),
                cursor.getString(cursor.getColumnIndex(Event.VMEvent.COLUMN_NAME_TITLE3)),
                cursor.getString(cursor.getColumnIndex(Event.VMEvent.COLUMN_NAME_TITLE4)),
                cursor.getString(cursor.getColumnIndex(Event.VMEvent.COLUMN_NAME_TITLE5)));

        // close the db connection
        cursor.close();

        return event;
    }

    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Event.VMEvent.COLUMN_NAME_TITLE2, event.getName());
        values.put(Event.VMEvent.COLUMN_NAME_TITLE3, event.getStartDate());
        values.put(Event.VMEvent.COLUMN_NAME_TITLE4, event.getEndDate());
        values.put(Event.VMEvent.COLUMN_NAME_TITLE5, event.getLocation());


        // updating row
        return db.update(Event.VMEvent.TABLE_NAME, values, Event.VMEvent._ID + " = ?",
                new String[]{String.valueOf(event.getId())});
    }

    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Event.VMEvent.TABLE_NAME, Event.VMEvent._ID + " = ?",
                new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public void deleteSpecificEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Event.VMEvent.TABLE_NAME, Event.VMEvent._ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Event.VMEvent.TABLE_NAME + " ORDER BY " +
                Event.VMEvent.COLUMN_NAME_TITLE4 + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndex(Event.VMEvent._ID)));
                event.setName(cursor.getString(cursor.getColumnIndex(Event.VMEvent.COLUMN_NAME_TITLE2)));
                event.setStartDate(cursor.getString(cursor.getColumnIndex(Event.VMEvent.COLUMN_NAME_TITLE3)));
                event.setEndDate(cursor.getString(cursor.getColumnIndex(Event.VMEvent.COLUMN_NAME_TITLE4)));
                event.setLocation(cursor.getString(cursor.getColumnIndex(Event.VMEvent.COLUMN_NAME_TITLE5)));

                events.add(event);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return events;
    }


    /* REPORT Table Methods*/
    public long insertReport(double hoursSpent, float estimatedHours) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // the id of a report should be the same as the id of the task it corresponds to ******

        values.put(Report.VMReport.COLUMN_NAME_TITLE2, hoursSpent);
        values.put(Report.VMReport.COLUMN_NAME_TITLE3, estimatedHours);

        //insert row
        long id = db.insert(Report.VMReport.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public Report getReport(long id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Report.VMReport.TABLE_NAME,
                new String[]{Report.VMReport._ID, Report.VMReport.COLUMN_NAME_TITLE2, Report.VMReport.COLUMN_NAME_TITLE3},
                Report.VMReport._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare report object
        Report report = new Report(
                cursor.getInt(cursor.getColumnIndex(Report.VMReport._ID)),
                cursor.getDouble(cursor.getColumnIndex(Report.VMReport.COLUMN_NAME_TITLE2)),
                cursor.getFloat(cursor.getColumnIndex(Report.VMReport.COLUMN_NAME_TITLE3)));

        // close the db connection
        cursor.close();

        return report;
    }

    public int updateReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Report.VMReport.COLUMN_NAME_TITLE2, report.getHoursSpent());
        values.put(Report.VMReport.COLUMN_NAME_TITLE3, report.getEstimatedHours());


        // updating row
        return db.update(Report.VMReport.TABLE_NAME, values, Report.VMReport._ID + " = ?",
                new String[]{String.valueOf(report.getId())});

    }

    public void deleteReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Report.VMReport.TABLE_NAME, Report.VMReport._ID + " = ?",
                new String[]{String.valueOf(report.getId())});
        db.close();

    }

    /* PROGRESS Table Methods */
    public long insertProgress(float progress, double hoursSpent){
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // the id of the progress should be the same as the id of the task it corresponds to ******

        values.put(Progress.VMProgress.COLUMN_NAME_TITLE2, progress);
        values.put(Progress.VMProgress.COLUMN_NAME_TITLE3, hoursSpent);

        //insert row
        long id = db.insert(Progress.VMProgress.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public Progress getProgress(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Progress.VMProgress.TABLE_NAME,
                new String[]{Progress.VMProgress._ID, Progress.VMProgress.COLUMN_NAME_TITLE2, Progress.VMProgress.COLUMN_NAME_TITLE3},
                Progress.VMProgress._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare report object
        Progress progress = new Progress(
                cursor.getFloat(cursor.getColumnIndex(Progress.VMProgress.COLUMN_NAME_TITLE2)),
                cursor.getDouble(cursor.getColumnIndex(Progress.VMProgress.COLUMN_NAME_TITLE3)));

        // close the db connection
        cursor.close();

        return progress;
    }

    public int updateProgress(Progress progress) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Progress.VMProgress.COLUMN_NAME_TITLE2, progress.getProgress());
        values.put(Progress.VMProgress.COLUMN_NAME_TITLE3, progress.getHoursSpent());


        // updating row
        return db.update(Progress.VMProgress.TABLE_NAME, values, Progress.VMProgress._ID + " = ?",
                new String[]{String.valueOf(progress.getId())});

    }

    public void deleteProgress(Progress progress) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Progress.VMProgress.TABLE_NAME, Progress.VMProgress._ID + " = ?",
                new String[]{String.valueOf(progress.getId())});
        db.close();
    }

    /*COMPLETION Table Methods*/
    public long insertCompletion(float hoursForCompletion){
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // the id of the progress should be the same as the id of the task it corresponds to ******

        values.put(Completion.VMCompletion.COLUMN_NAME_TITLE2, hoursForCompletion);

        //insert row
        long id = db.insert(Completion.VMCompletion.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public Completion getCompletion(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Completion.VMCompletion.TABLE_NAME,
                new String[]{Completion.VMCompletion._ID, Completion.VMCompletion.COLUMN_NAME_TITLE2},
                Completion.VMCompletion._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare report object
        Completion completion = new Completion(
                cursor.getFloat(cursor.getColumnIndex(Completion.VMCompletion.COLUMN_NAME_TITLE2)));

        // close the db connection
        cursor.close();

        return completion;

    }

    public int updateCompletion(Completion completion){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Completion.VMCompletion.COLUMN_NAME_TITLE2, completion.getHoursForCompletion());

        // updating row
        return db.update(Completion.VMCompletion.TABLE_NAME, values, Completion.VMCompletion._ID + " = ?",
                new String[]{String.valueOf(completion.getId())});

    }

    public void deleteCompletion(Completion completion) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Completion.VMCompletion.TABLE_NAME, Completion.VMCompletion._ID + " = ?",
                new String[]{String.valueOf(completion.getId())});
        db.close();
    }

    /* USER PREFERENCE Table Methods*/
    public long insertUserPref(String workPref, String noDayPref){
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // the id of the progress should be the same as the id of the task it corresponds to ******

        values.put(UserPref.VMUserPref.COLUMN_NAME_TITLE2, workPref);
        values.put(UserPref.VMUserPref.COLUMN_NAME_TITLE3, noDayPref);

        //insert row
        long id = db.insert(UserPref.VMUserPref.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public UserPref getUserPref(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(UserPref.VMUserPref.TABLE_NAME,
                new String[]{UserPref.VMUserPref._ID, UserPref.VMUserPref.COLUMN_NAME_TITLE2, UserPref.VMUserPref.COLUMN_NAME_TITLE3},
                UserPref.VMUserPref._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare report object
        UserPref userPref = new UserPref(
                cursor.getString(cursor.getColumnIndex(UserPref.VMUserPref.COLUMN_NAME_TITLE2)),
                cursor.getString(cursor.getColumnIndex(UserPref.VMUserPref.COLUMN_NAME_TITLE3)));

                // close the db connection
        cursor.close();

        return userPref;

    }

    public int updateUserPref(UserPref userPref){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserPref.VMUserPref.COLUMN_NAME_TITLE2, userPref.getWorkPref());
        values.put(UserPref.VMUserPref.COLUMN_NAME_TITLE3, userPref.getNoDayPref());


        // updating row
        return db.update(UserPref.VMUserPref.TABLE_NAME, values, UserPref.VMUserPref._ID + " = ?",
                new String[]{String.valueOf(userPref.getId())});

    }

    public void deleteUserPref(UserPref userPref) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserPref.VMUserPref.TABLE_NAME, UserPref.VMUserPref._ID + " = ?",
                new String[]{String.valueOf(userPref.getId())});
        db.close();
    }

    /*TIMETABLE Table Methods*/
    public long insertTimetable(String date, int taskID, int eventID, float duration, int completed){
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE2, date);
        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE3, taskID);
        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE4, eventID);
        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE5, duration);
        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE6, completed);

        //insert row
        long id = db.insert(Timetable.VMTimetable.TABLE_NAME, null, values);

        db.close();
        //return date ???
        return id;
    }

    public Timetable getTimetable(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Timetable.VMTimetable.TABLE_NAME,
                new String[]{Timetable.VMTimetable._ID, Timetable.VMTimetable.COLUMN_NAME_TITLE2 , Timetable.VMTimetable.COLUMN_NAME_TITLE3,
                        Timetable.VMTimetable.COLUMN_NAME_TITLE4, Timetable.VMTimetable.COLUMN_NAME_TITLE5, Timetable.VMTimetable.COLUMN_NAME_TITLE6},
                Timetable.VMTimetable._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare report object
        Timetable timetable = new Timetable(
                cursor.getString(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE2)),
                cursor.getInt(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE3)),
                cursor.getInt(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE4)),
                cursor.getFloat(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE5)),
                cursor.getInt(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE6)));

        // close the db connection
        cursor.close();

        return timetable;

    }

    public int updateTimetable(Timetable timetable){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE2, timetable.getDate());
        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE3, timetable.getTaskID());
        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE4, timetable.getEventID());
        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE5, timetable.getDuration());
        values.put(Timetable.VMTimetable.COLUMN_NAME_TITLE6, timetable.getCompleted());

        // updating row
        return db.update(Timetable.VMTimetable.TABLE_NAME, values, Timetable.VMTimetable._ID + " = ?",
                new String[]{String.valueOf(timetable.getId())});

    }

    public List<Timetable> getFullTimetable(){
        List<Timetable> timetable = new ArrayList<>();

        Date today = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmm");
        String strDate = dateFormat.format(today);

        // Need to query it do that it is from this date.

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Timetable.VMTimetable.TABLE_NAME + " ORDER BY " +
                Timetable.VMTimetable.COLUMN_NAME_TITLE2 + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Timetable timetableElement = new Timetable();
                timetableElement.setId(cursor.getInt(cursor.getColumnIndex(Timetable.VMTimetable._ID)));
                timetableElement.setDate(cursor.getString(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE2)));
                timetableElement.setTaskID(cursor.getInt(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE3)));
                timetableElement.setEventID(cursor.getInt(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE4)));
                timetableElement.setDuration(cursor.getLong(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE5)));
                timetableElement.setCompleted(cursor.getInt(cursor.getColumnIndex(Timetable.VMTimetable.COLUMN_NAME_TITLE6)));


                timetable.add(timetableElement);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return timetable;

    }

    public void deleteTimetable(Timetable timetable) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Timetable.VMTimetable.TABLE_NAME, Timetable.VMTimetable._ID + " = ?",
                new String[]{String.valueOf(timetable.getId())});
        db.close();
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
