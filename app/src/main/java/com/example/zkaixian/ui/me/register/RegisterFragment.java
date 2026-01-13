package com.example.zkaixian.ui.me.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.zkaixian.databinding.FragmentRegisterBinding;
import com.example.zkaixian.utils.UserStorage;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private UserStorage userStorage;
    private RegisterViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        userStorage = new UserStorage(requireContext());
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

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

        viewModel.getRegisterResult().observe(getViewLifecycleOwner(), user -> {
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
                        .asLoading("注册成功，已自动登录")
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
        binding.registerFragmentIvBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.registerFragmentTvGoLogin.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.registerFragmentTvGetCode.setOnClickListener(v -> sendVerifyCode());

        binding.registerFragmentBtnSubmit.setOnClickListener(this::handleRegister);
    }

    private void sendVerifyCode() {
        String email = binding.registerFragmentEtEmailInput.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "请先输入邮箱地址",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }

        viewModel.sendCode(email);
    }

    private void handleRegister(View v) {
        String name = binding.registerFragmentEtNicknameInput.getText().toString().trim();
        String email = binding.registerFragmentEtEmailInput.getText().toString().trim();
        String code = binding.registerFragmentEtCodeInput.getText().toString().trim();
        String password = binding.registerFragmentEtPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(code)) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "请完善所有信息",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            new XPopup.Builder(requireContext())
                    .asConfirm("提示", "密码长度至少6位",
                            null, "确定",
                            null, null, true)
                    .show();
            return;
        }

        viewModel.register(email, password, name, code);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
