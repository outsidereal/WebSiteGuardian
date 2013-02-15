package com.example.WebSiteGuardian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.WebSiteGuardian.db.DBAdapter;

import java.sql.Timestamp;

public class MainActivity extends Activity {
    private static Cursor cursor;
    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        context = getApplicationContext();
        initDataList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_preferences:
                //Show the site chooser activity
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doStartChecking(final View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent service = new Intent(context, StatusCheckerService.class);
                context.startService(service);
            }
        });
    }

    public void doStopChecking(final View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent service = new Intent(context, StatusCheckerService.class);
                context.stopService(service);
            }
        });
    }

    public void doRefresh(final View view) {
        if (DBAdapter.getInstance(context).isOpen())
            cursor.requery();
    }

    private void initDataList() {
        DBAdapter dbAdapter = DBAdapter.getInstance(context);
        dbAdapter.open();
        cursor = dbAdapter.list();
        startManagingCursor(cursor);

        String[] from = new String[]{DBAdapter.KEY_SERVER_ADDRESS, DBAdapter.KEY_STATUS, DBAdapter.KEY_CHECKED_TIME};
        int[] to = new int[]{R.id.text_server, R.id.text_status, R.id.text_time};

        // создааем адаптер и настраиваем список
        SimpleCursorAdapter notes = new SimpleCursorAdapter(context, R.layout.item, cursor, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(R.id.text_time);
                try {
                    String time = textView.getText().toString();
                    Timestamp date = new Timestamp(Long.parseLong(time) * 1000);
                    textView.setText(date.toString());
                } catch (Exception e) {

                }
                return view;
            }
        };
        ListView list = (ListView) findViewById(R.id.log_list);
        list.setAdapter(notes);
    }
}
