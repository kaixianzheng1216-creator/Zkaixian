package com.example.zkaixian.ui.chart.line;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.zkaixian.R;
import com.example.zkaixian.databinding.FragmentLineBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
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

        View root = binding.getRoot();

        LineChart lineChart = binding.lineChart;

        lineViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            ArrayList<Entry> entryList = new ArrayList<>();
            String[] xAxisLabels = new String[data.size()];

            for (int i = 0; i < data.size(); i++) {
                entryList.add(new Entry(i, (float) data.get(i).getValue()));
                xAxisLabels[i] = data.get(i).getName();
            }

            LineDataSet lineDataSet = new LineDataSet(entryList, "销售额");
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setColor(R.color.blue_7);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillAlpha(50);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawValues(false);

            LineData lineData = new LineData(lineDataSet);
            lineChart.setData(lineData);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));

            lineChart.getAxisRight().setEnabled(false);
            lineChart.getAxisLeft().setDrawGridLines(false);
            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.getDescription().setEnabled(false);
            lineChart.getLegend().setEnabled(false);

            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}