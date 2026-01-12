package com.example.zkaixian.ui.video.detail;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.zkaixian.R;
import com.example.zkaixian.adapter.EpisodeAdapter;
import com.example.zkaixian.api.RetrofitClient;
import com.example.zkaixian.databinding.FragmentVideoDetailBinding;
import com.example.zkaixian.pojo.Video;
import com.example.zkaixian.pojo.VideoDetail;
import com.google.android.material.tabs.TabLayout;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;

public class VideoDetailFragment extends Fragment {
    private static final String TEST_VIDEO_URL = "http://vjs.zencdn.net/v/oceans.mp4";

    private FragmentVideoDetailBinding binding;
    private EpisodeAdapter episodeAdapter;
    private StandardGSYVideoPlayer detailPlayer;
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    private boolean isPause;
    private String mCoverUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
                if (GSYVideoManager.backFromWindowFull(requireActivity())) {
                    return;
                }
                setEnabled(false);
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideoDetailBinding.inflate(inflater, container, false);
        detailPlayer = binding.videoDetailPlayer;
        orientationUtils = new OrientationUtils(getActivity(), detailPlayer);
        orientationUtils.setEnable(false);

        initViews();
        initRecyclerView();
        loadData();

        return binding.getRoot();
    }

    private void initViews() {
        binding.videoDetailTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                boolean isIntro = tab.getPosition() == 0;
                binding.videoDetailScrollIntro.setVisibility(isIntro ? View.VISIBLE : View.GONE);
                binding.videoDetailRvEpisodes.setVisibility(isIntro ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        detailPlayer.getFullscreenButton().setOnClickListener(v -> {
            orientationUtils.resolveByClick();
            detailPlayer.startWindowFullscreen(getActivity(), true, true);
        });

        detailPlayer.getBackButton().setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    private void initRecyclerView() {
        binding.videoDetailRvEpisodes.setLayoutManager(new LinearLayoutManager(getContext()));
        episodeAdapter = new EpisodeAdapter(new ArrayList<>());
        episodeAdapter.setEmptyView(R.layout.layout_empty_view);

        episodeAdapter.setOnItemClickListener((adapter, view, position) -> {
            VideoDetail detail = (VideoDetail) adapter.getItem(position);
            if (detail != null) {
                playVideo(TEST_VIDEO_URL, detail.getVideoName());
            }
        });

        binding.videoDetailRvEpisodes.setAdapter(episodeAdapter);
    }

    private void playVideo(String url, String title) {
        if (getContext() == null) return;

        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (mCoverUrl != null) Glide.with(this).load(mCoverUrl).into(imageView);

        new GSYVideoOptionBuilder()
                .setUrl(url)
                .setVideoTitle(title)
                .setCacheWithPlay(false)
                .setThumbImageView(imageView)
                .setShowFullAnimation(false)
                .setAutoFullWithSize(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setNeedLockFull(true)
                .setIsTouchWiget(true)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);

                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);

                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);

                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                })
                .build(detailPlayer);

        detailPlayer.startPlayLogic();
    }

    private void loadData() {
        Bundle arguments = getArguments();

        if (arguments != null) {
            Video video = (Video) arguments.getSerializable("video_data");

            if (video != null) {
                binding.videoDetailTvName.setText(video.getName());
                binding.videoDetailTvIntro.setText(video.getIntro());
                mCoverUrl = RetrofitClient.BASE_URL + video.getImg();

                if (video.getVideoDetailList() != null && !video.getVideoDetailList().isEmpty()) {
                    episodeAdapter.setList(video.getVideoDetailList());
                    playVideo(TEST_VIDEO_URL, video.getVideoDetailList().get(0).getVideoName());
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        detailPlayer.getCurrentPlayer().onVideoPause();
        isPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        detailPlayer.getCurrentPlayer().onVideoResume(false);
        isPause = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isPlay) detailPlayer.getCurrentPlayer().release();
        if (orientationUtils != null) orientationUtils.releaseListener();
        binding = null;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(getActivity(), newConfig, orientationUtils, true, true);
        }
    }
}