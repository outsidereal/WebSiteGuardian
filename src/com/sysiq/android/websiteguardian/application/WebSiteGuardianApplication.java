package com.sysiq.android.websiteguardian.application;

import com.sysiq.android.websiteguardian.db.DBAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: ZIM
 * Date: 19.02.13
 * Time: 22:33
 */
public class WebSiteGuardianApplication extends android.app.Application {
    private DBAdapter dbAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        dbAdapter = DBAdapter.getInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public DBAdapter getDbAdapter() {
        return dbAdapter;
    }
}
