package com.example.zkaixian.ui.me.security;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.zkaixian.databinding.FragmentSecurityPrivacyBinding;
import com.example.zkaixian.utils.UserStorage;
import com.google.android.material.snackbar.Snackbar;

public class SecurityPrivacyFragment extends Fragment {
    private FragmentSecurityPrivacyBinding binding;
    private UserStorage userStorage;
    private static final String MOCK_CODE = "123456";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecurityPrivacyBinding.inflate(inflater, container, false);
        userStorage = new UserStorage(requireContext());

        initView();
        initListener();

        return binding.getRoot();
    }

    private void initView() {
        binding.securityPrivacyTvCurrentEmailDisplay.setText(userStorage.getEmail());
    }

    private void initListener() {
        binding.securityPrivacyIvBackAction.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp()
        );

        binding.securityPrivacyTvSendCodeAction.setOnClickListener(v -> sendVerifyCode());

        binding.securityPrivacyBtnSavePasswordAction.setOnClickListener(this::updatePassword);
    }

    private void sendVerifyCode() {
        Snackbar.make(binding.getRoot(), "验证码已发送：" + MOCK_CODE, Snackbar.LENGTH_LONG).show();
    }

    private void updatePassword(View v) {
        String code = binding.securityPrivacyEtVerifyCodeInput.getText().toString().trim();
        String password = binding.securityPrivacyEtNewPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(password)) {
            Snackbar.make(v, "请填写完整信息", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!MOCK_CODE.equals(code)) {
            Snackbar.make(v, "验证码错误", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Snackbar.make(v, "新密码长度至少6位", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Snackbar.make(v, "密码修改成功", Snackbar.LENGTH_SHORT).show();

        Navigation.findNavController(v).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}