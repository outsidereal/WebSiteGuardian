package com.sysiq.android.websiteguardian.util;

import com.sysiq.android.websiteguardian.R;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 18.02.13
 * Time: 10:50
 */
public class DBGuardianConstants {
    public static final String DATABASE_TABLE = "SERVER_STATUS";
    public static final String KEY_ID = "_id";
    public static final String KEY_SERVER_ADDRESS = "server_address";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CHECKED_TIME = "checked_time";
    public static final int SUCCESS_STATUS = R.drawable.green;
    public static final String SELECT_ALL_RESULT
            = "SELECT * FROM " + DATABASE_TABLE +
            " ORDER BY " + KEY_CHECKED_TIME + " DESC LIMIT ?1 ;";
    public static final String SELECT_SUCCESS_RESULT
            = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_STATUS + " =" + SUCCESS_STATUS +
            " ORDER BY " + KEY_CHECKED_TIME + " DESC LIMIT ?1 ;";
    public static final String SELECT_FAILED_RESULT
            = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_STATUS + " !=" + SUCCESS_STATUS +
            " ORDER BY " + KEY_CHECKED_TIME + " DESC LIMIT ?1 ;";
    public static final String SELECT_UNLIMITED_FAILED_RESULT
            = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_STATUS + " !=" + SUCCESS_STATUS + ";";
    public static final String SELECT_UNLIMITED_SUCCESS_RESULT
            = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_STATUS + " =" + SUCCESS_STATUS + ";";
}
