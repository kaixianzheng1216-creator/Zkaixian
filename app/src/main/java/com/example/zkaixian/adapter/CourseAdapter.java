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
        helper.setText(R.id.item_course_tv_title, item.getTitle());
        helper.setText(R.id.item_course_tv_subtitle, item.getSubtitle());
        helper.setText(R.id.item_course_tv_tag, item.getTag());
    }
}