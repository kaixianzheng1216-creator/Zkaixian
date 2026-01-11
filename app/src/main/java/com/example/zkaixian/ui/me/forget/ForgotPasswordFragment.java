package com.example.zkaixian.ui.me.forget;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.zkaixian.databinding.FragmentForgotPasswordBinding;
import com.example.zkaixian.utils.UserStorage;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;
    private UserStorage userStorage;
    private ForgotPasswordViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        userStorage = new UserStorage(requireContext());
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(ForgotPasswordViewModel.class);

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

        viewModel.getResetPasswordResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                userStorage.setLogin(false);
                new XPopup.Builder(requireContext())
                        .dismissOnTouchOutside(false)
                        .setPopupCallback(new SimpleCallback() {
                            @Override
                            public void onDismiss(BasePopupView popupView) {
                                Navigation.findNavController(binding.getRoot()).navigateUp();
                            }
                        })
                        .asLoading("密码重置成功，请重新登录")
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

    private void initListener() {
        binding.forgotIvBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.forgotTvGetCode.setOnClickListener(v -> sendVerifyCode());

        binding.forgotBtnResetPassword.setOnClickListener(this::resetPassword);
    }

    private void sendVerifyCode() {
        String email = binding.forgotEtEmailInput.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "请先输入邮箱",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }
        viewModel.sendCode(email);
    }

    private void resetPassword(View v) {
        String email = binding.forgotEtEmailInput.getText().toString().trim();
        String code = binding.forgotEtCodeInput.getText().toString().trim();
        String newPassword = binding.forgotEtNewPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(code) || TextUtils.isEmpty(newPassword)) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "请完整填写信息",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }

        viewModel.resetPassword(email, code, newPassword);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}