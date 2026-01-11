package com.example.zkaixian.ui.me.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.zkaixian.R;
import com.example.zkaixian.databinding.FragmentLoginBinding;
import com.example.zkaixian.utils.UserStorage;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserStorage userStorage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        userStorage = new UserStorage(requireContext());

        initListener();
        return binding.getRoot();
    }

    private void initListener() {
        binding.loginIvBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.loginTvGoRegister.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        );

        binding.loginTvForgotPassword.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        );

        binding.loginBtnSubmit.setOnClickListener(this::handleLogin);
    }

    private void handleLogin(View v) {
        String email = binding.loginEtEmailInput.getText().toString().trim();
        String password = binding.loginEtPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Snackbar.make(v, "请输入邮箱和密码", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!"123@gmail.com".equals(email) || !"123456".equals(password)) {
            Snackbar.make(v, "账号或密码错误", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String name = "用户 123";
        userStorage.saveUserInfo(name, email);

        Snackbar.make(v, "登录成功", Snackbar.LENGTH_SHORT).show();

        Navigation.findNavController(v).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}