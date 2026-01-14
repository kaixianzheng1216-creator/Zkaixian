# Android 期末项目答辩文档 - Zkaixian (智凯线) IT 在线教育平台

## 1. 项目概况

### 1.1 项目简介
**项目名称**：Zkaixian (智凯线)
**项目定位**：一款专注于 IT 垂直领域的在线教育与资讯聚合平台。它为用户提供了一站式的学习环境，涵盖了技术资讯阅读、在线视频课程学习、个人学习数据统计以及个人信息管理等核心功能。

### 1.2 技术架构
本项目采用 Google 推荐的现代 Android 开发架构，确保代码的健壮性与可维护性：
*   **MVVM 架构**：通过 `ViewModel` 和 `LiveData` 实现 UI 与数据的分离，生命周期感知，避免内存泄漏。
*   **网络通信**：使用 `Retrofit 2` + `OkHttp 3` + `Gson`，实现 RESTful API 的高效调用与 JSON 解析。
*   **图片加载**：集成 `Glide 4`，支持图片缓存、圆角处理和占位图显示。
*   **组件化**：使用了 `Jetpack Navigation` 进行单 Activity 多 Fragment 的导航管理。

### 1.3 第三方核心库
*   **图表**：`MPAndroidChart` (数据可视化)
*   **视频**：`GSYVideoPlayer` (基于 IJKPlayer 的视频播放器)
*   **地图**：`AMap` (高德地图 SDK)
*   **UI 交互**：`BoomMenu` (爆炸菜单), `SmartRefreshLayout` (智能刷新), `XPopup` (通用弹窗), `AgentWeb` (Web 容器)

---

## 2. 启动流程与主页模块

### 2.1 闪屏页 (SplashScreen)
**设计思路**：
遵循 Google 最新的 Material Design 规范，使用 Android 12+ 引入的 SplashScreen API 实现无缝启动体验，提升应用的专业感。
**实现细节**：
*   **主题配置**：在 `themes.xml` 中定义 `Theme.App.Starting`，设置启动背景色、图标以及动画时长。指定 `postSplashScreenTheme` 为应用主主题，确保启动结束后自动切换。
*   **代码调用**：在 `MainActivity` 的 `onCreate` 中调用 `installSplashScreen()`，系统会自动处理等待逻辑。

**核心代码**：
```xml
<!-- themes.xml -->
<style name="Theme.App.Starting" parent="Theme.SplashScreen">
    <item name="windowSplashScreenBackground">@color/white</item>
    <item name="windowSplashScreenAnimatedIcon">@drawable/icon</item>
    <item name="postSplashScreenTheme">@style/Theme.Zkaixian</item>
</style>
```
```java
// MainActivity.java
@Override
protected void onCreate(Bundle savedInstanceState) {
    // 安装 SplashScreen，必须在 super.onCreate 之前调用
    SplashScreen.installSplashScreen(this);
    super.onCreate(savedInstanceState);
    // ...
}
```

### 2.2 启动引导页 (IntroActivity)
**设计思路**：
在用户首次安装运行时展示，通过图文并茂的方式介绍 App 的核心亮点（如 MVVM 架构、地图功能等），帮助用户快速上手。
**实现细节**：
继承自 `AppIntro` 库，简化了 ViewPager 的搭建。在 `onCreate` 中添加多个 `AppIntroFragment` 实例，每个实例对应一页介绍。用户点击“完成”后，将标志位写入 `SharedPreferences`，下次启动不再显示。

**核心代码**：
```java
// IntroActivity.java
// 添加引导页 Slide，配置标题、描述、图片和背景色
addSlide(AppIntroFragment.createInstance("第一日", "启动页与基础框架", R.drawable.day1, ...));
addSlide(AppIntroFragment.createInstance("第二日", "LiveData + Retrofit", R.drawable.day2, ...));

// 完成引导后的处理
private void completeIntro() {
    // 标记为非首次运行
    getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
    // 跳转至主页
    startActivity(new Intent(this, MainActivity.class));
    finish();
}
```

### 2.3 启动广告页 (AdActivity)
**设计思路**：
在进入主页前展示 5 秒开屏广告，兼顾商业价值与用户体验，提供“跳过”按钮以尊重用户选择。
**实现细节**：
使用 `Glide` 加载网络广告图。利用 Android 原生 `CountDownTimer` 实现倒计时逻辑。倒计时结束或用户点击“跳过”按钮时，跳转至主页并销毁当前页面，防止用户按返回键回到广告页。

**核心代码**：
```java
// AdActivity.java
// 初始化倒计时：总时长 5 秒，间隔 1 秒
countDownTimer = new CountDownTimer(5000, 1000) {
    @Override
    public void onTick(long millisUntilFinished) {
        // 更新 UI 显示剩余秒数
        btnSkip.setText("跳过 " + (millisUntilFinished / 1000 + 1) + "s");
    }
    @Override
    public void onFinish() {
        startMainActivity(); // 倒计时结束自动跳转
    }
};
countDownTimer.start();
```

### 2.4 主界面容器 (MainActivity)
**设计思路**：
作为整个应用的容器和导航中枢，负责管理 Fragment 的路由分发和底部导航栏的切换。
**实现细节**：
在 `onCreate` 中首先检查是否首次运行（跳转引导页）或是否需要展示广告（跳转广告页）。如果不需要，则初始化 `BottomNavigationView` 并与 `NavController` 绑定，实现底部导航栏的切换逻辑。同时处理返回键逻辑，防止误触退出。

**核心代码**：
```java
// MainActivity.java
if (checkAndHandleFirstRun()) return; // 检查首次运行
if (checkAndHandleAd()) return;       // 检查广告展示

// 初始化导航
NavigationUI.setupWithNavController(binding.mainActivityNavView, navController);

// 退出拦截
getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
    @Override
    public void handleOnBackPressed() {
        if (!navController.navigateUp()) { // 如果当前 Fragment 无法返回
            new XPopup.Builder(MainActivity.this)
                    .asConfirm("提示", "确定要退出程序吗？", ...).show();
        }
    }
});
```

### 2.5 主页面 (HomeFragment)
**设计思路**：
作为应用的核心入口，采用垂直滚动布局。顶部为自动轮播的 Banner 展示重要活动，中间为快捷功能入口，下方为实时资讯列表。
**实现细节**：
*   **MVVM 数据驱动**：通过 `observeViewModel` 监听 `HomeViewModel` 中的 `LiveData` 数据变化。当 `ads` 或 `news` 数据更新时，自动刷新 Banner 和 RecyclerView，实现 UI 与逻辑的解耦。
*   **下拉刷新**：集成 `SmartRefreshLayout`，配合 `ClassicsHeader` 实现经典的下拉刷新效果。

**核心代码**：
```java
// HomeFragment.java
private void observeViewModel() {
    // 观察广告数据变化
    homeViewModel.getAdList().observe(getViewLifecycleOwner(), ads -> {
        if (ads != null) {
            banner.addBannerLifecycleObserver(this)
                    .setAdapter(new ImageTitleBannerAdapter(ads));
        }
    });

    // 观察新闻列表数据变化
    homeViewModel.getNewsList().observe(getViewLifecycleOwner(), news -> {
        if (news != null) {
            // 更新 RecyclerView 适配器数据
            homeAdapter.setList(news); 
        }
    });
}
```

### 2.6 课程分类页 (CourseFragment)
**设计思路**：
通过复用同一个 Fragment 来展示不同类型的课程（如算法、实战、开源项目），提高代码复用率，降低维护成本。
**实现细节**：
通过 `Navigation` 传递参数 `course_type`。在 `loadData` 中根据参数动态设置页面标题，并请求对应的 API 接口加载数据。

**核心代码**：
```java
// CourseFragment.java
private void loadData() {
    // 获取传递的参数
    int courseType = getArguments().getInt("course_type");
    
    // 根据类型设置标题
    updateTitle(courseType); 
    
    // 请求对应类型的课程数据
    courseViewModel.fetchCourses(courseType); 
}
```

### 2.7 资讯详情页 (WebFragment)
**设计思路**：
提供一个内置的 Web 浏览器来加载新闻详情，支持沉浸式阅读体验，避免跳转到外部浏览器打断用户流程。
**实现细节**：
使用 `AgentWeb` 库替代原生 WebView。它封装了进度条、JS 交互和 WebChromeClient，极大简化了 Web 页面的加载逻辑，并自动处理了生命周期管理。

**核心代码**：
```java
// WebFragment.java
mAgentWeb = AgentWeb.with(this)
        .setAgentWebParent(root, new ViewGroup.LayoutParams(-1, -1)) // 填满父布局
        .useDefaultIndicator() // 使用默认进度条
        .createAgentWeb()
        .ready()
        .go(getArguments().getString("url")); // 加载传入的 URL

// 生命周期绑定，防止内存泄漏
@Override
public void onPause() {
    mAgentWeb.getWebLifeCycle().onPause();
    super.onPause();
}
```

---

## 3. 图表模块 (Chart)

### 3.1 图表导航页 (ChartFragment)
**设计思路**：
打破传统的列表导航，使用全屏背景 + 右下角悬浮按钮的设计，通过趣味性的交互提升用户探索欲。
**实现细节**：
集成 `BoomMenu` 库，配置为“爆炸展开”效果。构建多个 `TextOutsideCircleButton` 子菜单，分别对应柱状图、折线图和饼图，点击后通过 Navigation 跳转。

**核心代码**：
```java
// ChartFragment.java
// 构建子菜单项
TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
        .normalImageRes(boomList.get(i).getImageId())
        .normalText(boomList.get(i).getTitle())
        .listener(index -> {
            // 根据索引跳转不同图表页
            switch (index) {
                case 0: Navigation.findNavController(root).navigate(R.id.action_to_barFragment); break;
                case 1: Navigation.findNavController(root).navigate(R.id.action_to_lineFragment); break;
                case 2: Navigation.findNavController(root).navigate(R.id.action_to_pieFragment); break;
            }
        });
bmb.addBuilder(builder);
```

### 3.2 柱状图页 (BarFragment)
**设计思路**：
使用柱状图直观展示“编程语言热度”排行，便于用户进行横向对比。
**实现细节**：
使用 `MPAndroidChart` 的 `BarChart`。通过 `IndexAxisValueFormatter` 自定义 X 轴标签（显示语言名称）。配置 `animateY(1000)` 实现 Y 轴升起动画，提升视觉体验。

**核心代码**：
```java
// BarFragment.java
barViewModel.getData().observe(getViewLifecycleOwner(), data -> {
    ArrayList<BarEntry> entries = new ArrayList<>();
    String[] xAxisLabels = new String[data.size()];

    for (int i = 0; i < data.size(); i++) {
        entries.add(new BarEntry(i, (float) data.get(i).getValue()));
        xAxisLabels[i] = data.get(i).getName();
    }

    // 自定义 X 轴标签
    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));

    BarDataSet barDataSet = new BarDataSet(entries, "编程语言热度");
    barDataSet.setColors(ChartStyleUtils.COLORS); // 设置多彩配色

    BarData barData = new BarData(barDataSet);
    barChart.setData(barData);
    barChart.animateY(1000); // 播放 Y 轴入场动画
    barChart.invalidate();
});
```

### 3.3 折线图页 (LineFragment)
**设计思路**：
使用折线图展示“新增用户数”的趋势变化，帮助用户了解平台的发展态势。
**实现细节**：
*   **曲线平滑**：设置 `Mode.CUBIC_BEZIER` 启用贝塞尔曲线模式，使折线更加圆润自然。
*   **视觉增强**：开启 `setDrawFilled` 并配置主题色填充 (`setFillColor`) 及透明度 (`setFillAlpha`)，增强图表的视觉层次感。

**核心代码**：
```java
// LineFragment.java
LineDataSet lineDataSet = new LineDataSet(entryList, "新增用户数");
// 启用贝塞尔曲线模式
lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
// 开启区域填充
lineDataSet.setDrawFilled(true);
lineDataSet.setFillColor(ChartStyleUtils.COLOR_PRIMARY);
lineDataSet.setFillAlpha(50); // 半透明填充

// 样式微调
lineDataSet.setLineWidth(2f);
lineDataSet.setCircleRadius(4f);
lineDataSet.setDrawCircleHole(false);

LineData lineData = new LineData(lineDataSet);
lineChart.setData(lineData);
lineChart.animateY(1000); // 启动动画
```

### 3.4 饼图页 (PieFragment)
**设计思路**：
使用饼图展示“技术栈分布”的占比情况，清晰呈现各部分的比例关系。
**实现细节**：
*   **数据格式化**：使用 `PercentFormatter` 自动将数值转换为百分比格式。
*   **布局优化**：将数值显示在扇区外部 (`OUTSIDE_SLICE`) 以防拥挤；调整 `Legend` (图例) 至底部居中，避免遮挡图表主体。

**核心代码**：
```java
// PieFragment.java
PieDataSet dataSet = new PieDataSet(entries, "技术栈分布占比");
dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); // 数值显示在外部
dataSet.setSliceSpace(2f); // 扇区通过间隙

PieData pieData = new PieData(dataSet);
pieData.setValueFormatter(new PercentFormatter(pieChart)); // 百分比格式化

// 图例配置
Legend l = chart.getLegend();
l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); // 底部对齐
l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // 水平居中
l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
```

---

## 4. 视频模块 (Video)

### 4.1 视频列表页 (VideoFragment)
**设计思路**：
采用列表形式高效展示视频课程，每项包含封面、标题和简介，支持快速浏览。
**实现细节**：
使用 `RecyclerView` + `BaseRecyclerViewAdapterHelper`。设置点击监听器，点击列表项时将 `Video` 对象序列化并通过 Bundle 传递给详情页，实现数据的跨页面传递。

**核心代码**：
```java
// VideoFragment.java
private void initRecyclerView() {
    videoAdapter = new VideoAdapter(new ArrayList<>());
    // 设置点击事件监听
    videoAdapter.setOnItemClickListener((adapter, view, position) -> {
        Video clickedVideo = (Video) adapter.getItem(position);
        if (clickedVideo != null) {
            // 通过 Bundle 传递序列化对象
            Bundle bundle = new Bundle();
            bundle.putSerializable("video_data", clickedVideo);
            // 导航跳转到详情页
            Navigation.findNavController(view)
                    .navigate(R.id.action_navigation_video_to_videoDetailFragment, bundle);
        }
    });
    binding.videoFragmentRvList.setAdapter(videoAdapter);
}
```

### 4.2 视频详情页 (VideoDetailFragment)
**设计思路**：
集播放与介绍于一体。顶部为视频播放窗口，下方使用 `TabLayout` 切换“简介”和“选集”，提供完整的课程学习体验。
**实现细节**：
核心组件为 `GSYVideoPlayer`。
*   **功能配置**：通过 `GSYVideoOptionBuilder` 进行链式配置，开启手势控制、设置封面图、配置全屏/旋转回调。
*   **生命周期**：在 `onPrepared` 回调中启用屏幕旋转逻辑。

**核心代码**：
```java
// VideoDetailFragment.java
new GSYVideoOptionBuilder()
        .setUrl(url)
        .setVideoTitle(title)
        .setThumbImageView(imageView) // 设置封面图
        .setIsTouchWiget(true) // 开启手势触摸调节音量亮度
        .setRotateViewAuto(false) // 关闭自动旋转，配合 OrientationUtils 使用
        .setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                orientationUtils.setEnable(true); // 视频加载完毕后，允许旋转屏幕
                isPlay = true;
            }
            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo(); // 退出全屏时切回竖屏
                }
            }
        })
        .build(detailPlayer);
```

---

## 5. 我的模块 (Me)

### 5.1 个人中心页 (MeFragment)
**设计思路**：
作为用户信息的展示中心，根据用户的登录状态动态展示 UI（已登录显示头像昵称，未登录显示提示），引导用户进行身份认证。
**实现细节**：
在 `onResume` 中检查 `UserStorage` (SharedPreferences) 中的登录标志位。
*   **已登录**：显示头像、昵称、邮箱及学习数据。
*   **未登录**：显示默认灰色头像，提示“请点击登录”。

**核心代码**：
```java
// MeFragment.java
private void refreshUserState() {
    isLogin = userStorage.isLogin();
    if (isLogin) {
        // 已登录：加载用户信息
        binding.meFragmentTvUserName.setText(userStorage.getUserName());
        Glide.with(this).load(avatarUrl).into(binding.meFragmentIvAvatar);
    } else {
        // 未登录：恢复默认状态
        binding.meFragmentTvUserName.setText("请点击登录");
        binding.meFragmentIvAvatar.setImageDrawable(defaultDrawable);
    }
}
```

### 5.2 注册页 (RegisterFragment)
**设计思路**：
提供完整的注册流程，包含邮箱验证码发送和表单提交，确保注册用户的真实性。
**实现细节**：
*   **交互闭环**：通过 `LiveData` 监听验证码发送状态 (`getSendCodeResult`) 和注册结果 (`getRegisterResult`)。
*   **用户反馈**：使用 `XPopup` 弹窗展示加载中、成功或失败的提示，提升用户体验。

**核心代码**：
```java
// RegisterFragment.java
private void initObservers() {
    // 监听验证码发送结果
    viewModel.getSendCodeResult().observe(getViewLifecycleOwner(), success -> {
        if (success) {
            new XPopup.Builder(requireContext()).asLoading("验证码发送成功").show().delayDismiss(1500);
        }
    });

    // 监听注册结果
    viewModel.getRegisterResult().observe(getViewLifecycleOwner(), user -> {
        if (user != null) {
            // 注册成功，自动保存用户信息
            userStorage.saveUserInfo(user.getUsername(), user.getEmail());
            new XPopup.Builder(requireContext())
                    .asLoading("注册成功，已自动登录")
                    .show()
                    .delayDismiss(800);
            Navigation.findNavController(binding.getRoot()).navigateUp();
        }
    });
}
```

### 5.3 登录页 (LoginFragment)
**设计思路**：
提供简洁的登录表单，支持跳转注册和找回密码，构建完整的账号体系闭环。
**实现细节**：
*   **登录触发**：点击按钮调用 `handleLogin`，校验输入后调用 `ViewModel` 进行登录。
*   **状态监听**：通过 `initObservers` 监听登录结果。成功时保存用户信息并弹窗提示，随后返回上一页；失败时弹出错误提示。

**核心代码**：
```java
// LoginFragment.java
private void initObservers() {
    // 监听登录结果
    viewModel.getLoginResult().observe(getViewLifecycleOwner(), user -> {
        if (user != null) {
            // 保存用户信息到 SharedPreferences
            userStorage.saveUserInfo(user.getUsername(), user.getEmail());
            new XPopup.Builder(requireContext())
                    .dismissOnTouchOutside(false)
                    .setPopupCallback(new SimpleCallback() {
                        @Override
                        public void onDismiss(BasePopupView popupView) {
                            Navigation.findNavController(binding.getRoot()).navigateUp(); // 返回上一页
                        }
                    })
                    .asLoading("登录成功")
                    .show()
                    .delayDismiss(800);
        }
    });

    // 监听错误信息
    viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
        new XPopup.Builder(requireContext())
                .asConfirm("提示", errorMsg, null, "确定", null, null, true)
                .show();
    });
}
```

### 5.4 忘记密码页 (ForgetPasswordFragment)
**设计思路**：
通过验证码找回密码，保障账号安全，解决用户忘记密码的痛点。
**实现细节**：
重置密码成功后，为了安全起见，强制清除本地登录状态 (`userStorage.setLogin(false)`)，并引导用户重新登录。

**核心代码**：
```java
// ForgetPasswordFragment.java
viewModel.getResetPasswordResult().observe(getViewLifecycleOwner(), success -> {
    if (success) {
        userStorage.setLogin(false); // 清除本地登录状态
        new XPopup.Builder(requireContext()).asLoading("密码重置成功，请重新登录").show();
        Navigation.findNavController(binding.getRoot()).navigateUp(); // 返回上一页
    }
});
```

### 5.5 个人资料页 (AccountProfileFragment)
**设计思路**：
允许用户修改昵称、简介等个人信息，或退出登录，赋予用户对自己账号的控制权。
**实现细节**：
退出登录时，调用 `userStorage.logout()` 清除所有本地用户信息，并弹出 Loading 弹窗提示。

**核心代码**：
```java
// AccountProfileFragment.java
private void performLogout(View v) {
    userStorage.logout(); // 清空 SharedPreferences
    new XPopup.Builder(requireContext()).asLoading("已退出登录").show();
    Navigation.findNavController(v).navigateUp(); // 返回
}
```

### 5.6 安全隐私页 (SecurityPrivacyFragment)
**设计思路**：
提供修改登录密码的入口，需要再次验证邮箱验证码，进一步增强账号安全性。
**实现细节**：
输入旧密码（或验证码）和新密码，调用后端接口更新密码。

**核心代码**：
```java
// SecurityPrivacyFragment.java
private void updatePassword(View v) {
    String code = binding.securityPrivacyFragmentEtVerifyCodeInput.getText().toString();
    String password = binding.securityPrivacyFragmentEtNewPasswordInput.getText().toString();
    // 调用 ViewModel 更新密码
    viewModel.updatePassword(userStorage.getEmail(), code, password);
}
```

### 5.7 地址列表页 (AddressListFragment)
**设计思路**：
展示用户的收货地址列表，支持长按删除操作，方便用户管理多个地址。
**实现细节**：
使用 `RecyclerView` 展示地址。为 Adapter 设置 `OnItemLongClickListener`，长按时弹出 `XPopup` 确认框，用户确认后调用删除接口。

**核心代码**：
```java
// AddressListFragment.java
adapter.setOnItemLongClickListener((adapter, view, position) -> {
    Address address = (Address) adapter.getItem(position);
    // 弹出确认框
    new XPopup.Builder(getContext())
            .asConfirm("提示", "确定要删除该地址吗？", "取消", "确定",
                    () -> viewModel.deleteAddress(address.getId()), null, false)
            .show();
    return true;
});
```

### 5.8 地址新增/编辑页 (AddressAddFragment)
**设计思路**：
复用同一个页面进行新增和编辑，减少开发工作量。支持跳转到搜索页面选择地址，提升输入效率。
**实现细节**：
*   **数据回填**：如果是编辑模式，初始化时填充已有数据。
*   **搜索结果接收**：使用 `setFragmentResultListener` 监听搜索页面返回的数据，自动填充地址栏。

**核心代码**：
```java
// AddressAddFragment.java
getParentFragmentManager().setFragmentResultListener("address_request", getViewLifecycleOwner(), (requestKey, result) -> {
    // 接收搜索页面返回的 AmapTip 对象
    AmapTip tip = (AmapTip) result.getSerializable("address_result");
    if (tip != null) {
        // 自动填充地址文本框
        binding.addressAddFragmentEtAddress.setText(tip.getDistrict() + tip.getAddress());
        binding.addressAddFragmentEtDetail.setText(tip.getName());
    }
});
```

### 5.9 地址搜索页 (AddressSearchFragment)
**设计思路**：
集成高德地图 SDK，实现“所见即所得”的地址搜索与定位功能。
**实现细节**：
*   **搜索联动**：监听 `ViewModel` 的搜索结果 `LiveData`，获取到数据后自动刷新列表并更新地图视角。
*   **地图更新**：解析 API 返回的经纬度，清除旧 Marker，添加新 Marker 并移动相机。

**核心代码**：
```java
// AddressSearchFragment.java
private void initObservers() {
    viewModel.getSearchResult().observe(getViewLifecycleOwner(), tips -> {
        if (!tips.isEmpty()) {
            adapter.setList(tips); // 刷新列表
            updateMap(tips.get(0)); // 地图定位到第一条结果
        }
    });
}

private void updateMap(AmapTip tip) {
    String[] loc = tip.getLocation().split(",");
    LatLng latLng = new LatLng(Double.parseDouble(loc[1]), Double.parseDouble(loc[0]));

    if (aMap != null) {
        aMap.clear(); // 清除地图上旧的标记
        aMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(tip.getName())
                .snippet(tip.getAddress()));
        // 移动相机视角并放大到 15 级
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}
```

---

## 6. 项目总结与展望

### 6.1 项目总结
本项目成功构建了一个功能完善的 Android 在线教育平台，通过实践验证了 **MVVM 架构** 在中型项目中的优越性。
*   **架构规范**：严格遵守 MVVM 分层设计，ViewModel 负责业务逻辑，Fragment 负责 UI 渲染，结构清晰，易于测试和维护。
*   **技术栈先进**：整合了 Jetpack 全家桶 (Lifecycle, ViewModel, LiveData, Navigation) 以及 Retrofit, Glide 等主流开源库，技术选型符合当前行业标准。
*   **用户体验良好**：细节打磨到位，如视频播放的手势控制、图表的动态动画、地图搜索的实时联动等，提供了流畅的交互体验。

### 6.2 未来展望
虽然目前项目已具备核心功能，但在以下方面仍有改进空间：
*   **深色模式支持**：进一步完善 UI 资源，适配 Android 系统的深色模式，提供更舒适的夜间使用体验。
*   **课程互动**：增加评论区、弹幕功能，增强用户与课程内容的互动性。
*   **离线缓存**：引入 Room 数据库实现更完善的数据缓存策略，支持视频离线下载观看。
