package com.example.zkaixian.ui.chart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.pojo.Button;
import com.example.zkaixian.R;

import java.util.ArrayList;
import java.util.List;

public class ChartViewModel extends ViewModel {
    private final MutableLiveData<List<Button>> boomList = new MutableLiveData<>();

    public ChartViewModel() {
        List<Button> list = new ArrayList<>();

        Button btn1 = new Button();
        btn1.setImageId(R.drawable.bar_chart_24px);
        btn1.setTitle("编程语言热度");

        Button btn2 = new Button();
        btn2.setImageId(R.drawable.show_chart_24px);
        btn2.setTitle("用户增长趋势");

        Button btn3 = new Button();
        btn3.setImageId(R.drawable.pie_chart_24px);
        btn3.setTitle("技术栈分布占比");

        list.add(btn1);
        list.add(btn2);
        list.add(btn3);

        boomList.setValue(list);
    }

    public LiveData<List<Button>> getBoomList() {
        return boomList;
    }
}