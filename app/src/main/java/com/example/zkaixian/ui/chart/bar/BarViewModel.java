package com.example.zkaixian.ui.chart.bar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.pojo.Bar;

import java.util.ArrayList;
import java.util.List;

public class BarViewModel extends ViewModel {
    private final MutableLiveData<List<Bar>> data = new MutableLiveData<>();

    public LiveData<List<Bar>> getData() {
        List<Bar> list = new ArrayList<>();

        list.add(new Bar() {{
            setName("产品A");
            setValue(120);
        }});
        list.add(new Bar() {{
            setName("产品B");
            setValue(90);
        }});
        list.add(new Bar() {{
            setName("产品C");
            setValue(60);
        }});
        list.add(new Bar() {{
            setName("产品D");
            setValue(150);
        }});

        data.setValue(list);

        return data;
    }
}
