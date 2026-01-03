package com.example.zkaixian.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.zkaixian.R;
import com.example.zkaixian.api.RetrofitClient;
import com.example.zkaixian.pojo.News;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeAdapter extends BaseMultiItemQuickAdapter<News, BaseViewHolder> {
    public HomeAdapter(List<News> data) {
        super(data);

        addItemType(1, R.layout.item_news_single_img);
        addItemType(2, R.layout.item_news_double_img);
        addItemType(3, R.layout.item_news_triple_img);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, News item) {
        helper.setText(R.id.tv_news_title, item.getNewsName());
        helper.setText(R.id.tv_news_type, item.getNewsTypeName());

        switch (helper.getItemViewType()) {
            case 1:
                loadImage(helper, R.id.iv_img_1, item.getImg1());
                break;
            case 2:
                loadImage(helper, R.id.iv_img_1, item.getImg1());
                loadImage(helper, R.id.iv_img_2, item.getImg2());
                break;
            case 3:
                loadImage(helper, R.id.iv_img_1, item.getImg1());
                loadImage(helper, R.id.iv_img_2, item.getImg2());
                loadImage(helper, R.id.iv_img_3, item.getImg3());
                break;
            default:
                break;
        }
    }

    private void loadImage(BaseViewHolder helper, int viewId, String url) {
        ImageView imageView = helper.getView(viewId);

        String baseUrl = RetrofitClient.BASE_URL;

        Glide.with(getContext())
                .load(baseUrl + url)
                .centerCrop()
                .into(imageView);
    }
}