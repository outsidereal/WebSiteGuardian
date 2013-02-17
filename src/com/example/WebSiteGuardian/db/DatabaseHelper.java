package com.example.WebSiteGuardian.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 15.02.13
 * Time: 11:54
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + DBAdapter.DATABASE_NAME +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBAdapter.KEY_SERVER_ADDRESS + " TEXT NOT NULL, " +
                    DBAdapter.KEY_STATUS + " INTEGER NOT NULL, " +
                    DBAdapter.KEY_CHECKED_TIME + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
        database.execSQL("DROP TABLE IF EXISTS " + DBAdapter.DATABASE_NAME);
        onCreate(database);
    }
}
