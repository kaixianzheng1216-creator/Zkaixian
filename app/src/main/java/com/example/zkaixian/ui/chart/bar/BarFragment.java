package com.example.zkaixian.ui.chart.bar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.zkaixian.databinding.FragmentBarBinding;
import com.example.zkaixian.utils.ChartStyleUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class BarFragment extends Fragment {
    private FragmentBarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BarViewModel barViewModel = new ViewModelProvider(this).get(BarViewModel.class);
        binding = FragmentBarBinding.inflate(inflater, container, false);

        BarChart barChart = binding.barChart;
        ChartStyleUtils.initXYChartStyle(barChart);

        barViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            ArrayList<BarEntry> entries = new ArrayList<>();
            String[] xAxisLabels = new String[data.size()];

            for (int i = 0; i < data.size(); i++) {
                entries.add(new BarEntry(i, (float) data.get(i).getValue()));
                xAxisLabels[i] = data.get(i).getName();
            }

            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));

            BarDataSet barDataSet = new BarDataSet(entries, "产品销量对比");
            barDataSet.setValueTextSize(10f);
            barDataSet.setColors(ChartStyleUtils.COLORS);

            BarData barData = new BarData(barDataSet);

            barChart.setData(barData);
            barChart.animateY(1000);
            barChart.invalidate();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}