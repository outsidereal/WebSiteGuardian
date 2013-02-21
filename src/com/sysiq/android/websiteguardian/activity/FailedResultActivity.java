package com.sysiq.android.websiteguardian.activity;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import com.sysiq.android.websiteguardian.R;
import com.sysiq.android.websiteguardian.db.contentprovider.GuardianContentProvider;
import com.sysiq.android.websiteguardian.db.domain.ServerStatusTable;
import com.sysiq.android.websiteguardian.util.AdapterUtil;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 17.02.13
 * Time: 22:16
 */
public class FailedResultActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.failure_tab);
        initDataList();
        this.getListView().setDividerHeight(2);
        registerForContextMenu(getListView());
    }

    private void initDataList() {
        getLoaderManager().initLoader(0, null, this);
        adapter = AdapterUtil.createListAdapter(getApplicationContext());
        setListAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader = new CursorLoader(this, GuardianContentProvider.CONTENT_URI, null,
                ServerStatusTable.COLUMN_STATUS + "!=" + ServerStatusTable.SUCCESS_STATUS, null, null);
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
}
