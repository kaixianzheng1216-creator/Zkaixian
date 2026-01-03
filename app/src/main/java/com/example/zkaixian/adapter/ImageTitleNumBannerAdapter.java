package com.example.zkaixian.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zkaixian.R;
import com.example.zkaixian.api.RetrofitClient;
import com.example.zkaixian.pojo.News;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class ImageTitleNumBannerAdapter extends BannerAdapter<News, ImageTitleNumBannerAdapter.BannerViewHolder> {
    public ImageTitleNumBannerAdapter(List<News> mDatas) {
        super(mDatas);
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner, parent, false);

        return new BannerViewHolder(view);
    }

    @Override
    public void onBindView(BannerViewHolder holder, News data, int position, int size) {
        String baseUrl = RetrofitClient.BASE_URL;

        Glide.with(holder.imageView)
                .load(baseUrl + data.getImg1())
                .into(holder.imageView);

        holder.titleView.setText(data.getNewsName());
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;

        public BannerViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.bannerImage);
            titleView = view.findViewById(R.id.bannerTitle);
        }
    }
}