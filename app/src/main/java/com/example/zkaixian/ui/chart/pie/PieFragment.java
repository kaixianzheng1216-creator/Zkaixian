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
        
        binding.ivBack.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(v).navigateUp());

        PieChart pieChart = binding.pieChart;
        initPieSpecificStyle(pieChart);

        pieViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            ArrayList<PieEntry> entries = new ArrayList<>();
            for (Pie pie : data) {
                entries.add(new PieEntry((float) pie.getValue(), pie.getName()));
            }

            PieDataSet dataSet = new PieDataSet(entries, "技术栈分布占比");
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

        // 设置额外偏移量，防止图表与边缘或Legend重叠
        chart.setExtraOffsets(5.f, 10.f, 5.f, 5.f);

        chart.setCenterText("技术\n栈");
        chart.setCenterTextSize(14f);
        chart.setCenterTextColor(Color.GRAY);

        Legend l = chart.getLegend();
        // 修改Legend位置到底部居中，避免与图表挤在一起
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(5f);
        l.setXEntrySpace(10f);
        l.setWordWrapEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}