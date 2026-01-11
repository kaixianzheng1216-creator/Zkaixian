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
            setName("第一周");
            setValue(156);
        }});
        list.add(new Line() {{
            setName("第二周");
            setValue(289);
        }});
        list.add(new Line() {{
            setName("第三周");
            setValue(435);
        }});
        list.add(new Line() {{
            setName("第四周");
            setValue(680);
        }});

        data.setValue(list);

        return data;
    }
}
