package com.sysiq.android.websiteguardian.util;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.sysiq.android.websiteguardian.R;
import com.sysiq.android.websiteguardian.db.domain.ServerStatusTable;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 17.02.13
 * Time: 22:49
 */
public class AdapterUtil {
    private static final String TAG = "AdapterUtil";

    public static SimpleCursorAdapter createListAdapter(Context context) {
        String[] from = new String[]{ServerStatusTable.COLUMN_SERVER_ADDRESS, ServerStatusTable.COLUMN_STATUS, ServerStatusTable.COLUMN_CHECKED_TIME};
        int[] to = new int[]{R.id.text_server, R.id.imageView, R.id.text_time};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.item, null, from, to, 0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                String columnName = cursor.getColumnName(columnIndex);
                if (columnName.equals(ServerStatusTable.COLUMN_CHECKED_TIME)) {
                    TextView textView = (TextView) view.findViewById(R.id.text_time);
                    textView.setText(DateUtils.getRelativeTimeSpanString(cursor.getLong(columnIndex) * 1000, System.currentTimeMillis(), 1000));
                    return true;
                }
                return false;
            }
        });
        return adapter;
    }
}