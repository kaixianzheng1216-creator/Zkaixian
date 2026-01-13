package com.example.zkaixian.ui.me;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.zkaixian.R;
import com.example.zkaixian.databinding.FragmentMeBinding;
import com.example.zkaixian.utils.UserStorage;
import com.lxj.xpopup.XPopup;

public class MeFragment extends Fragment {
    private FragmentMeBinding binding;
    private UserStorage userStorage;
    private MeViewModel viewModel;
    private boolean isLogin = false;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMeBinding.inflate(inflater, container, false);

        userStorage = new UserStorage(requireContext());

        viewModel = new ViewModelProvider(this).get(MeViewModel.class);

        initObservers();

        initListener();

        refreshUserState();

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

            binding.meFragmentTvUserName.setText(userName);
            binding.meFragmentTvUserEmail.setText(userEmail);
            binding.meFragmentTvUserEmail.setVisibility(View.VISIBLE);

            viewModel.fetchUserInfo(userEmail);

            binding.meFragmentTvCourseCount.setText(String.valueOf(userStorage.getCourseCount()));
            binding.meFragmentTvStudyTime.setText(userStorage.getStudyTime());
            binding.meFragmentTvCertificateCount.setText(String.valueOf(userStorage.getCertificateCount()));

            String avatarUrl = "https://robohash.org/" + userName + ".png";
            Glide.with(this)
                    .load(avatarUrl)
                    .into(binding.meFragmentIvAvatar);
        } else {
            binding.meFragmentTvUserName.setText("请点击登录");
            binding.meFragmentTvUserEmail.setVisibility(View.GONE);
            binding.meFragmentIvAvatar.setImageDrawable(new ColorDrawable(Color.parseColor("#CCCCCC")));

            binding.meFragmentTvCourseCount.setText("0");
            binding.meFragmentTvStudyTime.setText("0h");
            binding.meFragmentTvCertificateCount.setText("0");
        }
    }

    private void initObservers() {
        viewModel.getUserInfoResult().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                userStorage.saveUserStats(user.getCourse_count(), user.getStudy_time(), user.getCertificate_count());

                binding.meFragmentTvCourseCount.setText(String.valueOf(user.getCourse_count()));
                binding.meFragmentTvStudyTime.setText(user.getStudy_time());
                binding.meFragmentTvCertificateCount.setText(String.valueOf(user.getCertificate_count()));
            }
        });
    }

    private void initListener() {
        binding.meFragmentClUserProfile.setOnClickListener(v -> {
            if (!isLogin) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_me_to_loginFragment);
            } else {
                Navigation.findNavController(v).navigate(R.id.action_navigation_me_to_accountProfileFragment);
            }
        });

        binding.meFragmentLlAccountProfile.setOnClickListener(v -> {
            if (checkLogin(v)) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_me_to_accountProfileFragment);
            }
        });

        binding.meFragmentLlSecurityPrivacy.setOnClickListener(v -> {
            if (checkLogin(v)) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_me_to_securityPrivacyFragment);
            }
        });

        binding.meFragmentLlShippingAddress.setOnClickListener(v -> {
            if (checkLogin(v)) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_me_to_addressListFragment);
            }
        });

        binding.meFragmentLlFavorite.setOnClickListener(v -> {
            if (checkLogin(v)) {
                showDevPopup();
            }
        });

        binding.meFragmentLlHistory.setOnClickListener(v -> {
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