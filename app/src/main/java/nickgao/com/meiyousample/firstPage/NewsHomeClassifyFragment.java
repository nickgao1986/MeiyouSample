package nickgao.com.meiyousample.firstPage;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.LoadingView;
import com.lingan.seeyou.ui.view.NewsHomeParallaxListview;
import com.lingan.seeyou.ui.view.ScrollableHelper;
import com.lingan.seeyou.ui.view.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/4/24.
 */

public class NewsHomeClassifyFragment extends Fragment implements View.OnClickListener,ScrollableHelper.ScrollableContainer {

    private View rootView;
    private final String TAG = "NewsHomeClassifyFragment";
    private LayoutInflater layoutInflater;
    private View topicFooter;
    private NewsHomeParallaxListview mListview;
    private LoadingView loadingView;
    private View mListViewHeader;
    private RelativeLayout rlSelectCity, rlManSelectCity;
    private TextView tvSelectCity;
    private ScrollableLayout news_home_scroll_layout;
    private ImageView ivBannerBg;
    private RelativeLayout rl_loadding, rl_update;
    private int classifyId, position;
    private String classifyName;//tab分类名字
    private int mSelectedPageNumber; //当前选中页
    private boolean isTopicLoading;//是否正在加载
    private boolean mLastADNetUseful = true;//广告网络状态是否可用，先默认可以用

    private List<TalkModel> recommendTopicList = new ArrayList<>();//数据源
    private BaseAdapter classifyAdapter;
    private BaseAdapter classifyAdAdapter;

    private boolean mIsDataLoadOver = false;//是否业务数据已加载
    private boolean mIsPageSelectLoad = false;//是否请求过广告

    private int mLvCurrentPosition;//上次的位置
    private float mLastY;//上次滚动的位置
    private boolean isSocll; // 用于统计是否滚动了
    private String cityId;//城市id
    private String onlyVideoId;//唯一值
    private boolean isLoadingNetWokeData = true;//是否要加载网络数据

    //开关值
    private int homeShowStyle;//首页样式
    private int feedsImageType;//图片样式
    private int feedbackButton;//反馈开关
    private int feedsIconViewType;//底部icon是不是要显示
    private boolean isVisible;//当前fragment是否可见
    private int first_requst;
    private int round;//当前刷新次数
    private Handler mHandler = new Handler();



    public static NewsHomeClassifyFragment newInstance(int classifyId, String classifyName, int position, int currentSelectedPage, boolean isLoadingNetWokeData, int round) {
        NewsHomeClassifyFragment classifyFragment = new NewsHomeClassifyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("classifyId", classifyId);
        bundle.putString("classifyName", classifyName);
        bundle.putInt("position", position);
        bundle.putInt("currentSelectedPage", currentSelectedPage);
        bundle.putBoolean("isLoadingNetWokeData", isLoadingNetWokeData);
        bundle.putInt("round", round);
        classifyFragment.setArguments(bundle);
        return classifyFragment;
    }

    public NewsHomeClassifyFragment() {

    }

    @Override
    public View getScrollableView() {
        return mListview;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_new_home_classify_fragment,null);
        return rootView;    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    private void init() {
        Bundle bundle = getArguments();
        classifyId = bundle.getInt("classifyId");
        classifyName = bundle.getString("classifyName");
        position = bundle.getInt("position");
        mSelectedPageNumber = bundle.getInt("currentSelectedPage");
        isLoadingNetWokeData = bundle.getBoolean("isLoadingNetWokeData");
        round = bundle.getInt("round");

//        onlyVideoId = getClass().getSimpleName() + "_" + System.currentTimeMillis() + "_" + Math.random();
//        cityId = DataSaveHelper.getInstance(getActivity().getApplicationContext()).getHomeCityId();
//        homeShowStyle = DataSaveHelper.getInstance(getActivity().getApplicationContext()).getHomeShowStyle();
//        feedsImageType = DoorPeriodController.getInstance().getFeedsImageType(getActivity());
//        feedbackButton = DataSaveHelper.getInstance(getActivity()).getHomeFeedbackButton();
//        feedsIconViewType = DoorPeriodController.getInstance().getFeedsIconViewType(getActivity().getApplicationContext());
//        first_requst = DataSaveHelper.getInstance(getActivity().getApplicationContext()).getFirstRequest();
//        if (first_requst == 1) {//第一次请求数据或者切换身份之后重新来
//            resetRecommendCache();
//            //切换到孕期，重置翻转属性s
//            if (CalendarController.getInstance().getIdentifyManager().isInPregnancyBabyMode()) {
//                NewsHomeController.getInstance().setFlipCount(0);
//                NewsHomeController.getInstance().setEnablePullToFlip(true);
//            }
//        }
    }

    private void initHeadUI() {
        mListViewHeader = LayoutInflater.from(getActivity()).inflate(R.layout.layout_news_home_head_animation, null);
        mListview.addHeaderView(mListViewHeader);
        rlSelectCity = (RelativeLayout) mListViewHeader.findViewById(R.id.rlSelectCity);
        tvSelectCity = (TextView) mListViewHeader.findViewById(R.id.tvSelectCity);
        if (classifyId == HomeType.CITY_ID) {
            rlSelectCity.setVisibility(View.VISIBLE);
        } else {
            rlSelectCity.setVisibility(View.GONE);
        }
        // 加入头部
        rl_loadding = (RelativeLayout) mListViewHeader.findViewById(R.id.rl_loadding);
        rl_update = (RelativeLayout) mListViewHeader.findViewById(R.id.rl_update);
        mListview.setScaleView(ivBannerBg);
        mListview.setLoaddingView(rl_update, rl_loadding, news_home_scroll_layout);
        LogUtils.d(TAG, "initHeadUI: 是否可见  " + isVisible + "  类名: " + classifyName);
        if (isVisible) {
            setViews();
        }
    }

    /**
     * listview头部的控件传到外层去做
     */
    private void setViews() {
        if (news_home_scroll_layout == null || rl_update == null || rl_loadding == null || ivBannerBg == null)
            return;
        LogUtils.d(TAG, "真正的把控件传递进去: " + classifyName);
        news_home_scroll_layout.setLoaddingView(rl_update, rl_loadding, ivBannerBg);
        news_home_scroll_layout.setOnRefreshListener(new NewsHomeParallaxListview.OnRefreshListener() {
            @Override
            public void OnRefresh() {
//                HomeRecommendCacheController.getInstance().handlePostYoumentSysxTime(classifyId, "下拉刷新");
//                pullDownRefresh();//下拉刷新
            }
        });
    }


    public void pressBack() {
        if (null != mListview && mListview.getCount() > 0) {
            LogUtils.e(TAG,"pressBack setSelection");
            mListview.setSelection(0);

        }
    }

    @Override
    public void onClick(View v) {

    }
}
