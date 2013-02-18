package com.example.WebSiteGuardian;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    private Context context;
    private boolean serviceRunning;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        context = getApplicationContext();
        initTabs();
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
                if (!serviceRunning) {
                    Intent service = new Intent(context, StatusCheckerService.class);
                    context.startService(service);
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
                    Intent service = new Intent(context, StatusCheckerService.class);
                    context.stopService(service);
                }
            }
        });
    }

    private void initTabs() {
        // получаем TabHost
        TabHost tabHost = getTabHost();
        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Total");
        tabSpec.setContent(new Intent(this, AllResultActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Success");
        tabSpec.setContent(new Intent(this, SuccessResultActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Failed");
        tabSpec.setContent(new Intent(this, FailedResultActivity.class));
        tabHost.addTab(tabSpec);
    }
}
