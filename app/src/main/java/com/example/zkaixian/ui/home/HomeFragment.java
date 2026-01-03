package com.example.zkaixian.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.zkaixian.R;
import com.example.zkaixian.adapter.HomeAdapter;
import com.example.zkaixian.adapter.ImageBannerAdapter;
import com.example.zkaixian.adapter.ImageTitleNumBannerAdapter;
import com.example.zkaixian.databinding.FragmentHomeBinding;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private HomeAdapter homeAdapter;
    private Banner banner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViewModel();

        initRecyclerView();

        initRefreshLayout();

        observeViewModel();

        loadData();

        return root;
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        binding.recyclerView.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(new ArrayList<>());

        binding.recyclerView.setAdapter(homeAdapter);

        homeAdapter.setEmptyView(R.layout.layout_empty_view);

        View headerView = getLayoutInflater().inflate(R.layout.layout_home_header, binding.recyclerView, false);

        homeAdapter.setHeaderView(headerView);

        banner = headerView.findViewById(R.id.banner);

        List<Integer> defaultImages = new ArrayList<>();
        defaultImages.add(R.drawable.banner_default);
        defaultImages.add(R.drawable.banner_default);
        defaultImages.add(R.drawable.banner_default);
        defaultImages.add(R.drawable.banner_default);

        banner.addBannerLifecycleObserver(this)
                .setAdapter(new ImageBannerAdapter(defaultImages));
    }

    private void initRefreshLayout() {
        RefreshLayout refreshLayout = binding.refreshLayout;

        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));

        refreshLayout.setOnRefreshListener(refresh -> {
            loadData();
            refresh.finishRefresh(1000);
        });

        refreshLayout.setOnLoadMoreListener(refresh -> {
            refresh.finishLoadMore(1000);
        });
    }

    private void observeViewModel() {
        homeViewModel.getAdList().observe(getViewLifecycleOwner(), ads -> {
            if (ads != null) {
                banner.addBannerLifecycleObserver(this)
                        .setAdapter(new ImageTitleNumBannerAdapter(ads));
            }
        });

        homeViewModel.getNewsList().observe(getViewLifecycleOwner(), news -> {
            if (news != null) {
                homeAdapter.setList(news);
            }
        });
    }

    private void loadData() {
        homeViewModel.fetchAds();
        homeViewModel.fetchNews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}