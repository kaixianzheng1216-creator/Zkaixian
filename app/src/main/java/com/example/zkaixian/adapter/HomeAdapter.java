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
        switch (helper.getItemViewType()) {
            case 1:
                helper.setText(R.id.item_news_single_img_tv_title, item.getNewsName());
                helper.setText(R.id.item_news_single_img_tv_type, item.getNewsTypeName());
                loadImage(helper, R.id.item_news_single_img_iv_1, item.getImg1());
                break;
            case 2:
                helper.setText(R.id.item_news_double_img_tv_title, item.getNewsName());
                helper.setText(R.id.item_news_double_img_tv_type, item.getNewsTypeName());
                loadImage(helper, R.id.item_news_double_img_iv_1, item.getImg1());
                loadImage(helper, R.id.item_news_double_img_iv_2, item.getImg2());
                break;
            case 3:
                helper.setText(R.id.item_news_triple_img_tv_title, item.getNewsName());
                helper.setText(R.id.item_news_triple_img_tv_type, item.getNewsTypeName());
                loadImage(helper, R.id.item_news_triple_img_iv_1, item.getImg1());
                loadImage(helper, R.id.item_news_triple_img_iv_2, item.getImg2());
                loadImage(helper, R.id.item_news_triple_img_iv_3, item.getImg3());
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