package com.sysiq.android.websiteguardian.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.WebSiteGuardian.R;
import com.sysiq.android.websiteguardian.application.WebSiteGuardianApplication;
import com.sysiq.android.websiteguardian.db.DBAdapter;
import com.sysiq.android.websiteguardian.db.contentprovider.GuardianContentProvider;
import com.sysiq.android.websiteguardian.db.domain.ServerStatusTable;
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
public class AllResultActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
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

        this.getListView().setDividerHeight(2);
        registerForContextMenu(getListView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefresh)
            initDataList();
    }

    private void initDataList() {
        DBAdapter dbAdapter = ((WebSiteGuardianApplication) getApplication()).getDbAdapter();
        Cursor cursor = dbAdapter.list(DBGuardianConstants.SELECT_ALL_RESULT, 20);
        startManagingCursor(cursor);
        adapter = AdapterUtil.createListAdapter(cursor, getApplicationContext());
        setListAdapter(adapter);
    }

    private SimpleCursorAdapter adapter;


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {ServerStatusTable.COLUMN_ID, ServerStatusTable.COLUMN_STATUS};
        CursorLoader cursorLoader = new CursorLoader(this, GuardianContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    private class RefreshTimerTask extends TimerTask {
        public void run() {
            needRefresh = true;
        }
    }
}
