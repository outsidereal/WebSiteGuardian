package com.example.WebSiteGuardian.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.WebSiteGuardian.util.DBGuardianConstants;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 15.02.13
 * Time: 12:31
 */
public class DBAdapter {
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private static volatile DBAdapter dbAdapter;

    public static DBAdapter getInstance(Context context) {
        if (dbAdapter == null) {
            synchronized (DBAdapter.class) {
                if (dbAdapter == null)
                    dbAdapter = new DBAdapter(context).open();
            }
        }
        return dbAdapter;
    }

    /**
     * создать новый элемент.
     * если создан успешно - возвращается номер строки id  * иначе -1
     */
    public long insert(String serverAddress, Integer status, Integer timeInMilliseconds) {
        ContentValues initialValues = createContentValues(serverAddress, status, timeInMilliseconds);
        return database.insert(DBGuardianConstants.DATABASE_TABLE, null, initialValues);
    }

    public Cursor list(String query, int maxElements) {
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(maxElements)});
        return cursor;
    }

    public Cursor list(String query) {
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    public boolean delete(long id) {
        return database.delete(DBGuardianConstants.DATABASE_TABLE, DBGuardianConstants.KEY_ID + "=" + id, null) > 0;
    }

    private DBAdapter(Context context) {
        this.context = context;
    }

    private DBAdapter open() throws SQLException {
        if (dbHelper == null || database == null || !database.isOpen()) {
            dbHelper = new DatabaseHelper(context, DBGuardianConstants.DATABASE_NAME, null, DBGuardianConstants.DATABASE_VERSION);
            database = dbHelper.getWritableDatabase();
        }
        return this;
    }

    private ContentValues createContentValues(String serverAddress, Integer status, Integer timeInMilliseconds) {
        ContentValues values = new ContentValues();
        values.put(DBGuardianConstants.KEY_SERVER_ADDRESS, serverAddress);
        values.put(DBGuardianConstants.KEY_STATUS, status);
        values.put(DBGuardianConstants.KEY_CHECKED_TIME, timeInMilliseconds);
        return values;
    }
}
