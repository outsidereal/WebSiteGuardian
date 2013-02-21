package com.sysiq.android.websiteguardian.chart;

import android.content.Context;
import android.graphics.Color;
import com.sysiq.android.websiteguardian.R;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * Created with IntelliJ IDEA.
 * User: d.ulanovych
 * Date: 21.02.13
 * Time: 12:56
 */

/**
 * In general this class provided as an example. Comments also are left.
 * Base example see here http://danielkvist.net/code/piechart-with-achartengine-in-android
 */
public class PieChartView extends GraphicalView {
    public static final int COLOR_GREEN = Color.parseColor("#62c51a");
    public static final int COLOR_RED = Color.parseColor("#ff1111");

    /**
     * Constructor that only calls the super method. It is not used to instantiate the object from outside of this
     * class.
     *
     * @param context
     * @param abstractChart
     */
    private PieChartView(Context context, AbstractChart abstractChart) {
        super(context, abstractChart);
    }

    /**
     * This method returns a new graphical view as a pie chart view. It uses the income and costs and the static color
     * constants of the class to create the chart.
     *
     * @param context the context
     * @param income  the total income
     * @param costs   the total cost
     * @return a GraphicalView object as a pie chart
     */
    public static GraphicalView getNewInstance(Context context, int income, int costs) {
        return ChartFactory.getPieChartView(context, getDataSet(context, income, costs), getRenderer());
    }

    /**
     * Creates the renderer for the pie chart and sets the basic color scheme and hides labels and legend.
     *
     * @return a renderer for the pie chart
     */
    private static DefaultRenderer getRenderer() {
        int[] colors = new int[]{COLOR_GREEN, COLOR_RED};

        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer simpleRenderer = new SimpleSeriesRenderer();
            simpleRenderer.setColor(color);
            simpleRenderer.setDisplayChartValues(true);
            simpleRenderer.setGradientEnabled(true);
            defaultRenderer.addSeriesRenderer(simpleRenderer);
        }
        defaultRenderer.setShowLabels(true);
        defaultRenderer.setLabelsTextSize(24);
        defaultRenderer.setShowLegend(false);
        return defaultRenderer;
    }

    /**
     * Creates the data set used by the piechart by adding the string represantation and it's integer value to a
     * CategorySeries object. Note that the string representations are hard coded.
     *
     * @param context      the context
     * @param successCount the total success results
     * @param failedCount  the total failed results
     * @return a CategorySeries instance with the data supplied
     */
    private static CategorySeries getDataSet(Context context, int successCount, int failedCount) {
        CategorySeries series = new CategorySeries("Chart");
        series.add(context.getString(R.string.success), successCount);
        series.add(context.getString(R.string.failed), failedCount);
        return series;
    }
}