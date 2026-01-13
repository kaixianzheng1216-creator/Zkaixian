package com.example.zkaixian.ui.me.login;

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
import com.example.zkaixian.databinding.FragmentLoginBinding;
import com.example.zkaixian.utils.UserStorage;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private UserStorage userStorage;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        userStorage = new UserStorage(requireContext());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        initObservers();

        initListener();

        return binding.getRoot();
    }

    private void initObservers() {
        viewModel.getLoginResult().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                userStorage.saveUserInfo(user.getUsername(), user.getEmail());
                new XPopup.Builder(requireContext())
                        .dismissOnTouchOutside(false)
                        .setPopupCallback(new SimpleCallback() {
                            @Override
                            public void onDismiss(BasePopupView popupView) {
                                Navigation.findNavController(binding.getRoot()).navigateUp();
                            }
                        })
                        .asLoading("登录成功")
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
        binding.loginFragmentIvBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.loginFragmentTvGoRegister.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        );

        binding.loginTvForgotPassword.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        );

        binding.loginBtnSubmit.setOnClickListener(this::handleLogin);
    }

    private void handleLogin(View v) {
        String email = binding.loginFragmentEtEmail.getText().toString().trim();
        String password = binding.loginFragmentEtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "请输入邮箱和密码",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }

        viewModel.login(email, password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}