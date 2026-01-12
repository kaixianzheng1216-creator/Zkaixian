package com.example.zkaixian.ui.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.zkaixian.R;
import com.example.zkaixian.databinding.FragmentChartBinding;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

public class ChartFragment extends Fragment {
    private FragmentChartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChartViewModel chartViewModel = new ViewModelProvider(this).get(ChartViewModel.class);

        binding = FragmentChartBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        BoomMenuButton bmb = root.findViewById(R.id.chart_fragment_bmb);
        bmb.setNormalColor(Color.parseColor("#666666"));
        bmb.setHighlightedColor(Color.parseColor("#666666"));

        chartViewModel.getBoomList().observe(getViewLifecycleOwner(), boomList -> {
            for (int i = 0; i < boomList.size(); i++) {
                TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                        .normalImageRes(boomList.get(i).getImageId())
                        .normalColor(Color.parseColor("#FFFFFF"))
                        .highlightedColor(Color.parseColor("#FFFFFF"))
                        .normalText(boomList.get(i).getTitle())
                        .listener(index -> {
                            switch (index) {
                                case 0:
                                    Navigation.findNavController(root).navigate(R.id.action_navigation_chart_to_barFragment);
                                    break;
                                case 1:
                                    Navigation.findNavController(root).navigate(R.id.action_navigation_chart_to_lineFragment);
                                    break;
                                case 2:
                                    Navigation.findNavController(root).navigate(R.id.action_navigation_chart_to_pieFragment);
                                    break;
                            }
                        });

                bmb.addBuilder(builder);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}