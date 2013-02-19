package com.example.WebSiteGuardian;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.WebSiteGuardian.db.DBAdapter;
import com.example.WebSiteGuardian.util.DBGuardianConstants;

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
                //Show the site chooser activity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
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
        DBAdapter adapter = DBAdapter.getInstance(this);
        Integer successCount = adapter.list(DBGuardianConstants.SELECT_UNLIMITED_SUCCESS_RESULT).getCount();
        Integer failedCount = adapter.list(DBGuardianConstants.SELECT_UNLIMITED_FAILED_RESULT).getCount();

        ImageView image = (ImageView) findViewById(R.id.image_pie);
        Integer height = 400;//image.getHeight();
        Integer width = 700;//image.getWidth();
        String requestString = buildRequestString(width, height, successCount, failedCount);

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
            inputStream = OpenHttpConnection(urlRqs);
            bm = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Problem with load chart.", e);
        }
        return bm;
    }

    private InputStream OpenHttpConnection(String strURL) throws IOException {
        InputStream is = null;
        URL url = new URL(strURL);
        URLConnection urlConnection = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            Log.e(TAG, "Http Connection Failed..", ex);
        }
        return is;
    }
}
