package com.example.zkaixian.ui.web;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.zkaixian.databinding.FragmentWebBinding;
import com.just.agentweb.AgentWeb;

public class WebFragment extends Fragment {
    AgentWeb mAgentWeb;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentWebBinding binding = FragmentWebBinding.inflate(inflater, container, false);

        ConstraintLayout root = binding.getRoot();

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(
                        root,
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        )
                )
                .useDefaultIndicator()
//                .useMiddlewareWebChrome(
//                        new WebChromeClient() {
//                            @Override
//                            public void onReceivedTitle(WebView webView, String title) {
//                                MainActivity activity = (MainActivity) getActivity();
//                                activity.getSupportActionBar().setTitle(title);
//                            }
//                        }
//                )
                .createAgentWeb()
                .ready()
                .go(getArguments().getString("url"));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                boolean back = mAgentWeb.back();

                if (!back) {
                    setEnabled(false);
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();

        super.onPause();

    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mAgentWeb.getWebLifeCycle().onDestroy();

        super.onDestroyView();
    }
}