package com.example.WebSiteGuardian.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 15.02.13
 * Time: 12:31
 */
public class DBAdapter {
    public static final String KEY_ID = "_id";
    public static final String KEY_SERVER_ADDRESS = "server_address";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CHECKED_TIME = "checked_time";

    private static final String DATABASE_NAME = "WebSiteGuardian4";
    private static final String DATABASE_TABLE = "SERVER_STATUS";
    private static final int DATABASE_VERSION = 1;
    private static final String SELECT_QUERY = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + KEY_CHECKED_TIME + " DESC LIMIT 25 ;";

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private static volatile DBAdapter dbAdapter;
    private boolean isOpen;

    public static DBAdapter getInstance(Context context) {
        if (dbAdapter == null) {
            synchronized (DBAdapter.class) {
                if (dbAdapter == null)
                    dbAdapter = new DBAdapter(context);
            }
        }
        return dbAdapter;
    }

    private DBAdapter(Context context) {
        this.context = context;
    }

    public DBAdapter open() throws SQLException {
        if (dbHelper == null || database == null) {
            dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
            database = dbHelper.getWritableDatabase();
        }
        isOpen = true;
        return this;
    }

    public void close() {
        dbHelper.close();
        isOpen = false;
    }

    /**
     * создать новый элемент.
     * если создан успешно - возвращается номер строки id  * иначе -1
     */
    public long insert(String serverAddress, Integer status, Integer timeInMilliseconds) {
        ContentValues initialValues = createContentValues(serverAddress, status, timeInMilliseconds);
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public Cursor list() {
        Cursor cursor = database.rawQuery(SELECT_QUERY, null);
        return cursor;
    }

    public boolean delete(long id) {
        return database.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    public boolean isOpen() {
        return isOpen;
    }

    private ContentValues createContentValues(String serverAddress, Integer status, Integer timeInMilliseconds) {
        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_ADDRESS, serverAddress);
        values.put(KEY_STATUS, status);
        values.put(KEY_CHECKED_TIME, timeInMilliseconds);
        return values;
    }
}
