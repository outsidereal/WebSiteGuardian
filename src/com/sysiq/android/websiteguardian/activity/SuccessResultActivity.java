package com.sysiq.android.websiteguardian.activity;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import com.example.WebSiteGuardian.R;
import com.sysiq.android.websiteguardian.db.contentprovider.GuardianContentProvider;
import com.sysiq.android.websiteguardian.util.AdapterUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 17.02.13
 * Time: 22:41
 */
public class SuccessResultActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REFRESH_TIME = 30000;
    private Timer timer;
    private Loader loader;
    private SimpleCursorAdapter adapter;
    private volatile boolean needRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_tab);
        initDataList();
        this.getListView().setDividerHeight(2);
        registerForContextMenu(getListView());

        RefreshTimerTask refreshTimerTask = new RefreshTimerTask();
        timer = new Timer();
        timer.schedule(refreshTimerTask, REFRESH_TIME, REFRESH_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefresh)
            loader.forceLoad();
    }

    private void initDataList() {
        loader = getLoaderManager().initLoader(0, null, this);
        adapter = AdapterUtil.createListAdapter(getApplicationContext());
        setListAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //TODO: conditions
        CursorLoader cursorLoader = new CursorLoader(this, GuardianContentProvider.CONTENT_URI, null, null, null, null);
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
