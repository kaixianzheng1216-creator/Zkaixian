package com.example.zkaixian.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.zkaixian.R;
import com.example.zkaixian.databinding.FragmentMeBinding;
import com.example.zkaixian.utils.UserStorage;
import com.lxj.xpopup.XPopup;

public class MeFragment extends Fragment {

    private FragmentMeBinding binding;
    private UserStorage userStorage;
    private boolean isLogin = false;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMeBinding.inflate(inflater, container, false);
        userStorage = new UserStorage(requireContext());
        initListener();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserState();
    }

    private void refreshUserState() {
        isLogin = userStorage.isLogin();

        if (isLogin) {
            String userName = userStorage.getUserName();
            String userEmail = userStorage.getEmail();

            binding.meTvUserName.setText(userName);
            binding.meTvUserEmail.setText(userEmail);
            binding.meTvUserEmail.setVisibility(View.VISIBLE);

            String avatarUrl = "https://robohash.org/" + userName + ".png";
            Glide.with(this)
                    .load(avatarUrl)
                    .into(binding.meIvAvatar);

            binding.meTvCourseCount.setText("12");
            binding.meTvStudyTime.setText("45h");
            binding.meTvCertificateCount.setText("3");
        } else {
            binding.meTvUserName.setText("请点击登录");
            binding.meTvUserEmail.setVisibility(View.GONE);
            binding.meIvAvatar.setImageResource(android.R.drawable.sym_def_app_icon);

            binding.meTvCourseCount.setText("0");
            binding.meTvStudyTime.setText("0h");
            binding.meTvCertificateCount.setText("0");
        }
    }

    private void initListener() {
        binding.meClUserProfile.setOnClickListener(v -> {
            if (!isLogin) {
                Navigation.findNavController(v)
                        .navigate(R.id.action_navigation_me_to_loginFragment);
            } else {
                Navigation.findNavController(v)
                        .navigate(R.id.action_navigation_me_to_accountProfileFragment);
            }
        });

        binding.meLlAccountProfile.setOnClickListener(v -> {
            if (checkLogin(v)) {
                Navigation.findNavController(v)
                        .navigate(R.id.action_navigation_me_to_accountProfileFragment);
            }
        });

        binding.meLlSecurityPrivacy.setOnClickListener(v -> {
            if (checkLogin(v)) {
                Navigation.findNavController(v)
                        .navigate(R.id.action_navigation_me_to_securityPrivacyFragment);
            }
        });

        binding.meLlShippingAddress.setOnClickListener(v -> {
            if (checkLogin(v)) {
                showDevPopup();
            }
        });

        binding.meLlFavorite.setOnClickListener(v -> {
            if (checkLogin(v)) {
                showDevPopup();
            }
        });

        binding.meLlHistory.setOnClickListener(v -> {
            if (checkLogin(v)) {
                showDevPopup();
            }
        });
    }

    private boolean checkLogin(View v) {
        if (!isLogin) {
            new XPopup.Builder(getContext())
                    .asConfirm("温馨提示", "您需要登录才能访问此功能",
                            "取消", "去登录",
                            () -> {
                                Navigation.findNavController(v)
                                        .navigate(R.id.action_navigation_me_to_loginFragment);
                            }, null, false)
                    .show();

            return false;
        }

        return true;
    }

    private void showDevPopup() {
        new XPopup.Builder(getContext())
                .asConfirm("提示", "该功能正在紧急开发中，敬请期待！",
                        "", "我知道了",
                        null, null, true)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}