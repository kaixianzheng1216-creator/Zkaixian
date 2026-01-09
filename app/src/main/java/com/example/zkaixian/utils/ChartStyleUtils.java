package com.example.zkaixian.utils;

import android.graphics.Color;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class ChartStyleUtils {

    public static final int COLOR_PRIMARY = Color.parseColor("#0052d9");
    public static final int[] COLORS = {
            Color.parseColor("#0052d9"),
            Color.parseColor("#d54941"),
            Color.parseColor("#e37318"),
            Color.parseColor("#2ba471")
    };

    public static void initBaseStyle(Chart<?> chart) {
        chart.getDescription().setEnabled(false);
        chart.setNoDataText("暂无数据");

        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
    }

    public static void initXYChartStyle(BarLineChartBase<?> chart) {
        initBaseStyle(chart);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
    }
}