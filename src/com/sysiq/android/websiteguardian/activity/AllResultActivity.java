package com.sysiq.android.websiteguardian.activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.WebSiteGuardian.R;
import com.sysiq.android.websiteguardian.application.WebSiteGuardianApplication;
import com.sysiq.android.websiteguardian.db.DBAdapter;
import com.sysiq.android.websiteguardian.util.AdapterUtil;
import com.sysiq.android.websiteguardian.util.DBGuardianConstants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 17.02.13
 * Time: 22:15
 */
public class AllResultActivity extends Activity {
    private static final int REFRESH_TIME = 5000;
    private Timer timer;
    private volatile boolean needRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tab);

        initDataList();
        RefreshTimerTask refreshTimerTask = new RefreshTimerTask();
        timer = new Timer();
        timer.schedule(refreshTimerTask, REFRESH_TIME, REFRESH_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefresh)
            initDataList();
    }

    private void initDataList() {
        DBAdapter dbAdapter = ((WebSiteGuardianApplication)getApplication()).getDbAdapter();
        Cursor cursor = dbAdapter.list(DBGuardianConstants.SELECT_ALL_RESULT, 20);
        startManagingCursor(cursor);
        SimpleCursorAdapter adapter = AdapterUtil.createListAdapter(cursor, getApplicationContext());
        ListView list = (ListView) findViewById(R.id.log_list_all);
        list.setAdapter(adapter);
    }

    private class RefreshTimerTask extends TimerTask {
        public void run() {
            needRefresh = true;
        }
    }
}
