package nickgao.com.meiyousample.firstPage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.ListFooterUtil;
import com.lingan.seeyou.ui.view.LoadingView;
import com.lingan.seeyou.ui.view.NewsHomeParallaxListview;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.firstPage.module.RecommendTopicResponeModel;
import nickgao.com.meiyousample.firstPage.view.ScrollableLayout;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class TestFragment extends Fragment {

    private View rootView;
    private final String TAG = "NewsHomeClassifyFragment";
    private LayoutInflater layoutInflater;
    private View topicFooter;
    private ListView mListview;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_new_home_classify_fragment,null);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // init();
        initView();
        intLogic();
    }

    private void intLogic() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadHomeTabData();
            }
        }, 2000);
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

    private void initView() {
        //这三个是外部的控件
        news_home_scroll_layout = (ScrollableLayout) rootView.findViewById(R.id.news_home_scroll_layout);
        ivBannerBg = (ImageView) rootView.findViewById(R.id.ivBannerBg);
        layoutInflater = getActivity().getLayoutInflater();
        mListview = (ListView) rootView.findViewById(R.id.news_home_listview);
        loadingView = (LoadingView) rootView.findViewById(R.id.news_home_loadingView);
        rlManSelectCity = (RelativeLayout) rootView.findViewById(R.id.rlManSelectCity);
        if (first_requst == 0) {
            loadingView.setStatus(LoadingView.STATUS_LOADING);
        }
//        if (classifyId != HomeType.RECOMMEND_ID || news_home_scroll_layout.isSticked()) {
//            NewsHomeController.getInstance().handlePosition(getActivity().getApplicationContext(), loadingView);//loadingview的位置
//        }
        initHeadUI();
        //加入底部布局
        topicFooter = ListFooterUtil.getInstance().getListViewFooter(layoutInflater);
        mListview.addFooterView(topicFooter);
        View emptySpaceView = layoutInflater.inflate(R.layout.layout_home_empty_space, null);
        emptySpaceView.setVisibility(View.VISIBLE);
        mListview.addFooterView(emptySpaceView);
        ListFooterUtil.getInstance().hideListViewFooter(topicFooter);
      //  mListview.updateUI();
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
//        mListview.setScaleView(ivBannerBg);
//        mListview.setLoaddingView(rl_update, rl_loadding, news_home_scroll_layout);
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

    public String getFromAssets(String fileName,final Context context){
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadHomeTabData() {
        //本地tab下需要判断当前有没选择城市，没有城市的话显示定位服务不可以用
        if (classifyId == HomeType.CITY_ID && StringUtils.getInt(cityId) == 0) {//本地tab类
            rlManSelectCity.setVisibility(View.VISIBLE);
            loadingView.setContent(LoadingView.STATUS_NODATA, "定位服务不可用");
            return;
        } else {
            rlManSelectCity.setVisibility(View.GONE);
        }
        isTopicLoading = true;
//        NewsHomeController.getInstance().loadCommunityDataFromCache(getActivity(), classifyId, new IOnExcuteListener() {
//            @Override
//            public void onResult(Object object) {
//                boolean isHasCacheData;
//                if (object == null) {
//                    isHasCacheData = false;
//                } else {
//                    RecommendTopicResponeModel communityResponeModel = (RecommendTopicResponeModel) object;
//                    if (null != communityResponeModel && null != communityResponeModel.list && communityResponeModel.list.size() > 0) {
//                        isHasCacheData = true;
//                        recommendTopicList.clear();
//                        recommendTopicList.addAll(communityResponeModel.list);
//                        updateList();
//                        loadingView.hide();
//                    } else {
//                        isHasCacheData = false;
//                    }
//                }
//                if (isLoadingNetWokeData || !isHasCacheData) {//没有缓存数据的时候 和 强制需要加载网络数据的时候去加载网络数据
//                    // loadRecommend(cityId, true, true);//加载网络数据
//                } else {
//                    isTopicLoading = false;
//
//                }
//            }
//        });
        String str = getFromAssets("talk.txt",getActivity());
        RecommendTopicResponeModel recommendTopicResponeModel = null;
        try {
            JSONObject object = new JSONObject(str);
            recommendTopicResponeModel = new RecommendTopicResponeModel(getActivity(), object);
        }catch (Exception ex) {

        }
        recommendTopicList.clear();
        recommendTopicList.addAll(recommendTopicResponeModel.list);
        loadingView.hide();
       // NewsHomeClassifyAdapter classifyAdapter = new NewsHomeClassifyAdapter(getActivity(), layoutInflater, recommendTopicList, mListview, classifyId, classifyName, mOnRealPositionListener);
        MeiyouAccountsOpusAdapter adapter = new MeiyouAccountsOpusAdapter(getActivity(), layoutInflater, recommendTopicList, mListview, classifyId, classifyName, null);
        loadingView.hide();
        mListview.setVisibility(View.VISIBLE);
        mListview.setAdapter(adapter);

    }

    private BaseAdapter getAdapter() {
        if (classifyAdAdapter != null) {
            return classifyAdAdapter;
        }
        return classifyAdapter;
    }

    private NewsHomeClassifyAdapter.OnRealPositionListener mOnRealPositionListener = new NewsHomeClassifyAdapter.OnRealPositionListener() {
        @Override
        public int getRealPosition(int position) {
            if (null == classifyAdAdapter) {
                return position;
            } else {
//                if (classifyAdAdapter instanceof FeedsAdapter) {
//                    return ((FeedsAdapter) classifyAdAdapter).getRealPosition(position);
//                }
                return position;
            }
        }
    };

    /**
     * 数据加载完之后刷新数据
     */
    public void updateList() {
        try {
            mListview.setVisibility(View.VISIBLE);
            if (null == classifyAdapter) {
                classifyAdapter = new NewsHomeClassifyAdapter(getActivity(), layoutInflater, recommendTopicList, mListview, classifyId, classifyName, mOnRealPositionListener);
                ((NewsHomeClassifyAdapter) classifyAdapter).setOnlyVideoIdAndVisible(onlyVideoId, isVisible);
                /*ListViewAnimationAdapter listViewAnimationAdapter= new ListViewAnimationAdapter(classifyAdapter);
                listViewAnimationAdapter.setDebug(true);*/
                mListview.setAdapter(getAdapter());
            } else {
                ((NewsHomeClassifyAdapter) classifyAdapter).setOnlyVideoIdAndVisible(onlyVideoId, isVisible);
                getAdapter().notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
