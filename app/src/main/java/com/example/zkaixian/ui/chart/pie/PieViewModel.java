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
            setName("北区");
            setValue(40);
        }});
        list.add(new Pie() {{
            setName("南区");
            setValue(25);
        }});
        list.add(new Pie() {{
            setName("东区");
            setValue(20);
        }});
        list.add(new Pie() {{
            setName("西区");
            setValue(15);
        }});

        data.setValue(list);

        return data;
    }
}
