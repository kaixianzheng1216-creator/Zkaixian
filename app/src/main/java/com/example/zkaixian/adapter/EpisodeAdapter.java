package com.example.zkaixian.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.zkaixian.R;
import com.example.zkaixian.pojo.VideoDetail;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class EpisodeAdapter extends BaseQuickAdapter<VideoDetail, BaseViewHolder> {
    public EpisodeAdapter(List<VideoDetail> data) {
        super(R.layout.item_episode, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, VideoDetail item) {
        helper.setText(R.id.tv_episode_name, item.getVideoName());
    }
}