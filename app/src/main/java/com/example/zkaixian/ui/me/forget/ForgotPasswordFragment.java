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
import com.google.android.material.snackbar.Snackbar;

public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    private UserStorage userStorage;
    private static final String MOCK_CODE = "123456";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        userStorage = new UserStorage(requireContext());

        initListener();
        return binding.getRoot();
    }

    private void initListener() {
        binding.forgotIvBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.forgotTvGetCode.setOnClickListener(v -> sendVerifyCode());

        binding.forgotBtnResetPassword.setOnClickListener(this::resetPassword);
    }

    private void sendVerifyCode() {
        String email = binding.forgotEtEmailInput.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Snackbar.make(binding.getRoot(), "请先输入邮箱", Snackbar.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(binding.getRoot(), "验证码已发送：" + MOCK_CODE, Snackbar.LENGTH_SHORT).show();
    }

    private void resetPassword(View v) {
        String email = binding.forgotEtEmailInput.getText().toString().trim();
        String code = binding.forgotEtCodeInput.getText().toString().trim();
        String newPassword = binding.forgotEtNewPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(code) || TextUtils.isEmpty(newPassword)) {
            Snackbar.make(v, "请完整填写信息", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!MOCK_CODE.equals(code)) {
            Snackbar.make(v, "验证码错误", Snackbar.LENGTH_SHORT).show();
            return;
        }

        userStorage.setLogin(false);

        Snackbar.make(v, "密码重置成功，请重新登录", Snackbar.LENGTH_SHORT).show();

        Navigation.findNavController(v).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}