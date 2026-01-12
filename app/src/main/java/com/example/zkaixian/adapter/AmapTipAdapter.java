package com.example.zkaixian.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.zkaixian.R;
import com.example.zkaixian.pojo.AmapTip;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AmapTipAdapter extends BaseQuickAdapter<AmapTip, BaseViewHolder> {
    public AmapTipAdapter(List<AmapTip> list) {
        super(R.layout.item_amap_tip, list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull AmapTip item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.item_amap_tip_tv_address, item.getDistrict() + item.getAddress());
    }
}