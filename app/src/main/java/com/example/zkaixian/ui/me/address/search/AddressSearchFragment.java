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

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.example.zkaixian.adapter.AmapTipAdapter;
import com.example.zkaixian.databinding.FragmentAddressSearchBinding;
import com.example.zkaixian.pojo.AmapTip;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;

public class AddressSearchFragment extends Fragment {
    private FragmentAddressSearchBinding binding;
    private AddressSearchViewModel viewModel;
    private AmapTipAdapter adapter;
    private AMap aMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddressSearchBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddressSearchViewModel.class);
        binding.addressSearchFragmentMapView.onCreate(savedInstanceState);

        initView();
        initListener();
        initObservers();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding != null) {
            binding.addressSearchFragmentMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (binding != null) {
            binding.addressSearchFragmentMapView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        if (binding != null) {
            binding.addressSearchFragmentMapView.onDestroy();
        }

        super.onDestroyView();

        binding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (binding != null) {
            binding.addressSearchFragmentMapView.onSaveInstanceState(outState);
        }
    }

    private void initView() {
        binding.addressSearchFragmentRvList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AmapTipAdapter(new ArrayList<>());

        binding.addressSearchFragmentRvList.setAdapter(adapter);

        binding.addressSearchFragmentEtSearch.requestFocus();

        if (aMap == null) {
            aMap = binding.addressSearchFragmentMapView.getMap();
        }
    }

    private void initListener() {
        binding.addressSearchFragmentIvBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.addressSearchFragmentEtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(binding.addressSearchFragmentEtSearch.getText().toString());

                hideKeyboard();

                return true;
            }

            return false;
        });

        binding.addressSearchFragmentEtSearch.addTextChangedListener(new TextWatcher() {
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
            if (!tips.isEmpty()) {
                adapter.setList(tips);

                updateMap(tips.get(0));
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

            imm.hideSoftInputFromWindow(binding.addressSearchFragmentEtSearch.getWindowToken(), 0);
        }
    }

    private void updateMap(AmapTip tip) {
        if (tip == null || tip.getLocation() == null || tip.getLocation().isEmpty()) {
            return;
        }

        String[] loc = tip.getLocation().split(",");

        if (loc.length == 2) {
            double lon = Double.parseDouble(loc[0]);
            double lat = Double.parseDouble(loc[1]);

            LatLng latLng = new LatLng(lat, lon);

            if (aMap != null) {
                aMap.clear();

                aMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(tip.getName())
                        .snippet(tip.getAddress()));

                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        }
    }
}