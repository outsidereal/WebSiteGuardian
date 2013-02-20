package com.sysiq.android.websiteguardian.db.domain;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.WebSiteGuardian.R;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 20.02.13
 * Time: 12:32
 */
public class ServerStatusTable {
    public static final String DATABASE_TABLE = "SERVER_STATUS";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SERVER_ADDRESS = "server_address";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_CHECKED_TIME = "checked_time";
    public static final int SUCCESS_STATUS = R.drawable.green;
    public static final String DATABASE_CREATE =
            "CREATE TABLE " + DATABASE_TABLE +
                    "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SERVER_ADDRESS + " TEXT NOT NULL, " +
                    COLUMN_STATUS + " INTEGER NOT NULL, " +
                    COLUMN_CHECKED_TIME + " INTEGER NOT NULL" +
                    ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ServerStatusTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(database);
    }
}
