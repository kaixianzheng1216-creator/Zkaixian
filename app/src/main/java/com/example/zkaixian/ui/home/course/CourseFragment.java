package com.example.zkaixian.ui.home.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.zkaixian.R;
import com.example.zkaixian.adapter.CourseAdapter;
import com.example.zkaixian.databinding.FragmentCourseBinding;

import java.util.ArrayList;

public class CourseFragment extends Fragment {
    private FragmentCourseBinding binding;
    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCourseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        root.findViewById(R.id.course_fragment_iv_back).setOnClickListener(v -> androidx.navigation.Navigation.findNavController(v).navigateUp());

        initViewModel();

        initRecyclerView();

        observeViewModel();

        loadData();

        return root;
    }

    private void initViewModel() {
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
    }

    private void initRecyclerView() {
        binding.courseFragmentRvCourse.setLayoutManager(new LinearLayoutManager(getContext()));

        courseAdapter = new CourseAdapter(new ArrayList<>());

        courseAdapter.setEmptyView(R.layout.layout_empty_view);

        binding.courseFragmentRvCourse.setAdapter(courseAdapter);
    }

    private void observeViewModel() {
        courseViewModel.getPythonCourseList().observe(getViewLifecycleOwner(), courses -> {
            if (courses != null) {
                courseAdapter.setList(courses);
            }
        });
    }

    private void loadData() {
        courseViewModel.fetchPythonCourses();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}