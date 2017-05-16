package nickgao.com.meiyousample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lingan.seeyou.ui.view.LinearListView;
import com.lingan.seeyou.ui.view.ListFooterUtil;
import com.lingan.seeyou.ui.view.LoadingView;
import com.lingan.seeyou.ui.view.news.view.DetailListView;
import com.lingan.seeyou.ui.view.news.view.DetailScrollView;
import com.lingan.seeyou.ui.view.news.view.NewsDetailToolBar;
import com.lingan.seeyou.ui.view.skin.ViewFactory;
import com.lingan.seeyou.ui.view.webview.CustomWebView;
import com.lingan.seeyou.ui.view.webview.WebViewController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.adapter.NewsDetailAdapter;
import nickgao.com.meiyousample.adapter.NewsTextRecommendAdapter;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.model.NewsDetailRecommendModel;
import nickgao.com.meiyousample.model.NewsDetailReviewListModel;
import nickgao.com.meiyousample.model.NewsDetailShareBodyModel;
import nickgao.com.meiyousample.model.NewsReviewModel;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/3/18.
 */

public class NewsListActivity extends BaseActivity {

    protected static final String TAG = "NewsListActivity";

    public static final String URL = "url";
    protected static final String NEWS_ID = "newsId";
    public static final String JS_NAME = "tlsj";
    private Activity mActivity;
    private long mPageCode = System.currentTimeMillis();

    private DetailScrollView mScrollView;
    private CustomWebView webView;
    private LoadingView loadingView;
    private DetailListView listView;
    //上拉加载更多View
    private View loadMoreView;

    private NewsDetailAdapter mAdapter;
    private LinearLayout llTextRecommend; //相关推荐父控件
    private LinearListView lvTextRecommend; //相关推荐列表
    //底部回复栏
    private NewsDetailToolBar mToolBar;
    private TextView tvWriteReview;
    private ImageView ivReply, ivShare;
   // private CollectButton btnCollect;
    private View mBottomBarLayout;
  //  protected NewsDetailReviewHelper mReviewHelper;//评论帮助类
    //头部头像和昵称
    private LinearLayout mTopAuthorLayout;//作者信息布局
    private LoaderImageView mTopAuthorImv;//作者头像
    private TextView mTopAuthorNickTv;//作者昵称

    //相关数据
    private String mUrl;  //H5加载的url
    private int mNewsId;
    private List<NewsReviewModel> mReviews = new ArrayList<>(); //回复
    private NewsDetailReviewListModel model;
    private NewsDetailShareBodyModel mShareBody;
    private boolean isActivityFinish;
    private boolean hasMoreData = true;//加载更多是否还有数据
    private int mWebViewHeight;
    private int mLastY = -1;
    private int mListScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private boolean isMoving = false;
    private int mScreenHeight, mScreenWidth;
    private int mFirstVisibleItem;
    private boolean isRecommendExposure = false; //是否已经上报过了

//    private NewsDetailPosRecordDO mRecordDO;  //上次阅读位置，用于第一次加载完WebView定位
//    private ShareCallBack callBack;
    private int mScrollY;//滑动的位置
    private float mCurY;
    private int mFadeShowAuthorY;
//    private NewsWebViewClient webViewClient;
    private Handler mHandler = new Handler();
    /**
     * Runnable延迟执行的时间
     */
    private long delayMillis = 100;

    /**
     * 上次滑动的时间
     */
    private long lastScrollUpdate = -1;

    /**
     * 监听scrollview是否停止
     */
    private Runnable scrollerTask = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 100) {
                lastScrollUpdate = -1;
                onScrollEnd();
            } else {
                mHandler.postDelayed(this, delayMillis);
            }
        }
    };

    private void onScrollEnd() {
        //getAKeyTopView().setStopScoll(true);
    }

    /**
     * 接收网页相关事件
     *
     * @param event
     */
    public void onEventMainThread(NewsWebViewEvent event) {
        if (event.getPageCode() == mPageCode) {
            switch (event.getEventType()) {
                case LOAD_DATA_SUCCESS:
                    mScrollView.setVerticalScrollBarEnabled(true);
                    webView.setVisibility(View.VISIBLE);
                    //H5数据加载结束，开始请求资讯评论
                    listView.setVisibility(View.VISIBLE);
                    mWebViewHeight = webView.getMeasuredHeight() + DeviceUtils.dip2px(mActivity, 10); //更新webView高度
                   // handleLocateLastPosition(mRecordDO);
                    loadNewsReviewData(0);

                    break;
            }
        }
    }

    private void loadNewsReviewData(int id) {
        LoadFileTask task = new LoadFileTask();
        task.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //跳转到下一个页面时也要考虑保存当前阅读位置
       // handleSaveReadingPosition();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.layout_news_detail_h5);
        initUI();
        loadFirstData();

        loadNewsReviewData(0);




    }

    protected void loadFirstData() {
        //直接拿url去load H5
        mUrl = "https://news-node.seeyouyima.com/article?news_id=3002603&platform=android&v=6.0&bundleid=1111&imei=866798025057912&myclient=0130600111100000&sdkversion=19&device_id=866798025057912&app_id=1&tbuid=&myuid=129746620&auth=3.1SgaXeZhJRoTsPsxwMtZ0494fWe8AHXzXtacKxVcjQQ%3D&mode=0";
        if (!StringUtils.isNull(mUrl)) {
           // loadingView.setStatus(LoadingView.STATUS_LOADING);
            this.webView.loadUrl(mUrl);
        } else {
            loadingView.setStatus(LoadingView.STATUS_NODATA);
        }
    }


    private void initUI() {
        mScrollView = (DetailScrollView) findViewById(R.id.scroll_view);
        listView = (DetailListView) findViewById(R.id.lv_news_reviews);
        loadingView = (LoadingView) findViewById(R.id.loadingView);
        loadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstData();
            }
        });
        webView = (CustomWebView) findViewById(R.id.webView);
        initScrollView();
        initWebView();
        initListView();

        //相关推荐
        llTextRecommend = (LinearLayout) findViewById(R.id.ll_recommend);
        llTextRecommend.setVisibility(View.GONE);
        lvTextRecommend = (LinearListView) findViewById(R.id.lv_text_recommend);
        lvTextRecommend.setRemoveDivider(true);
    }


    /**
     * 初始化ScrollView
     */
    private void initScrollView() {
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.setChildListView(listView);
        mScrollView.setOnScrollChangedListener(new DetailScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                mScrollY = t;
//                handleShowIbToTop(oldt);//是否显示一键置顶的按钮
//                fadeShowAuthorInfo(t);

                if (lastScrollUpdate == -1) {
                    mHandler.postDelayed(scrollerTask,delayMillis);
                }
                // 更新ScrollView的滑动时间
                lastScrollUpdate = System.currentTimeMillis();
            }
        });
        mScrollView.setScanScrollChangedListener(new DetailScrollView.ISmartScrollChangedListener() {
            @Override
            public void onScrolledToBottom(int vericalY) {
                //到底的时候,事件交给listview,此时,需要让scrollview惯性滚动一下,没滚动完之前,不运行scrollview拦截
                if (!listView.isHandleTouchEvent() && vericalY < 0 && !mScrollView.isTouchingScrollView() && mReviews
                        .size() > 0) {
                    handleListViewTouchEvent();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        listView.fling(Math.abs(vericalY / 3));
                    } else {
                        listView.startFling(Math.abs(vericalY / 3)/*5000*/);
                    }
                    listView.setHandleTouchEvent(true);
                    LogUtils.d(TAG, "==》onScrolledToBottom 让listview fling了!!");
                    return;
                }
                handleListViewTouchEvent();
            }

            @Override
            public void onScrolledToTop() {

            }
        });
        mScrollView.setMoveListener(new DetailScrollView.onMoveListener() {
            //当按下scrollview的时候,如果listview还在fling,强制重置它的位置,并抢夺事件;
            @SuppressLint("NewApi")
            @Override
            public void onDown() {
                if (listView != null && mListScrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    LogUtils.d(TAG, "listView还在fling,重置它的位置");
                    listView.stopFling();
                    listView.setSelectionFromTop(0, 1);
                    listView.setHandleTouchEvent(false);
                }
            }

            //ListView过渡到ScrollView的时候,需要再惯性让ScrollView再滚动一下
            @Override
            public void onUp(int velocityY) {
                if (isMoving) {
                    LogUtils.d(TAG, "mScrollView onUp:" + velocityY);
                    isMoving = false;
                    if (mScrollView != null && isWebViewOverScreen()) {
                        mScrollView.fling(velocityY);
                    }
                }
//                handleListViewTouchEvent();
                delayHandleListViewTouchEvent(1000);
            }

            @SuppressLint("NewApi")
            @Override
            public void onMove(float distance) {
                if (listView != null && mListScrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    LogUtils.d(TAG, "listView还在fling,重置它的位置");
                    listView.stopFling();
                    listView.setSelectionFromTop(0, 1);
                    listView.setHandleTouchEvent(false);
                    mListScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
                }
            }
        });
    }

    //webview是否超过可见范围,也就是,是否可滚动
    private boolean isWebViewOverScreen() {
        return mWebViewHeight > (mScreenHeight - getTopAndBottomHeight());
    }

    private void delayHandleListViewTouchEvent(int delayTime) {
        mScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isActivityFinish) {
                    return;
                }
                handleListViewTouchEvent();
            }
        }, delayTime);
    }

    private void initWebView() {
        webView.init(WebViewController.getInstance().getWebViewConfig());
        Context context = mActivity.getApplicationContext();
       // webViewClient = new NewsWebViewClient(mActivity, webView, loadingView, null, mPageCode);
//        webView.setWebViewClient(webViewClient);
//        webView.setWebChromeClient(new MeetyouWebViewChromeClient(mActivity));
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //拦截x5的长按事件
                return true;
            }
        });
//        WebViewJs webViewJs = new WebViewJs(mActivity, webView);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
//            webView.addJavascriptInterface(webViewJs, JS_NAME);
//        }
 //       webView.setVisibility(View.INVISIBLE);
    }

    private void initListView() {
        //上拉加载更多footerView
        loadMoreView = ListFooterUtil.getInstance()
                .getListViewFooter(ViewFactory.from(mActivity)
                        .getLayoutInflater());
        ListFooterUtil.getInstance()
                .updateListViewFooter(loadMoreView, ListFooterUtil.ListViewFooterState.NORMAL, "");
        listView.addFooterView(loadMoreView);

        mAdapter = new NewsDetailAdapter(this, mReviews, mNewsId, mPageCode, false);
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mListScrollState = scrollState;
                int lastIndex = listView.getAdapter().getCount() - 1;
                //滚动到底部
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() == lastIndex && hasMoreData) {
                    LogUtils.d(TAG, "=======>listview 滚动到底部 加载更多,处理事件");
                    //loadMoreReviews();
                    handleListViewTouchEvent();
                    return;
                }
                //滚动到顶部
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mFirstVisibleItem == 0 && listView
                        .getChildAt(0)
                        .getTop() == 0) {
                    LogUtils.d(TAG, "=======>listview 滚动到顶部,不处理事件了");
                    if (mScrollView != null)
                        mScrollView.smoothScrollBy(0, -5);
                    listView.setHandleTouchEvent(false);
                    return;
                }
                //停止的时候,进行事件决定
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    handleListViewTouchEvent();
                    if (listView.getChildCount() > 0) {
                        //getAKeyTopView().setStopScoll(true);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mFirstVisibleItem = firstVisibleItem;
            }
        });
        listView.getLayoutParams().height = DeviceUtils.dip2px(mActivity, 20);
        listView.requestLayout();
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.d(TAG, "MyListView onTouch ACTION_DOWN");
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    LogUtils.d(TAG, "MyListView onTouch ACTION_MOVE");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    LogUtils.d(TAG, "MyListView onTouch ACTION_UP");
                }
                //触摸的时候,让父控件不要拦截我的所有事件
                if (mScrollView != null && listView.isHandleTouchEvent()) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                    } else {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                }
                float y = event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        mCurY = y;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                        int detaY = (int) (mCurY - y);
                        if (detaY != 0) {
                            if (detaY < 0) {
                                //getAKeyTopView().setTopScoll(false);
                            } else {
                               // getAKeyTopView().setTopScoll(true);
                            }
                        }
                        mCurY = y;
                        break;
                }
                return false;
            }
        });
        listView.setMoveListener(new DetailListView.onMoveListener() {
            @Override
            public void onMove(float distance) {
                //listview在顶部的时候,往下滑动,此时需要scrollview跟着一起滚动,进行过渡
                if (mScrollView != null && listView != null && listView.getChildCount() > 0 && getListViewPositionAtScreen() >= getTopHeight() && distance > 0 && mFirstVisibleItem == 0 && listView
                        .getChildAt(0)
                        .getTop() == 0) {
                    // 滚动
                    mScrollView.smoothScrollBy(0, -(int) distance);
                    // 取消listview的事件消费,会在其onTouchEvent返回false
                    listView.setHandleTouchEvent(false);
                    isMoving = true;
                }
            }

            //拖动scrollview一起滚动之后,在松开的时候,需要有个惯性滚动
            @Override
            public void onUp(final int velocityY) {
                if (isMoving) {
                    LogUtils.d(TAG, "listView onUp:" + velocityY);
                    isMoving = false;
                    //延迟一下fling才有效果。
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mScrollView != null)
                                mScrollView.fling(-velocityY);
                        }
                    }, 50);
                }
            }
        });
//        getAKeyTopView().setOnAKeyTopLisener(new AKeyTopView.OnAKeyTopClickListener() {
//            @Override
//            public void OnAKeyTopClick() {
//                handleScrollToTop();
//            }
//        });
    }

    //停止后,决定事件是由谁来处理的;
    private void handleListViewTouchEvent() {
        try {
            if (listView == null)
                return;
            if (listView.getChildCount() > 0 && getListViewPositionAtScreen() > getTopHeight()) {
                listView.setHandleTouchEvent(false);
                LogUtils.d(TAG, "==》handleListViewTouchEvent listview 露出屏幕,由外部处理事件");
            } else {
                listView.setHandleTouchEvent(true);
                LogUtils.d(TAG, "==》handleListViewTouchEvent listview 占满全屏,自己处理事件");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int getListViewPositionAtScreen() {
        try {
            int[] location = new int[2];
            listView.getLocationInWindow(location);
            int y = location[1];
            return y;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }


    class LoadFileTask extends AsyncTask<Void, Void, NewsDetailReviewListModel> {

        @Override
        protected NewsDetailReviewListModel doInBackground(Void... params) {
            String str = getFromAssets("news.txt");
            NewsDetailReviewListModel data = null;
            String errorMsg = null;
            try {
                JSONObject jsonObject = new JSONObject(str);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    String dataString = jsonObject.optString("data");
                    if (!StringUtils.isNull(dataString)) {
                        data = JSON.parseObject(dataString, NewsDetailReviewListModel.class);
                    }
                } else {
                    errorMsg = jsonObject.getString("message");
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(NewsDetailReviewListModel homeDynamicModels) {
            fillRecommendUI(homeDynamicModels.news_recommend);
            mReviews.addAll(homeDynamicModels.news_review);

            notifyAndHandleListViewHeight();
        }

    }

    private void fillRecommendUI(List<NewsDetailRecommendModel> recommendModels) {
        if (recommendModels == null || recommendModels.size() == 0) {
            llTextRecommend.setVisibility(View.GONE);
        } else {
            llTextRecommend.setVisibility(View.VISIBLE);
            lvTextRecommend.setAdapter(new NewsTextRecommendAdapter(mActivity, recommendModels));
        }
        llTextRecommend.post(new Runnable() {
            @Override
            public void run() {
                //重新更新高度
                mWebViewHeight = webView.getHeight() + llTextRecommend.getHeight() + DeviceUtils.dip2px(mActivity, 10);
            }
        });
    }


    /**
     * 通知更新并处理ListView高度
     */
    private void notifyAndHandleListViewHeight() {
        if (listView == null || mAdapter == null || listView.getLayoutParams() == null) {
            return;
        }
        int lastHeight = listView.getLayoutParams().height;
        int targetHeight;
        if (mReviews.size() == 0) {
            //当没有评论时，listView高度缩减
            targetHeight = Math.max(DeviceUtils.dip2px(mActivity, 225), getContentHeight() / 2);
        } else {
            targetHeight = getContentHeight();
        }
        if (targetHeight != lastHeight) {
            listView.getLayoutParams().height = targetHeight;
            listView.requestLayout();
        }
        mAdapter.notifyDataSetChanged();
    }

    private int getContentHeight() {
        return mScreenHeight - getTopAndBottomHeight();
    }

    private int getTopAndBottomHeight() {
        return DeviceUtils.getStatusBarHeight(mActivity) + DeviceUtils.dip2px(mActivity, 50) + DeviceUtils
                .dip2px(mActivity, 48);
    }

    private int getTopHeight() {
        return DeviceUtils.getStatusBarHeight(mActivity) + DeviceUtils.dip2px(mActivity, 50);
    }
    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void loadRecommendData() {

    }
}
