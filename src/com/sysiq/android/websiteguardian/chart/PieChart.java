package com.sysiq.android.websiteguardian.chart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.WebSiteGuardian.R;
import com.sysiq.android.websiteguardian.activity.MainActivity;
import com.sysiq.android.websiteguardian.application.WebSiteGuardianApplication;
import com.sysiq.android.websiteguardian.db.DBAdapter;
import com.sysiq.android.websiteguardian.util.DBGuardianConstants;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 19.02.13
 * Time: 14:36
 */
public class PieChart extends Activity {
    private final static String urlGoogleChart = "http://chart.apis.google.com/chart";
    private final static String TAG = "PieChart";
    private final static int CHART_IMAGE_WIDTH = 700;
    private final static int CHART_IMAGE_HEIGHT = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_chart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_history: {
                Intent startMainActivityIntent = new Intent(this, MainActivity.class);
                startActivity(startMainActivityIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        sendRequestForPie();
    }

    private void sendRequestForPie() {
        DBAdapter adapter = ((WebSiteGuardianApplication) getApplication()).getDbAdapter();
        Integer successCount = adapter.list(DBGuardianConstants.SELECT_UNLIMITED_SUCCESS_RESULT).getCount();
        Integer failedCount = adapter.list(DBGuardianConstants.SELECT_UNLIMITED_FAILED_RESULT).getCount();

        ImageView image = (ImageView) findViewById(R.id.image_pie);
        String requestString = buildRequestString(CHART_IMAGE_WIDTH, CHART_IMAGE_HEIGHT, successCount, failedCount);

        Bitmap bitmap = loadChart(requestString);
        if (bitmap == null) {
            Toast.makeText(PieChart.this, "Problem in loading 3D Pie Chart", Toast.LENGTH_LONG).show();
        } else {
            image.setImageBitmap(bitmap);
        }
    }

    private String buildRequestString(int width, int height, int successCount, int failedCount) {
        StringBuilder request = new StringBuilder(urlGoogleChart);
        request.append("?cht=p3&chs=").append(width).append("x").append(height)
                .append("&chl=Success|Fail&chd=t:").append(successCount).append(",").append(failedCount)
                .append("&chco=00FF11|FF1100");
        return request.toString();
    }

    private Bitmap loadChart(String urlRqs) {
        Bitmap bm = null;
        InputStream inputStream;
        try {
            inputStream = openHttpConnection(urlRqs);
            if (inputStream == null)
                throw new IOException();

            bm = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Problem with load chart.", e);
        }
        return bm;
    }

    private InputStream openHttpConnection(String strURL) throws IOException {
        InputStream is = null;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            Log.e(TAG, "HTTP client failed.", e);
            return is;
        }
        if (networkInfo == null || !networkInfo.isConnected()) {
            return is;
        }

        URL url = new URL(strURL);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        try {
            is = new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception ex) {
            Log.e(TAG, "Http Connection Failed..", ex);
        }
        return is;
    }
}
