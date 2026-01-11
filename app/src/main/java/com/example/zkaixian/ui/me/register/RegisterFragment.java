package com.example.zkaixian.ui.me.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.zkaixian.databinding.FragmentRegisterBinding;
import com.example.zkaixian.utils.UserStorage;
import com.google.android.material.snackbar.Snackbar;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private UserStorage userStorage;
    private static final String MOCK_CODE = "123456";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        userStorage = new UserStorage(requireContext());

        initListener();
        return binding.getRoot();
    }

    private void initListener() {
        binding.registerIvBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.registerTvGoLogin.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.registerTvGetCode.setOnClickListener(v -> sendVerifyCode());

        binding.registerBtnSubmit.setOnClickListener(this::handleRegister);
    }

    private void sendVerifyCode() {
        String email = binding.registerEtEmailInput.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Snackbar.make(binding.getRoot(), "请先输入邮箱地址", Snackbar.LENGTH_SHORT).show();

            return;
        }

        Snackbar.make(binding.getRoot(), "验证码已发送：" + MOCK_CODE, Snackbar.LENGTH_LONG).show();
    }

    private void handleRegister(View v) {
        String name = binding.registerEtNicknameInput.getText().toString().trim();
        String email = binding.registerEtEmailInput.getText().toString().trim();
        String code = binding.registerEtCodeInput.getText().toString().trim();
        String password = binding.registerEtPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(code)) {
            Snackbar.make(v, "请完善所有信息", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Snackbar.make(v, "密码长度至少6位", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!MOCK_CODE.equals(code)) {
            Snackbar.make(v, "验证码错误", Snackbar.LENGTH_SHORT).show();
            return;
        }

        userStorage.saveUserInfo(name, email);

        Snackbar.make(v, "注册成功，已自动登录", Snackbar.LENGTH_SHORT).show();

        Navigation.findNavController(v).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}