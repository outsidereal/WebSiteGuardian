package com.sysiq.android.websiteguardian.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.WebSiteGuardian.R;

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
        String[] from = new String[]{DBGuardianConstants.KEY_SERVER_ADDRESS, DBGuardianConstants.KEY_STATUS, DBGuardianConstants.KEY_CHECKED_TIME};
        int[] to = new int[]{R.id.text_server, R.id.imageView, R.id.text_time};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.item, null, from, to, 0) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView timeView = (TextView) view.findViewById(R.id.text_time);
                try {
                    String time = timeView.getText().toString();
                    Timestamp date = new Timestamp(Long.parseLong(time) * 1000);
                    timeView.setText(date.toLocaleString());
                } catch (Exception e) {
                    Log.e(TAG, "Troubles with date translating.", e);
                }
                return view;
            }
        };
        return adapter;
    }
}
