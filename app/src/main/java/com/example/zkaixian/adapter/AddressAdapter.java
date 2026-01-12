package com.example.zkaixian.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.zkaixian.R;
import com.example.zkaixian.pojo.Address;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddressAdapter extends BaseQuickAdapter<Address, BaseViewHolder> {
    public AddressAdapter(List<Address> list) {
        super(R.layout.item_address, list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull Address item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_phone, item.getPhone());
        helper.setText(R.id.tv_address, item.getAddress() + " " + item.getDetail());
    }
}