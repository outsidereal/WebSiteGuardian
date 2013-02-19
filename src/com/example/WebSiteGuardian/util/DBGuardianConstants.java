package com.example.WebSiteGuardian.util;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 18.02.13
 * Time: 10:50
 */
public class DBGuardianConstants {
    public static final String DATABASE_NAME = "WebSiteGuardian_v2";
    public static final String DATABASE_TABLE = "SERVER_STATUS";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_ID = "_id";
    public static final String KEY_SERVER_ADDRESS = "server_address";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CHECKED_TIME = "checked_time";
    public static final String DATABASE_CREATE =
            "CREATE TABLE " + DBGuardianConstants.DATABASE_TABLE +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBGuardianConstants.KEY_SERVER_ADDRESS + " TEXT NOT NULL, " +
                    DBGuardianConstants.KEY_STATUS + " INTEGER NOT NULL, " +
                    DBGuardianConstants.KEY_CHECKED_TIME + " INTEGER NOT NULL);";
    public static final String SELECT_ALL_RESULT
            = "SELECT * FROM " + DATABASE_TABLE +
            " ORDER BY " + KEY_CHECKED_TIME + " DESC LIMIT ?1 ;";
    public static final String SELECT_SUCCESS_RESULT
            = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_STATUS + " =200 " +
            " ORDER BY " + KEY_CHECKED_TIME + " DESC LIMIT ?1 ;";
    public static final String SELECT_FAILED_RESULT
            = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_STATUS + " !=200 " +
            " ORDER BY " + KEY_CHECKED_TIME + " DESC LIMIT ?1 ;";
    public static final String SELECT_UNLIMITED_FAILED_RESULT
            = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_STATUS + " !=200 ;";
    public static final String SELECT_UNLIMITED_SUCCESS_RESULT
            = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_STATUS + " =200 ;";
}
