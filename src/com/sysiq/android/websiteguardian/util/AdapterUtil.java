package com.sysiq.android.websiteguardian.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.WebSiteGuardian.R;
import com.sysiq.android.websiteguardian.service.StatusCheckerService;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 17.02.13
 * Time: 22:49
 */
public class AdapterUtil {
    private static final String TAG = "AdapterUtil";

    public static SimpleCursorAdapter createListAdapter(Cursor cursor, Context context) {
        String[] from = new String[]{DBGuardianConstants.KEY_SERVER_ADDRESS, DBGuardianConstants.KEY_STATUS, DBGuardianConstants.KEY_CHECKED_TIME};
        int[] to = new int[]{R.id.text_server, R.id.imageView, R.id.text_time};

        // создааем адаптер и настраиваем список
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.item, cursor, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(R.id.text_time);
                try {
                    String time = textView.getText().toString();
                    Timestamp date = new Timestamp(Long.parseLong(time) * 1000);
                    textView.setText(date.toLocaleString());
                } catch (Exception e) {
                    Log.e(TAG, "Troubles with date translating.");
                }

                ImageView img = (ImageView) view.findViewById(R.id.imageView);
                Cursor cursorLocal = (Cursor) getItem(position);
                String codeValue = cursorLocal.getString(cursorLocal.getColumnIndex(DBGuardianConstants.KEY_STATUS));
                Integer code = Integer.parseInt(codeValue);

                if (code == StatusCheckerService.SUCCESS_CODE) {
                    img.setImageResource(R.drawable.green);
                } else {
                    img.setImageResource(R.drawable.red);
                }
                return view;
            }
        };
        return adapter;
    }
}
