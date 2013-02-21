package com.sysiq.android.websiteguardian.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import com.sysiq.android.websiteguardian.R;
import com.sysiq.android.websiteguardian.service.StatusCheckerService;

public class MainActivity extends TabActivity {
    private Context context;
    private boolean serviceRunning;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = getApplicationContext();
        initResultTabs();
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
                Intent preferenceActivityIntent = new Intent(this, PreferencesActivity.class);
                startActivity(preferenceActivityIntent);
                return true;
            case R.id.menu_availability: 
                Intent pieChartActivityIntent = new Intent(this, PieChartActivity.class);
                startActivity(pieChartActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doStartChecking(final View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!serviceRunning) {
                    Intent startStatusCheckerServiceIntent = new Intent(context, StatusCheckerService.class);
                    context.startService(startStatusCheckerServiceIntent);
                    serviceRunning = true;
                }
            }
        });
    }

    public void doStopChecking(final View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serviceRunning) {
                    Intent stopStatusCheckerServiceIntent = new Intent(context, StatusCheckerService.class);
                    context.stopService(stopStatusCheckerServiceIntent);
                }
            }
        });
    }

    private void initResultTabs() {
        TabHost tabHost = getTabHost();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        View allTabHeader = getLayoutInflater().inflate(R.layout.all_tab_header, null);
        tabSpec.setIndicator(allTabHeader);
        tabSpec.setContent(new Intent(this, AllResultActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        View successTabHeader = getLayoutInflater().inflate(R.layout.success_tab_header, null);
        tabSpec.setIndicator(successTabHeader);
        tabSpec.setContent(new Intent(this, SuccessResultActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        View failedTabHeader = getLayoutInflater().inflate(R.layout.failure_tab_header, null);
        tabSpec.setIndicator(failedTabHeader);
        tabSpec.setContent(new Intent(this, FailedResultActivity.class));
        tabHost.addTab(tabSpec);
    }
}
