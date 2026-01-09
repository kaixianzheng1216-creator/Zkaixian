package com.example.zkaixian.ui.chart.line;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.zkaixian.databinding.FragmentLineBinding;
import com.example.zkaixian.utils.ChartStyleUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class LineFragment extends Fragment {
    private FragmentLineBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LineViewModel lineViewModel = new ViewModelProvider(this).get(LineViewModel.class);
        binding = FragmentLineBinding.inflate(inflater, container, false);

        LineChart lineChart = binding.lineChart;
        ChartStyleUtils.initXYChartStyle(lineChart);

        lineViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            ArrayList<Entry> entryList = new ArrayList<>();
            String[] xAxisLabels = new String[data.size()];

            for (int i = 0; i < data.size(); i++) {
                entryList.add(new Entry(i, (float) data.get(i).getValue()));
                xAxisLabels[i] = data.get(i).getName();
            }

            lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));

            LineDataSet lineDataSet = new LineDataSet(entryList, "销售额趋势");
            lineDataSet.setValueTextSize(10f);

            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setColor(ChartStyleUtils.COLOR_PRIMARY);
            lineDataSet.setCircleColor(ChartStyleUtils.COLOR_PRIMARY);
            lineDataSet.setLineWidth(2f);
            lineDataSet.setCircleRadius(4f);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillColor(ChartStyleUtils.COLOR_PRIMARY);
            lineDataSet.setFillAlpha(50);

            LineData lineData = new LineData(lineDataSet);

            lineChart.setData(lineData);
            lineChart.animateY(1000);
            lineChart.invalidate();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}