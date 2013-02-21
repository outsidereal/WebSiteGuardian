package com.sysiq.android.websiteguardian.db.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.sysiq.android.websiteguardian.db.DatabaseHelper;
import com.sysiq.android.websiteguardian.db.domain.ServerStatusTable;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 20.02.13
 * Time: 12:51
 */
public class GuardianContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.sysiq.android.websiteguardian.db.contentprovider";
    private static final String BASE_PATH = "statuses";
    private static final int URI_STATUSES = 1;
    private static final int URI_STATUSES_ID = 2;
    private DatabaseHelper database;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/statuses";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/status";

    private static final UriMatcher sURIMatcher;

    static {
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, URI_STATUSES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", URI_STATUSES_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(ServerStatusTable.DATABASE_TABLE);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case URI_STATUSES:
                break;
            case URI_STATUSES_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere(ServerStatusTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case URI_STATUSES:
                id = sqlDB.insert(ServerStatusTable.DATABASE_TABLE, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case URI_STATUSES:
                rowsDeleted = sqlDB.delete(ServerStatusTable.DATABASE_TABLE, selection, selectionArgs);
                break;
            case URI_STATUSES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ServerStatusTable.DATABASE_TABLE, ServerStatusTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(ServerStatusTable.DATABASE_TABLE, ServerStatusTable.COLUMN_ID + "=" + id
                            + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case URI_STATUSES:
                rowsUpdated = sqlDB.update(ServerStatusTable.DATABASE_TABLE, values, selection, selectionArgs);
                break;
            case URI_STATUSES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ServerStatusTable.DATABASE_TABLE, values,
                            ServerStatusTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(ServerStatusTable.DATABASE_TABLE, values,
                            ServerStatusTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    public static ContentValues createContentValues(String serverAddress, Integer status, Integer timeInMilliseconds) {
        ContentValues values = new ContentValues();
        values.put(ServerStatusTable.COLUMN_SERVER_ADDRESS, serverAddress);
        values.put(ServerStatusTable.COLUMN_STATUS, status);
        values.put(ServerStatusTable.COLUMN_CHECKED_TIME, timeInMilliseconds);
        return values;
    }

    private void checkColumns(String[] projection) {
        String[] available = {ServerStatusTable.COLUMN_SERVER_ADDRESS, ServerStatusTable.COLUMN_STATUS,
                ServerStatusTable.COLUMN_CHECKED_TIME, ServerStatusTable.COLUMN_ID};

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
