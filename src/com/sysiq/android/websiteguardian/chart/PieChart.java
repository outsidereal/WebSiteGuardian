package com.sysiq.android.websiteguardian.chart;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import com.sysiq.android.websiteguardian.R;
import com.sysiq.android.websiteguardian.activity.MainActivity;
import com.sysiq.android.websiteguardian.db.contentprovider.GuardianContentProvider;
import com.sysiq.android.websiteguardian.db.domain.ServerStatusTable;
import org.achartengine.GraphicalView;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 19.02.13
 * Time: 14:36
 */
public class PieChart extends Activity {
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
        drawPie();
    }

    private void drawPie() {
        ContentResolver contentResolver = getContentResolver();
        Integer successCount = contentResolver.query(GuardianContentProvider.CONTENT_URI, null,
                ServerStatusTable.COLUMN_STATUS + "=" + ServerStatusTable.SUCCESS_STATUS, null, null).getCount();
        Integer failedCount = contentResolver.query(GuardianContentProvider.CONTENT_URI, null,
                ServerStatusTable.COLUMN_STATUS + "!=" + ServerStatusTable.SUCCESS_STATUS, null, null).getCount();
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_layout);
        GraphicalView chartView = PieChartView.getNewInstance(this, successCount, failedCount);
        chartContainer.addView(chartView);
    }
}
