package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Database.Event.Event;
import Database.Task.Task;

public class VMDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VM.db";

    public VMDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // All the tables must be created here when the application is downloaded and opened
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Task.SQL_CREATE_TASKS);
        db.execSQL(Event.SQL_CREATE_EVENTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
       db.execSQL(Task.SQL_DELETE_TASKS);
       db.execSQL(Event.SQL_DELETE_EVENTS);
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
                Task.VMTask._ID + "=?",
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

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
