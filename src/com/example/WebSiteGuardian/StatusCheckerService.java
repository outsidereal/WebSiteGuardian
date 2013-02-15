package com.example.WebSiteGuardian;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import com.example.WebSiteGuardian.db.DBAdapter;
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
    private static final int SUCCESS_CODE = 200;
    private static final int FAILURE_CODE = -1;
    private static final int NETWORK_FAIL = -2;
    private static final String DEFAULT_URL = "http://www.playground.ru";
    private static final String DEFAULT_INTERVAL = "5000L";

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
        dbAdapter = DBAdapter.getInstance(context).open();
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
        timer.cancel();
        dbAdapter.close();
    }

    public int isOnline(String url) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return FAILURE_CODE;
        }
        int stat_code = response.getStatusLine().getStatusCode();
        return stat_code;// == SUCCESS_CODE ? SUCCESS_CODE :FAILURE_CODE;
    }


    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            String url = preferences.getString(getString(R.string.pref_url_key), DEFAULT_URL);
            int status = isOnline(url);
            int currentTime = (int) (System.currentTimeMillis() / 1000);
            dbAdapter.insert(url, status, currentTime);
        }
    }
}
