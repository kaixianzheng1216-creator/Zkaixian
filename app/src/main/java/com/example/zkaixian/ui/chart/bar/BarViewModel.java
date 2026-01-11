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
            setName("Java");
            setValue(85);
        }});
        list.add(new Bar() {{
            setName("Kotlin");
            setValue(92);
        }});
        list.add(new Bar() {{
            setName("Python");
            setValue(76);
        }});
        list.add(new Bar() {{
            setName("C++");
            setValue(65);
        }});

        data.setValue(list);

        return data;
    }
}
