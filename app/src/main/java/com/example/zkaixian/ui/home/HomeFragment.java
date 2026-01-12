package com.example.zkaixian.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zkaixian.R;
import com.example.zkaixian.adapter.HomeAdapter;
import com.example.zkaixian.adapter.ImageBannerAdapter;
import com.example.zkaixian.adapter.ImageTitleBannerAdapter;
import com.example.zkaixian.databinding.FragmentHomeBinding;
import com.example.zkaixian.pojo.News;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Collections;
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
        binding.homeFragmentRvNews.setLayoutManager(new LinearLayoutManager(getContext()));

        initAdapter();

        addHeaderView();

        binding.homeFragmentRvNews.setAdapter(homeAdapter);
    }

    private void initAdapter() {
        homeAdapter = new HomeAdapter(new ArrayList<>());

        homeAdapter.setEmptyView(R.layout.layout_empty_view);

        homeAdapter.setOnItemClickListener(this::onNewsItemClick);
    }

    private void onNewsItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        News item = homeAdapter.getData().get(position);

        Bundle bundle = new Bundle();
        bundle.putString("url", item.getNewsUrl());

        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_webFragment, bundle);
    }

    private void addHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.layout_home_header, binding.homeFragmentRvNews, false);

        initHeaderBanner(headerView);
        initHeaderMenu(headerView);

        homeAdapter.setHeaderView(headerView);
    }

    private void initHeaderBanner(View headerView) {
        banner = headerView.findViewById(R.id.layout_home_header_banner);

        List<Integer> defaultImages = Collections.singletonList(R.drawable.banner_default);

        banner.addBannerLifecycleObserver(this)
                .setAdapter(new ImageBannerAdapter(defaultImages));
    }

    private void initHeaderMenu(View headerView) {
        headerView.findViewById(R.id.layout_home_header_ll_menu_course).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_courseFragment);
        });
    }

    private void initRefreshLayout() {
        RefreshLayout refreshLayout = binding.homeFragmentRefreshLayout;

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
                        .setAdapter(new ImageTitleBannerAdapter(ads));
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