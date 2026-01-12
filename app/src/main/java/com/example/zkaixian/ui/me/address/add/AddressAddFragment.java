package com.example.zkaixian.ui.me.address.add;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.zkaixian.R;
import com.example.zkaixian.databinding.FragmentAddressAddBinding;
import com.example.zkaixian.pojo.Address;
import com.example.zkaixian.pojo.AmapTip;
import com.example.zkaixian.utils.UserStorage;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.SimpleCallback;

public class AddressAddFragment extends Fragment {
    private FragmentAddressAddBinding binding;
    private AddressAddViewModel viewModel;
    private UserStorage userStorage;
    private Address editingAddress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddressAddBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddressAddViewModel.class);
        userStorage = new UserStorage(requireContext());

        if (getArguments() != null) {
            editingAddress = (Address) getArguments().getSerializable("address_data");
        }

        initView();
        initListener();
        initObservers();

        getParentFragmentManager().setFragmentResultListener("address_request", getViewLifecycleOwner(), (requestKey, result) -> {
            AmapTip tip = (AmapTip) result.getSerializable("address_result");

            if (tip != null) {
                String fullAddress = "";
                if (tip.getDistrict() != null) fullAddress += tip.getDistrict();
                if (tip.getAddress() != null) fullAddress += tip.getAddress();
                if (tip.getName() != null) fullAddress += tip.getName();

                binding.addressAddFragmentEtAddress.setText(fullAddress);
                binding.etDetail.setText(tip.getName());
            }
        });

        return binding.getRoot();
    }

    private void initView() {
        if (editingAddress != null) {
            binding.addressAddFragmentTvTitle.setText("编辑收货地址");
            binding.addressAddFragmentEtName.setText(editingAddress.getName());
            binding.addressAddFragmentEtPhone.setText(editingAddress.getPhone());
            binding.addressAddFragmentEtAddress.setText(editingAddress.getAddress());
            binding.etDetail.setText(editingAddress.getDetail());
        }
    }

    private void initListener() {
        binding.addressAddFragmentIvBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.addressAddFragmentEtAddress.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_addressAddFragment_to_addressSearchFragment);
        });

        binding.addressAddFragmentBtnSave.setOnClickListener(v -> {
            String name = binding.addressAddFragmentEtName.getText().toString().trim();
            String phone = binding.addressAddFragmentEtPhone.getText().toString().trim();
            String fullAddress = binding.addressAddFragmentEtAddress.getText().toString().trim();
            String detail = binding.etDetail.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(detail)) {
                new XPopup.Builder(getContext()).asCenterList("请填写完整信息", new String[]{"确定"}, null).show();
                return;
            }

            Address address = new Address();
            address.setName(name);
            address.setPhone(phone);
            address.setAddress(fullAddress);
            address.setDetail(detail);

            if (editingAddress != null) {
                viewModel.updateAddress(editingAddress.getId(), address);
            } else {
                viewModel.addAddress(userStorage.getEmail(), address);
            }
        });
    }

    private void initObservers() {
        viewModel.getAddResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                new XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .setPopupCallback(new SimpleCallback() {
                            @Override
                            public void onDismiss(com.lxj.xpopup.core.BasePopupView popupView) {
                                Navigation.findNavController(binding.getRoot()).navigateUp();
                            }
                        })
                        .asLoading("保存成功")
                        .show()
                        .delayDismiss(800);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            new XPopup.Builder(getContext())
                    .asConfirm("提示", error,
                            "取消", "确定",
                            null, null, false)
                    .show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}