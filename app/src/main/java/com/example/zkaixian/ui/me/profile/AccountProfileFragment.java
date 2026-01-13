package com.example.zkaixian.ui.me.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.zkaixian.databinding.FragmentAccountProfileBinding;
import com.example.zkaixian.utils.UserStorage;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;

public class AccountProfileFragment extends Fragment {
    private FragmentAccountProfileBinding binding;
    private UserStorage userStorage;
    private AccountProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountProfileBinding.inflate(inflater, container, false);
        userStorage = new UserStorage(requireContext());
        viewModel = new ViewModelProvider(this).get(AccountProfileViewModel.class);

        initView();

        initObservers();

        initListener();

        return binding.getRoot();
    }

    private void initObservers() {
        viewModel.getUpdateResult().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                userStorage.updateProfile(user.getUsername(), user.getBio());

                new XPopup.Builder(requireContext())
                        .dismissOnTouchOutside(false)
                        .setPopupCallback(new SimpleCallback() {
                            @Override
                            public void onDismiss(BasePopupView popupView) {
                                Navigation.findNavController(binding.getRoot()).navigateUp();
                            }
                        })
                        .asLoading("个人资料保存成功")
                        .show()
                        .delayDismiss(800);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (binding != null) {
                new XPopup.Builder(requireContext())
                        .asConfirm("提示", errorMsg,
                                null, "确定",
                                null, null, true)
                        .show();
            }
        });
    }

    private void initView() {
        String name = userStorage.getUserName();
        String email = userStorage.getEmail();
        String bio = userStorage.getBio();

        binding.accountProfileFragmentEtNickname.setText(name);
        binding.accountProfileTvEmailDisplay.setText(email);
        binding.accountProfileEtBioInput.setText(bio);

        String avatarUrl = "https://robohash.org/" + name + ".png";

        Glide.with(this).load(avatarUrl).into(binding.accountProfileFragmentIvAvatar);
    }

    private void initListener() {
        binding.accountProfileFragmentIvBack.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp()
        );

        binding.accountProfileFragmentBtnSave.setOnClickListener(this::saveProfile);

        binding.accountProfileFragmentBtnLogout.setOnClickListener(this::showLogoutDialog);
    }

    private void saveProfile(View v) {
        String name = binding.accountProfileFragmentEtNickname.getText().toString().trim();
        String bio = binding.accountProfileEtBioInput.getText().toString().trim();
        String email = userStorage.getEmail();

        if (TextUtils.isEmpty(name)) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "昵称不能为空",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }

        viewModel.updateProfile(email, name, bio);
    }

    private void showLogoutDialog(View v) {
        new XPopup.Builder(requireContext())
                .asConfirm("提示", "确定要退出当前账号吗？",
                        "取消", "确定退出",
                        () -> performLogout(v), null, false)
                .show();
    }

    private void performLogout(View v) {
        userStorage.logout();

        new XPopup.Builder(requireContext())
                .dismissOnTouchOutside(false)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onDismiss(BasePopupView popupView) {
                        Navigation.findNavController(v).navigateUp();
                    }
                })
                .asLoading("已退出登录")
                .show()
                .delayDismiss(800);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}