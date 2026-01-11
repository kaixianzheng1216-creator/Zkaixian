package com.example.zkaixian.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserStorage {
    private static final String PREF_NAME = "user_data";
    private static final String KEY_IS_LOGIN = "is_login";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_BIO = "user_bio";

    private final SharedPreferences sp;

    public UserStorage(Context context) {
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLogin(boolean isLogin) {
        sp.edit().putBoolean(KEY_IS_LOGIN, isLogin).apply();
    }

    public boolean isLogin() {
        return sp.getBoolean(KEY_IS_LOGIN, false);
    }

    public void saveUserInfo(String name, String email) {
        sp.edit()
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_EMAIL, email)
                .putBoolean(KEY_IS_LOGIN, true)
                .apply();
    }

    public void updateProfile(String name, String bio) {
        sp.edit()
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_BIO, bio)
                .apply();
    }

    public String getUserName() {
        return sp.getString(KEY_USER_NAME, "user");
    }

    public String getEmail() {
        return sp.getString(KEY_USER_EMAIL, "user@example.com");
    }

    public String getBio() {
        return sp.getString(KEY_USER_BIO, "user bio");
    }

    public void logout() {
        sp.edit().clear().apply(); // 或者只设为 false
    }
}