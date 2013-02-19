package com.sysiq.android.websiteguardian.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.sysiq.android.websiteguardian.util.DBGuardianConstants;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 15.02.13
 * Time: 11:54
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DBGuardianConstants.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
        database.execSQL("DROP TABLE IF EXISTS " + DBGuardianConstants.DATABASE_NAME);
        onCreate(database);
    }
}
