package com.maximeweekhout.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime Weekhout on 02-10-16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "firstdb.db";
    private static final int DATABASE_VERSION = 7;
    private static final String TABLE_LISTS= "todolists";
    private static final String TABLE_ITEMS = "todoitems";
    private static final String KEY_ID = "todoid";
    private static final String KEY_TITLE = "todotitle";
    private static final String KEY_NAME = "todoitem";
    private static final String KEY_CHECKED = "todochecked";
    private static final String KEY_LISTID = "listid";

    private int listId;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, int id) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.listId = id;
    }

    public DBHelper(Context context, String title) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);


        // Inserting Row
        this.listId = (int) db.insert(TABLE_LISTS, null, values);
        db.close(); // Closing database connection
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_LISTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT"
                + ")";
        db.execSQL(CREATE_LIST_TABLE);

        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_CHECKED + " BOOL,"
                + KEY_LISTID + " INTEGER"
                + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);

        // Create tables again
        onCreate(db);
    }

    // Create
    public void add(String item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item);
        values.put(KEY_CHECKED, false);
        values.put(KEY_LISTID, this.listId);

        // Inserting Row
        db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection
    }

    // Read
    public List<ListItem> read() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE " + KEY_LISTID  + " = " + this.listId, null);

        List<ListItem> list = new ArrayList<ListItem>();

        if (cursor .moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                System.out.println(cursor.getColumnIndex(KEY_ID));
                ListItem item = new ListItem(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_CHECKED))
                );
                list.add(item);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    // Read
    public List<ListItem> getLists() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LISTS, null);

        List<ListItem> list = new ArrayList<ListItem>();

        if (cursor .moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                System.out.println(cursor.getColumnIndex(KEY_ID));
                ListItem item = new ListItem(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                );
                list.add(item);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    // Update
    public void update(int id, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_CHECKED, checked);

        db.update(TABLE_ITEMS, cv, KEY_ID + " = " + id, null);
        db.close();
    }

    public void removeList(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LISTS, KEY_ID + " = '" + id + "'", null);
        db.close();
    }

    // remove
    public void remove(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = '" + id + "'", null);
        db.close();
    }
}
