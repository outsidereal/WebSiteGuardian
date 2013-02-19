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
 * Time: 22:41
 */
public class SuccessResultActivity extends Activity {
    private static final int REFRESH_TIME = 5000;
    private volatile boolean needRefresh;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_tab);
        initDataList();
        RefreshTimerTask task = new RefreshTimerTask();
        timer = new Timer();
        timer.schedule(task, REFRESH_TIME, REFRESH_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefresh)
            initDataList();
    }

    private void initDataList() {
        DBAdapter dbAdapter = ((WebSiteGuardianApplication)getApplication()).getDbAdapter();
        Cursor cursor = dbAdapter.list(DBGuardianConstants.SELECT_SUCCESS_RESULT, 20);
        startManagingCursor(cursor);
        SimpleCursorAdapter adapter = AdapterUtil.createListAdapter(cursor, getApplicationContext());
        ListView list = (ListView) findViewById(R.id.log_list_success);
        list.setAdapter(adapter);
    }

    private class RefreshTimerTask extends TimerTask {
        public void run() {
            needRefresh = true;
        }
    }
}
