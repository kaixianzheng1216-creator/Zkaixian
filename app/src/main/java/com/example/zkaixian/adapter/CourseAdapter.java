package com.example.zkaixian.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.zkaixian.R;
import com.example.zkaixian.pojo.Course;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseAdapter extends BaseQuickAdapter<Course, BaseViewHolder> {
    public CourseAdapter(List<Course> list) {
        super(R.layout.item_course, list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull Course item) {
        helper.setText(R.id.tv_content, item.getContent());
        helper.setText(R.id.tv_address, item.getAddress());
        helper.setText(R.id.tv_open_class, item.getOpenClass());
    }
}