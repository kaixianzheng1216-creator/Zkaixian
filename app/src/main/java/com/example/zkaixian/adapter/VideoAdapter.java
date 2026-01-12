package com.example.zkaixian.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.zkaixian.R;
import com.example.zkaixian.api.RetrofitClient;
import com.example.zkaixian.pojo.Video;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VideoAdapter extends BaseQuickAdapter<Video, BaseViewHolder> {
    public VideoAdapter(List<Video> list) {
        super(R.layout.item_video, list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull Video item) {
        helper.setText(R.id.item_video_tv_name, item.getName());
        helper.setText(R.id.item_video_tv_intro, item.getIntro());

        ImageView ivImg = helper.getView(R.id.item_video_iv_img);

        String baseUrl = RetrofitClient.BASE_URL;

        Glide.with(getContext())
                .load(baseUrl + item.getImg())
                .into(ivImg);
    }
}