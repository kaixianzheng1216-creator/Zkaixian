package com.example.zkaixian.ui.me.address.search;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.zkaixian.adapter.AmapTipAdapter;
import com.example.zkaixian.databinding.FragmentAddressSearchBinding;
import com.example.zkaixian.pojo.AmapTip;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;

public class AddressSearchFragment extends Fragment {

    private FragmentAddressSearchBinding binding;
    private AddressSearchViewModel viewModel;
    private AmapTipAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddressSearchBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddressSearchViewModel.class);
        binding.mapView.onCreate(savedInstanceState);

        initView();
        initListener();
        initObservers();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding != null) {
            binding.mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (binding != null) {
            binding.mapView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        if (binding != null) {
            binding.mapView.onDestroy();
        }

        super.onDestroyView();

        binding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (binding != null) {
            binding.mapView.onSaveInstanceState(outState);
        }
    }

    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AmapTipAdapter(new ArrayList<>());

        binding.recyclerView.setAdapter(adapter);

        binding.etSearch.requestFocus();
    }

    private void initListener() {
        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(binding.etSearch.getText().toString());

                hideKeyboard();

                return true;
            }

            return false;
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            AmapTip tip = (AmapTip) adapter.getItem(position);
            if (tip != null) {
                Bundle result = new Bundle();
                result.putSerializable("address_result", tip);

                getParentFragmentManager().setFragmentResult("address_request", result);

                Navigation.findNavController(view).navigateUp();

                hideKeyboard();
            }
        });
    }

    private void initObservers() {
        viewModel.getSearchResult().observe(getViewLifecycleOwner(), tips -> {
            if (tips != null) {
                adapter.setList(tips);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            hideKeyboard();
            new XPopup.Builder(getContext())
                    .asConfirm("提示", error,
                            "取消", "确定",
                            null, null, false)
                    .show();
        });
    }

    private void performSearch(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            viewModel.searchAddress(keyword.trim());
        } else {
            adapter.setList(new ArrayList<>());
        }
    }

    private void hideKeyboard() {
        if (getContext() != null && binding != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
        }
    }
}