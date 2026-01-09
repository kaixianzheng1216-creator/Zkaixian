package com.example.zkaixian.ui.chart.pie;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.zkaixian.databinding.FragmentPieBinding;
import com.example.zkaixian.pojo.Pie;
import com.example.zkaixian.utils.ChartStyleUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class PieFragment extends Fragment {
    private FragmentPieBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PieViewModel pieViewModel = new ViewModelProvider(this).get(PieViewModel.class);
        binding = FragmentPieBinding.inflate(inflater, container, false);

        PieChart pieChart = binding.pieChart;
        initPieSpecificStyle(pieChart);

        pieViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            ArrayList<PieEntry> entries = new ArrayList<>();
            for (Pie pie : data) {
                entries.add(new PieEntry((float) pie.getValue(), pie.getName()));
            }

            PieDataSet dataSet = new PieDataSet(entries, "区域分布占比");
            dataSet.setColors(ChartStyleUtils.COLORS);
            dataSet.setSliceSpace(2f);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData pieData = new PieData(dataSet);
            pieData.setValueTextSize(10f);
            pieData.setValueFormatter(new PercentFormatter(pieChart));

            pieChart.setData(pieData);
            pieChart.animateY(1000);
            pieChart.invalidate();
        });

        return binding.getRoot();
    }

    private void initPieSpecificStyle(PieChart chart) {
        ChartStyleUtils.initBaseStyle(chart);

        chart.setUsePercentValues(true);
        chart.setTransparentCircleAlpha(110);

        chart.setCenterText("区域\n分布");
        chart.setCenterTextSize(14f);
        chart.setCenterTextColor(Color.GRAY);

        Legend l = chart.getLegend();
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setYOffset(0f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}