package com.example.zkaixian.ui.chart.line;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.pojo.Line;

import java.util.ArrayList;
import java.util.List;

public class LineViewModel extends ViewModel {
    private final MutableLiveData<List<Line>> data = new MutableLiveData<>();

    public LiveData<List<Line>> getData() {
        List<Line> list = new ArrayList<>();

        list.add(new Line() {{
            setName("1月");
            setValue(100);
        }});
        list.add(new Line() {{
            setName("2月");
            setValue(130);
        }});
        list.add(new Line() {{
            setName("3月");
            setValue(120);
        }});
        list.add(new Line() {{
            setName("4月");
            setValue(160);
        }});

        data.setValue(list);

        return data;
    }
}
