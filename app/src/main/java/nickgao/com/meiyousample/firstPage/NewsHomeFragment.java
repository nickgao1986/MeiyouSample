package nickgao.com.meiyousample.firstPage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.HomeSlidingTabLayout;
import com.lingan.seeyou.ui.view.HomeSlidingTabStrip;
import com.lingan.seeyou.ui.view.skin.SkinManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fragment.PeriodBaseFragment;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.controller.NewsHomeController;
import nickgao.com.meiyousample.firstPage.view.ScrollableLayout;

/**
 * Created by gaoyoujian on 2017/3/15.
 */

public class NewsHomeFragment extends PeriodBaseFragment implements View.OnClickListener{
    private String TAG = "NewsHomeFragment";
    private Activity mActivity;
    private int homeStyleType;
    private int cat_list;
    //UI
    //private ViewGroup rootView;
    public ScrollableLayout news_home_scroll_layout;
    private RelativeLayout rl_news_home_sliding_tab;
    private HomeSlidingTabLayout news_home_sliding_tab;
    private NewsHomeViewPager news_home_viewpager;
    private RelativeLayout rlMoreClassify, rlHomeBack;
    private ImageView ivMoreClassify, ivHomeBack;
    private View view_gradient_trans_left, view_gradient_trans_right;
    //头部ui
    private ImageView ivBannerBg;
    private TextView home_title;
    private RelativeLayout rlHomeTitleBar;
    private RelativeLayout rlLeft;
    private RelativeLayout rlQian;

    //分类数据源
    private NewsHomePagerAdapter homePagerAdapter;
    private List<HomeClassifyModel> classifyModels = new ArrayList<>();
    private Map<Integer, Boolean> classifyFragmentsIsLoadingNetDatas = new HashMap<>();//保存所有tab下是不是要加载网络数据
    private Map<Integer, Integer> mRoundLists = new HashMap<>();
    //保存所有tab下刷新次数
    private int mCurrentPosition;//当前分类页面
    private boolean mIsPageChanged;//是否翻页了
    private boolean mIsHideFragment;
    private boolean bTopMenuShowed;
    private NewsHomeTabController newsHomeTabController;
    private View rootView;

    @Override
    protected int getLayout() {
        return R.layout.layout_news_home_main;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        initLogic();
        setLisenner();
    }


    /**
     * 初始化逻辑
     */
    private void initLogic() {
        try {
            NewsHomeController.getInstance().loadHomeCatIdCache(getActivity().getApplicationContext(), HomeType.RECOMMEND_ID);
            loadFragmentPager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载首页接口数据获取有几个分类数据
     */
    private void loadFragmentPager() {
        classifyModels.clear();
        classifyFragmentsIsLoadingNetDatas.clear();
        mRoundLists.clear();
        handleTabLoadingData();
        updateData();
        //updateViewPager();
    }


    /**
     * 默认每个tab加载网络数据
     */
    private void handleTabLoadingData() {
        if (classifyModels.size() == 0)
            return;
        classifyFragmentsIsLoadingNetDatas.clear();
        for (HomeClassifyModel classifyModel : classifyModels) {
            classifyFragmentsIsLoadingNetDatas.put(classifyModel.catid, true);
            mRoundLists.put(classifyModel.catid, 0);
        }
    }


    private void initUI() {
        getTitleBar().setCustomTitleBar(R.layout.layout_home_title);
        rootView = getRootView();
        news_home_viewpager = (NewsHomeViewPager) rootView.findViewById(R.id.news_home_viewpager);
        news_home_scroll_layout = (ScrollableLayout) rootView.findViewById(R.id.news_home_scroll_layout);
        rl_news_home_sliding_tab = (RelativeLayout) rootView.findViewById(R.id.rl_news_home_sliding_tab);
        news_home_sliding_tab = (HomeSlidingTabLayout) rootView.findViewById(R.id.news_home_sliding_tab);
        news_home_sliding_tab.setCustomTabView(R.layout.layout_home_classify_tab_item, R.id.homeTab);
        news_home_sliding_tab.setIsDrawDiver(false);

        home_title = (TextView) getActivity().findViewById(R.id.home_title);
        rlMoreClassify = (RelativeLayout) rootView.findViewById(R.id.rlMoreClassify);
        ivMoreClassify = (ImageView) rootView.findViewById(R.id.ivMoreClassify);
        view_gradient_trans_right = rootView.findViewById(R.id.view_gradient_trans_right);
        view_gradient_trans_left = rootView.findViewById(R.id.view_gradient_trans_left);
        rlHomeBack = (RelativeLayout) rootView.findViewById(R.id.rlHomeBack);
        ivHomeBack = (ImageView) rootView.findViewById(R.id.ivHomeBack);

        //titlebar
        rlHomeTitleBar = (RelativeLayout) rootView.findViewById(R.id.rlHomeTitleBar);
        rlLeft = (RelativeLayout) rootView.findViewById(R.id.rlLeft);
        rlQian = (RelativeLayout) rootView.findViewById(R.id.rlQian);
        initHeadUI();
        if (cat_list == HomeType.MEIYOU_MORE_TAB_SHOW) {
            rlMoreClassify.setVisibility(View.VISIBLE);
            view_gradient_trans_right.setVisibility(View.VISIBLE);
        } else {
            rlMoreClassify.setVisibility(View.GONE);
            view_gradient_trans_right.setVisibility(View.GONE);
        }

        updateUI();
    }

    /**
     * 初始化头部ui
     */
    private void initHeadUI() {
        //头部主要布局控件
        ivBannerBg = (ImageView) rootView.findViewById(R.id.ivBannerBg);//头部的背景
        ivBannerBg.setScaleType(ImageView.ScaleType.CENTER_CROP);

    }

    /**
     * 换肤问题
     */
    private void updateUI() {
//        SkinManager.getInstance().setDrawable(ivBannerBg, R.drawable.apk_first_banner);
//        int[] colors = new int[]{ColorUtils.getColorWithAlpha(SkinManager.getInstance().getAdapterColor(R.color.white_an), 0.0F), ColorUtils.getColorWithAlpha(SkinManager.getInstance().getAdapterColor(R.color.white_an), 1.0F)};
//        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
//        view_gradient_trans_left.setBackgroundDrawable(gradientDrawable);
//
//        GradientDrawable gradientDrawableRight = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
//        view_gradient_trans_right.setBackgroundDrawable(gradientDrawableRight);
    }


    private void setLisenner() {
        rlMoreClassify.setOnClickListener(this);
        ivMoreClassify.setOnClickListener(this);
        rlHomeBack.setOnClickListener(this);
        ivHomeBack.setOnClickListener(this);
        view_gradient_trans_left.setOnClickListener(this);
        news_home_sliding_tab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
//                if (mCurrentPosition != i) {
//                    mIsPageChanged = true;
//                }
//                NewsHomeController.getInstance().onFragmentStop(getActivity().getApplicationContext(), classifyModels.get(mCurrentPosition).catid);//ga统计tab时长事件
//                mCurrentPosition = i;
//                NewsHomeController.getInstance().onFragmentStart(getActivity().getApplicationContext(), classifyModels.get(mCurrentPosition).catid);//ga统计tab时长事件
//                if (homePagerAdapter != null) {
//                    homePagerAdapter.changeCurrentSelectPage(mCurrentPosition);
//                }
//                NewsHomeController.getInstance().updateClassifySelect(classifyModels, mCurrentPosition);
//
//                EventBus.getDefault().post(new NewsHomeSelectedEvent());
//                if (mCurrentPosition < classifyModels.size()) {//友盟统计事件
//                    EventsUtils.getInstance().countEvent(getActivity().getApplicationContext(), "home-dhlcx", EventsUtils.FROM, classifyModels.get(mCurrentPosition).getName());
//                }
//                setCurrentScrollableContainer();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
//                if (mIsPageChanged && i == 0) {
//                    EventBus.getDefault().post(new NewsHomePageSelectedEvent(mCurrentPosition));
//                    mIsPageChanged = false;
//                    setCurrentScrollableContainer();
//                }
            }
        });

        news_home_sliding_tab.setTabOnClickListener(new HomeSlidingTabLayout.onItemClick() {
            @Override
            public void onSameItemClickListener(int position) {
                //点击同一个分类刷新
//                pressTabRefrsh(position, true, false, false);
//                HomeRecommendCacheController.getInstance().handlePostYoumentSysxTime(classifyModels.get(mCurrentPosition).catid, "点击导航栏标签");
            }

            @Override
            public void onItemClickListener(int position) {
//                setCurrentScrollableContainer();
//                //点击其他的tab
//                if (!news_home_scroll_layout.isSticked()) {//不吸顶的时候要回原味
//                    pressOtherTab(position);
//                }
//                if (position < classifyModels.size()) {//友盟统计事件
//                    HomeClassifyModel model = classifyModels.get(position);
//                    String fromName = model.catid == HomeType.CITY_ID ? "本地" : model.getName();
//                    EventsUtils.getInstance().countEvent(getActivity().getApplicationContext(), "home-dhl", EventsUtils.FROM, fromName);
//                }
            }
        });
        news_home_scroll_layout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                NewsHomeController.getInstance().setCurrentScrollY(currentY);
                if (ivBannerBg != null) {//首页滑动titlebar跟tab的交互
                    int halfHeight = maxY / 2;
                    NewsHomeController.getInstance().handleAlpha(getActivity(), news_home_scroll_layout, rl_news_home_sliding_tab, rlHomeTitleBar, rlLeft, rlQian, home_title, halfHeight);
                }
                if (news_home_scroll_layout.isSticked()) {
//                    if (DataSaveHelper.getInstance(getActivity().getApplicationContext()).isShowHomeGuideDialog()) {
//                        NewsHomeController.getInstance().showHomeGuideDialog(getActivity());
//                    }
                //    homeMsgHelper.isShowMsgBox((PeriodBaseActivity) getActivity());
                } else {
                //    homeMsgHelper.hideWmMessageBox((PeriodBaseActivity) getActivity());
                }
            }
        });

        news_home_viewpager.setOnCallBackLisenter(new OnCallBackListener() {
            @Override
            public void OnCallBack(Object o) {
                pressBack();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlMoreClassify://更多tab展示
            case R.id.ivMoreClassify:
                newsHomeTabController = new NewsHomeTabController(getActivity(), classifyModels, bTopMenuShowed);
                newsHomeTabController.handleCategoryMenu(new OnCallBackListener() {
                    @Override
                    public void OnCallBack(Object object) {
                        bTopMenuShowed = (boolean) object;
                    }
                });
                break;
            case R.id.rlHomeBack://点击左边<吸顶回退
            case R.id.ivHomeBack:
            case R.id.view_gradient_trans_left:
                pressBack();
                break;
        }
    }


    /**
     * 点击左上角的返回键
     */
    public void pressBack() {
        if (newsHomeTabController != null && newsHomeTabController.bTopMenuShowed) {
            newsHomeTabController.hide();
        }
        if (news_home_scroll_layout != null && news_home_scroll_layout.isSticked()) {
            news_home_scroll_layout.setScrollBy();
        }
//        if (homePagerAdapter != null) {
//            NewsHomeClassifyFragment classifyFragment = homePagerAdapter.getPositionFragment();
//            if (classifyFragment != null) {
//                classifyFragment.pressBack();
//            }
//        }
    }

    private void updateData() {
        List<HomeClassifyModel> models = getClassifyModels();
        classifyModels.clear();
        classifyFragmentsIsLoadingNetDatas.clear();
        mRoundLists.clear();
        if (models != null && models.size() > 0) {//有缓存数据
            classifyModels.addAll(models);
        }
        handleTabLoadingData();
        updateViewPager();
    }

    /**
     * 更新viewpager
     */
    private void updateViewPager() {
        try {
            NewsHomeController.getInstance().setClassifySelect(mCurrentPosition);
            if (homePagerAdapter == null) {
                homePagerAdapter = new NewsHomePagerAdapter(getChildFragmentManager(), classifyModels, classifyFragmentsIsLoadingNetDatas, mRoundLists);

                news_home_viewpager.setAdapter(homePagerAdapter);
            } else {
//                homePagerAdapter.setLoadingNetData(classifyFragmentsIsLoadingNetDatas);
//                homePagerAdapter.setRoundData(mRoundLists);
                homePagerAdapter.notifyDataSetChanged();
            }
            news_home_sliding_tab.setViewPager(news_home_viewpager);
            news_home_viewpager.setCurrentItem(mCurrentPosition);
          //  NewsHomeController.getInstance().handleSelectClassifsModels(classifyModels, mCurrentPosition);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //很重要
                   // news_home_scroll_layout.getHelper().setCurrentScrollableContainer(homePagerAdapter.getPositionFragment());
                }
            }, 500);
            setTabSelect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 选中tab的颜色变化
     */
    private void setTabSelect() {
        try {
            HomeSlidingTabStrip tabStrip = (HomeSlidingTabStrip) news_home_sliding_tab.getChildAt(0);
            if (tabStrip != null) {
                TextView textView = (TextView) tabStrip.getChildAt(mCurrentPosition);
                if (textView != null) {
                    if (textView != null) {
                        SkinManager.getInstance().setTextColor(textView, R.color.red_b);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_19));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEventMainThread(NewsHomeSucessEvent event) {
        if (classifyFragmentsIsLoadingNetDatas.size() == 0)
            return;
        classifyFragmentsIsLoadingNetDatas.put(event.classifyId, false);
    }


    public void handleHideFragment() {

    }

    public void handleShowFragment() {

    }

    public void updateFragment(boolean isSamePage, boolean isFromNotify) {
        if (isSamePage) {//点击同一个tab
            if (newsHomeTabController != null && newsHomeTabController.bTopMenuShowed) {
                newsHomeTabController.hide();
            }
        }
    }


    private  List<HomeClassifyModel> getClassifyModels() {
        List<HomeClassifyModel> models = new ArrayList<HomeClassifyModel>();
        HomeClassifyModel model = new HomeClassifyModel();
        model.name = "推荐";
        model.isSelect = false;
        model.catid = 1;
        models.add(model);

        model = new HomeClassifyModel();
        model.name = "她她圈";
        model.isSelect = false;
        model.catid = 6;
        models.add(model);

        model = new HomeClassifyModel();
        model.name = "视频";
        model.isSelect = false;
        model.catid = 4;
        models.add(model);

        model = new HomeClassifyModel();
        model.name = "情感";
        model.isSelect = false;
        model.catid = 3;
        models.add(model);

        model = new HomeClassifyModel();
        model.name = "娱乐";
        model.isSelect = false;
        model.catid = 9;
        models.add(model);

        model = new HomeClassifyModel();
        model.name = "搞笑";
        model.isSelect = false;
        model.catid = 11;
        models.add(model);

        model = new HomeClassifyModel();
        model.name = "厦门";
        model.isSelect = false;
        model.catid = 2;
        models.add(model);

        model = new HomeClassifyModel();
        model.name = "健康";
        model.isSelect = false;
        model.catid = 15;
        models.add(model);
        return models;
    }

}
