package nickgao.com.meiyousample.myfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.BadgeImageView;
import com.lingan.seeyou.ui.view.LoadingView;
import com.lingan.seeyou.ui.view.ParallaxScrollListView;
import com.lingan.seeyou.ui.view.ResizeLayout;
import com.lingan.seeyou.ui.view.RoundedImageView;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.meetyou.crsdk.util.ImageLoader;
import com.meetyou.pullrefresh.lib.PtrFrameLayout;

import activity.PeriodBaseActivity;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.adapter.HomeDynamicAdapter;
import nickgao.com.meiyousample.personal.ImageViewWithMask;
import nickgao.com.okhttpexample.view.LoaderImageView;


/**
 * 密友圈
 */
public class DynamicHomeActivityUsingRecycleView extends PeriodBaseActivity{

    private String TAG = "DynamicHomeActivity";
    public static final String PREV = "prev";
    public static final String NEXT = "next";
    private Activity myActivity;

    private HomeDynamicController mHomeDynamicController;
    private ResizeLayout rootContainer; // 根布局
    private ParallaxScrollListView parallaxListview;


    private LoaderImageView ivBannerBg;
    private TextView tvNoNetwork;


    private RoundedImageView ivHead;


    private BadgeImageView bivVerify;
    private LinearLayout llMsgTip;
    private TextView tvMsgTip;// 消息提示


    private LoaderImageView ivMsgIcon;
    private BadgeImageView bivMsgVerify;

    private LinearLayout llTopMenu;


    // 列表底部
    private View moreView;
    private ProgressBar moreProgressBar;
    private TextView loadMoreView;

    // 正在加载
    private boolean bLoading = false;
    private boolean isFistLoad;//是否首次加载

    private LinearLayout llEmptyContainer;
    private Button btnOperate;


    private HomeDynamicAdapter mAdapter;

    private int mLvCurrentPosition = 0;
    private boolean isKeyboardShow;

    private int scrolledX;
    private int scrolledY;
    boolean isActivityFinish = false;
    private float mLastY;
    private RelativeLayout rl_custom_title_bar;
    private float headerHeight = 0f;
    private View mListViewHeader;
    //private ImageView mMask;
    private ImageViewWithMask mImageViewWithMask;

    private RecyclerView mRecyclerView;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadingView loadingView;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.dynamic_home_layout_using_recycle_view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        ImageLoader.initialize(this, false);
        initSkin();

        super.onCreate(savedInstanceState);
        myActivity = this;
        LogUtils.i("DynamicHomeActivity onCreate");

        loadingView = (LoadingView) findViewById(R.id.loadingView);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_review_detail);
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.ptr_layout);
        mPtrFrameLayout.setVisibility(View.GONE);

    }


    private void initRecyclerView() {
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new ReviewDetailItemDecoration(this));
//        mRecyclerView.addOnScrollListener(new OnRecycleViewScrollListener(this));
//        LayoutInflater inflater = ViewFactory.from(mActivity).getLayoutInflater();
//        View headerView = inflater.inflate(R.layout.layout_news_review_detail_header, mRecyclerView, false);
//
//
//        mAdapter = new NewsReviewDetailAdapter(mActivity, mSubReviews);
//        mAdapter.addHeaderView(headerView);
//        mAdapter.setHeaderAndEmpty(true);
//        mAdapter.setPageCode(mPageCode);
//        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSkin() {
        try {
            //初始化皮肤
            SkinManager.getInstance().init(this, this.getResources(), this.getAssets());
            SkinManager.getInstance().setApply(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            LogUtils.i("DynamicHomeActivity onResume" + mLvCurrentPosition);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onDestroy() {
        LogUtils.i("DynamicHomeActivity onDestory");

        super.onDestroy();
    }

}