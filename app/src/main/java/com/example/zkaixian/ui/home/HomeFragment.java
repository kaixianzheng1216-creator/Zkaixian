package com.example.zkaixian.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.zkaixian.R;
import com.example.zkaixian.databinding.FragmentHomeBinding;
import com.example.zkaixian.pojo.News;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View root = binding.getRoot();

        homeViewModel.getAdList().observe(getViewLifecycleOwner(), ads -> {
            if (ads != null) {
                for (News item : ads) {
                    Log.d("HomeFragment", "广告: " + item.toString());
                }
            }
        });

        homeViewModel.getNewsList().observe(getViewLifecycleOwner(), news -> {
            if (news != null) {
                for (News item : news) {
                    Log.d("HomeFragment", "新闻: " + item.toString());
                }
            }
        });

        RefreshLayout refreshLayout = root.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));

        refreshLayout.setOnRefreshListener(refreshlayout -> {
            homeViewModel.fetchAds();
            homeViewModel.fetchNews();

            refreshlayout.finishRefresh(2000);
        });

        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            refreshlayout.finishLoadMore(2000);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}