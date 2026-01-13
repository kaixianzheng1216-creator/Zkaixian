package com.example.zkaixian.ui.me.security;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.zkaixian.databinding.FragmentSecurityPrivacyBinding;
import com.example.zkaixian.utils.UserStorage;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;

public class SecurityPrivacyFragment extends Fragment {
    private FragmentSecurityPrivacyBinding binding;
    private UserStorage userStorage;
    private SecurityPrivacyViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecurityPrivacyBinding.inflate(inflater, container, false);
        userStorage = new UserStorage(requireContext());
        viewModel = new ViewModelProvider(this).get(SecurityPrivacyViewModel.class);

        initView();
        initObservers();
        initListener();

        return binding.getRoot();
    }

    private void initObservers() {
        viewModel.getSendCodeResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                new XPopup.Builder(requireContext())
                        .asLoading("验证码发送成功")
                        .show()
                        .delayDismiss(1500);
            }
        });

        viewModel.getUpdatePasswordResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                new XPopup.Builder(requireContext())
                        .dismissOnTouchOutside(false)
                        .setPopupCallback(new SimpleCallback() {
                            @Override
                            public void onDismiss(BasePopupView popupView) {
                                Navigation.findNavController(binding.getRoot()).navigateUp();
                            }
                        })
                        .asLoading("密码修改成功")
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
        binding.securityPrivacyFragmentTvCurrentEmailDisplay.setText(userStorage.getEmail());
    }

    private void initListener() {
        binding.securityPrivacyFragmentIvBackAction.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp()
        );

        binding.securityPrivacyFragmentTvSendCodeAction.setOnClickListener(v -> sendVerifyCode());

        binding.securityPrivacyFragmentBtnSavePasswordAction.setOnClickListener(this::updatePassword);
    }

    private void sendVerifyCode() {
        String email = userStorage.getEmail();
        if (TextUtils.isEmpty(email)) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "获取当前用户邮箱失败",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }
        viewModel.sendCode(email);
    }

    private void updatePassword(View v) {
        String code = binding.securityPrivacyFragmentEtVerifyCodeInput.getText().toString().trim();
        String password = binding.securityPrivacyFragmentEtNewPasswordInput.getText().toString().trim();
        String email = userStorage.getEmail();

        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(password)) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "请填写完整信息",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }

        if (password.length() < 6) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "新密码长度至少6位",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }

        viewModel.updatePassword(email, code, password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}