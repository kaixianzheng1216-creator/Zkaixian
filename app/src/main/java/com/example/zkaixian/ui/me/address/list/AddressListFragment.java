package com.example.zkaixian.ui.me.address.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.zkaixian.R;
import com.example.zkaixian.adapter.AddressAdapter;
import com.example.zkaixian.databinding.FragmentAddressListBinding;
import com.example.zkaixian.pojo.Address;
import com.example.zkaixian.utils.UserStorage;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;

public class AddressListFragment extends Fragment {
    private FragmentAddressListBinding binding;
    private AddressListViewModel viewModel;
    private AddressAdapter adapter;
    private UserStorage userStorage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddressListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddressListViewModel.class);
        userStorage = new UserStorage(requireContext());
        
        initView();
        initListener();
        initObservers();
        
        return binding.getRoot();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        viewModel.fetchAddresses(userStorage.getEmail());
    }

    private void initView() {
        binding.addressListFragmentRvList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AddressAdapter(new ArrayList<>());

        adapter.setEmptyView(R.layout.layout_empty_view);

        binding.addressListFragmentRvList.setAdapter(adapter);
    }

    private void initListener() {
        binding.addressListFragmentIvBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.addressListFragmentBtnAdd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_addressListFragment_to_addressAddFragment);
        });

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.item_address_tv_address) {
                Address address = (Address) adapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("address_data", address);

                Navigation.findNavController(view).navigate(R.id.action_addressListFragment_to_addressAddFragment, bundle);
            }
        });

        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            Address address = (Address) adapter.getItem(position);

            new XPopup.Builder(getContext())
                    .asConfirm("提示", "确定要删除该地址吗？",
                            "取消", "确定",
                            () -> viewModel.deleteAddress(address.getId()), null, false)
                    .show();

            return true;
        });
    }

    private void initObservers() {
        viewModel.getAddressList().observe(getViewLifecycleOwner(), addresses -> {
            adapter.setList(addresses);
        });

        viewModel.getDeleteResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                viewModel.fetchAddresses(userStorage.getEmail());
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
