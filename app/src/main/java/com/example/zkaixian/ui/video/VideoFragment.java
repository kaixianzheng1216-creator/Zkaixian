package com.example.zkaixian.ui.video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.zkaixian.R;
import com.example.zkaixian.adapter.VideoAdapter;
import com.example.zkaixian.databinding.FragmentVideoBinding;
import com.example.zkaixian.pojo.Video;

import java.util.ArrayList;

public class VideoFragment extends Fragment {
    private FragmentVideoBinding binding;
    private VideoViewModel videoViewModel;
    private VideoAdapter videoAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViewModel();
        initRecyclerView();
        observeViewModel();
        loadData();

        return root;
    }

    private void initViewModel() {
        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
    }

    private void initRecyclerView() {
        binding.videoFragmentRvList.setLayoutManager(new LinearLayoutManager(getContext()));

        videoAdapter = new VideoAdapter(new ArrayList<>());
        videoAdapter.setEmptyView(R.layout.layout_empty_view);

        videoAdapter.setOnItemClickListener((adapter, view, position) -> {
            Video clickedVideo = (Video) adapter.getItem(position);

            if (clickedVideo != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("video_data", clickedVideo);

                Navigation.findNavController(view).navigate(R.id.action_navigation_video_to_videoDetailFragment, bundle);
            }
        });

        binding.videoFragmentRvList.setAdapter(videoAdapter);
    }

    private void observeViewModel() {
        videoViewModel.getVideoList().observe(getViewLifecycleOwner(), videos -> {
            if (videos != null) {
                videoAdapter.setList(videos);
            }
        });
    }

    private void loadData() {
        videoViewModel.fetchVideoList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}