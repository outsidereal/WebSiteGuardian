package com.example.WebSiteGuardian;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.WebSiteGuardian.db.DBAdapter;
import com.example.WebSiteGuardian.util.AdapterUtil;
import com.example.WebSiteGuardian.util.DBGuardianConstants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 17.02.13
 * Time: 22:16
 */
public class FailedResultActivity extends Activity {
    private static final int REFRESH_TIME = 5000;
    private Cursor cursor;
    private Context context;
    private volatile boolean needRefresh;
    private Timer timer;
    private RefreshTimerTask task;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.failure_tab);
        context = getApplicationContext();
        initDataList();

        task = new RefreshTimerTask();
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
        DBAdapter dbAdapter = DBAdapter.getInstance(context);
        cursor = dbAdapter.list(DBGuardianConstants.SELECT_FAILED_RESULT, 25);
        startManagingCursor(cursor);

        // создааем адаптер и настраиваем список
        SimpleCursorAdapter adapter = AdapterUtil.createListAdapter(cursor, context);
        ListView list = (ListView) findViewById(R.id.log_list_failed);
        list.setAdapter(adapter);
    }

    private class RefreshTimerTask extends TimerTask {
        public void run() {
            needRefresh = true;
        }
    }
}
