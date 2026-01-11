package com.example.zkaixian.ui.chart.pie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zkaixian.pojo.Pie;

import java.util.ArrayList;
import java.util.List;

public class PieViewModel extends ViewModel {
    private final MutableLiveData<List<Pie>> data = new MutableLiveData<>();

    public LiveData<List<Pie>> getData() {
        List<Pie> list = new ArrayList<>();

        list.add(new Pie() {{
            setName("Android");
            setValue(45);
        }});
        list.add(new Pie() {{
            setName("Backend");
            setValue(30);
        }});
        list.add(new Pie() {{
            setName("Web");
            setValue(15);
        }});
        list.add(new Pie() {{
            setName("AI");
            setValue(10);
        }});

        data.setValue(list);

        return data;
    }
}
