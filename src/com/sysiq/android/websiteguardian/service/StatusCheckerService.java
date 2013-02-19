package com.sysiq.android.websiteguardian.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import com.example.WebSiteGuardian.R;
import com.sysiq.android.websiteguardian.db.DBAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 14.02.13
 * Time: 16:36
 */
public class StatusCheckerService extends Service {
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = -12;
    public static final int NETWORK_FAIL = -1024;
    private static final String TAG = "StatusCheckerService";
    private static final String DEFAULT_URL = "http://www.playground.ru";
    private static final String DEFAULT_INTERVAL = "5000";

    private SharedPreferences preferences;
    private Timer timer;
    private MyTimerTask timerTask;
    private DBAdapter dbAdapter;

    @Override
    public void onCreate() {
        timer = new Timer();
        timerTask = new MyTimerTask();
        Context context = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        dbAdapter = DBAdapter.getInstance(context);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        String sInterval = preferences.getString(getString(R.string.pref_interval_key), DEFAULT_INTERVAL);
        Long interval = Long.parseLong(sInterval);
        timer.schedule(timerTask, interval, interval);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int isOnline(String url) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            Log.e(TAG, "HTTP client failed.", e);
            return NETWORK_FAIL;
        }
        if (networkInfo == null || !networkInfo.isConnected()) {
            return NETWORK_FAIL;
        }

        //check if website is online
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (Exception e) {
            Log.e(TAG, "HTTP client failed.", e);
            return FAIL_CODE;
        }
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode;
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            String url = preferences.getString(getString(R.string.pref_url_key), DEFAULT_URL);
            //get server status
            int status = isOnline(url);
            if (status != NETWORK_FAIL) {
                //get checking time in seconds
                int currentTime = (int) (System.currentTimeMillis() / 1000);
                dbAdapter.insert(url, status, currentTime);
            }
        }
    }
}
