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
            "CREATE TABLE SERVER_STATUS " +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "server_address TEXT NOT NULL, " +
                    "status INTEGER NOT NULL, " +
                    "checked_time INTEGER NOT NULL);";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,  int version) {
        super(context, name, factory, version);
    }

    // метод вызывается при создании базы данных
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // метод вызывается при обновлении базы данных, например, когда вы увеличиваете номер версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS SERVER_STATUS");
        onCreate(database);
    }
}
