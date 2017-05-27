package nickgao.com.meiyousample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.com.meetyou.news.ILoaderView;
import com.com.meetyou.news.LoadStateHelper;
import com.com.meetyou.news.OnNewsDetailLoadFailureListener;
import com.com.meetyou.news.RecyclerViewHelper;
import com.com.meetyou.news.RefreshViewHelper;
import com.com.meetyou.news.model.DataLoader;
import com.com.meetyou.news.model.ListLoader;
import com.com.meetyou.news.model.NewsReviewDetailDataModel;
import com.com.meetyou.news.model.NewsReviewDetailModel;
import com.com.meetyou.news.model.NewsReviewModel;
import com.com.meetyou.news.model.OnLoadSuccessListener;
import com.lingan.seeyou.ui.view.LoadingView;
import com.lingan.seeyou.ui.view.PraiseButton;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.skin.ViewFactory;
import com.meetyou.crsdk.util.ImageLoader;
import com.meetyou.crsdk.util.NetWorkStatusUtil;
import com.meetyou.news.view.OnRecycleViewScrollListener;
import com.meetyou.news.view.ReviewDetailItemDecoration;
import com.meetyou.pullrefresh.lib.PtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import activity.PeriodBaseActivity;
import nickgao.com.framework.utils.CalendarUtil;
import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.meiyousample.adapter.NewsReviewDetailAdapter;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.skin.ToastUtils;
import nickgao.com.meiyousample.utils.EmojiConversionUtil;
import nickgao.com.okhttpexample.view.ImageLoadParams;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/5/25.
 */

public class NewsReviewDetailActivity extends PeriodBaseActivity implements ILoaderView {

    private RecyclerView mRecyclerView;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadingView loadingView;


    private Activity mActivity;
    private Mode mMode = Mode.NORMAL;
    private List<NewsReviewModel> mSubReviews = new ArrayList<>();

    public enum Mode {
        NORMAL, GOTO
    }
    int mGotoId;
    private NewsReviewDetailAdapter mAdapter;
    //主评论控件
    private RelativeLayout rlMainReview;  //主楼父布局
    private LinearLayout llHeadLoading; //跳楼模式 头部loading
    private LoaderImageView ivUserAvatar, ivNews;
    private TextView tvUserName, tvPublishTime, tvReviewContent, tvNewsContent;
    private TextView mReviewCountTv;
    private PraiseButton btnPraise;
    private LinearLayout llNews;
    private LinearLayoutManager mLayoutManager;
    private long mPageCode = System.currentTimeMillis();
    private int mReviewId;
    private boolean showNewsDetail = true;  //是否显示资讯详情相关

    private ListLoader<NewsReviewDetailModel, NewsReviewModel> mLoader;
    private NewsReviewDetailDataModel mDataModel;
    private NewsReviewModel mHeaderReplyModel;//主评论
    private NewsReviewModel mNeedToRepliedModel;//被回复的评论


    @Override
    protected int getLayoutId() {
        return R.layout.layout_news_review_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageLoader.initialize(this, false);
        mActivity = this;
        super.onCreate(savedInstanceState);

        loadingView = (LoadingView) findViewById(R.id.loadingView);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_review_detail);
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.ptr_layout);
        mPtrFrameLayout.setVisibility(View.GONE);
        initSkin();
        initRecyclerView();
        initLoader();

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
    /**
     * 初始化列表
     */
    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new ReviewDetailItemDecoration(this));
        mRecyclerView.addOnScrollListener(new OnRecycleViewScrollListener(this));
        LayoutInflater inflater = ViewFactory.from(mActivity).getLayoutInflater();
        View headerView = inflater.inflate(R.layout.layout_news_review_detail_header, mRecyclerView, false);
        mReviewCountTv = (TextView) headerView.findViewById(R.id.tv_review);
        rlMainReview = (RelativeLayout) headerView.findViewById(R.id.rl_main_review);
        llHeadLoading = (LinearLayout) headerView.findViewById(R.id.ll_head_loading);
        llHeadLoading.setVisibility(View.GONE);
        ivUserAvatar = (LoaderImageView) headerView.findViewById(R.id.iv_user_avatar);
        ivNews = (LoaderImageView) headerView.findViewById(R.id.iv_news);
        tvUserName = (TextView) headerView.findViewById(R.id.tv_user_name);
        tvPublishTime = (TextView) headerView.findViewById(R.id.tv_publish_time);
        tvReviewContent = (TextView) headerView.findViewById(R.id.tv_review_content);
        tvNewsContent = (TextView) headerView.findViewById(R.id.tv_news_content);
        btnPraise = (PraiseButton) headerView.findViewById(R.id.btn_praise);
        llNews = (LinearLayout) headerView.findViewById(R.id.ll_news);

//        NewsReviewModel model1 = new NewsReviewModel();
//        model1.content = "dasfdsfsfsdfsd";
//        model1.id = 2;
//        mSubReviews.add(model1);
//
//        NewsReviewModel model2 = new NewsReviewModel();
//        model2.content = "fdsmfkmwkefewfe";
//        model2.id = 3;
//        mSubReviews.add(model1);

        mAdapter = new NewsReviewDetailAdapter(mActivity, mSubReviews);
        mAdapter.addHeaderView(headerView);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setPageCode(mPageCode);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }


    private void initLoader() {
        mDataModel = new NewsReviewDetailDataModel(mSubReviews, mGotoId);
        mDataModel.setReviewId(mReviewId);
        mDataModel.setShowHeader(true);
        mLoader = new ListLoader<>(this, mDataModel);
        mLoader.setListViewHelper(new RecyclerViewHelper(mRecyclerView));
        mLoader.setRefreshViewHelper(new RefreshViewHelper(mPtrFrameLayout));
        mLoader.setLoadStateHelper(new LoadStateHelper(mPtrFrameLayout, loadingView));
        mLoader.setOnLoadFailureListener(new OnNewsDetailLoadFailureListener(this));
        mLoader.setOnLoadSuccessListener(new OnLoadSuccessListener<NewsReviewDetailModel>() {
            @Override
            public void onSuccess(boolean isRefreshing, NewsReviewDetailModel response) {
                if (mHeaderReplyModel == null) {
                    mHeaderReplyModel = response.news_review;
                    fillHeaderUI(response.news_review);
                    mAdapter.setValue(getNewsId(), response.news_review);
                    mAdapter.notifyDataSetChanged();
                    isShowKeyboard();
                }
                handleResult(response);//先处理数据
                mGotoId = 0;
                mDataModel.setGotoId(mGotoId);//数据加载成功后，就不再设置跳楼了
            }
        });
        mLoader.load();

        //加载上一页帮助类
//        NewsReviewDetailLoadPrevHelper prevHelper = new NewsReviewDetailLoadPrevHelper(this, mRecyclerView, mDataModel, llHeadLoading);
//        prevHelper.init();
    }

    private void handleResult(NewsReviewDetailModel response) {
        if (!NetWorkStatusUtil.queryNetWork(mActivity)) {
            ToastUtils.showToast(mActivity, R.string.network_broken);
        }
        if (response == null) {
            handleModeChanged(mMode);
            return;
        }
        //成功且有数据
       // handleFirstLoadResult(response);
        handleModeChanged(mMode);
    }

    private void handleModeChanged(Mode mode) {
        if (mode == Mode.NORMAL) {
            mPtrFrameLayout.setEnabled(true);
            rlMainReview.setVisibility(View.VISIBLE);
        } else if (mode == Mode.GOTO) {
            mPtrFrameLayout.setEnabled(false);
            rlMainReview.setVisibility(View.GONE);
        }
    }


    private void isShowKeyboard() {
//        int line = getTextViewLineCount(mHeaderReplyModel.content, tvReviewContent.getWidth(), tvReviewContent.getPaint());
//        if ((mSubReviews.size() == 0 && line < 6) || mNeedToRepliedModel != null) {
//            mReplyHelper.showEditPanel(getNewsId(), mHeaderReplyModel, mNeedToRepliedModel, mPageCode);
//        }
    }

    private int getTextViewLineCount(String text, int textViewWidth, TextPaint paint) {
        StaticLayout staticLayout = new StaticLayout(text, paint, textViewWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        return staticLayout.getLineCount();
    }



    private int getNewsId() {
        if (mHeaderReplyModel == null || mHeaderReplyModel.news_detail == null) {
            return 0;
        } else {
            return mHeaderReplyModel.news_detail.id;
        }
    }

    /**
     * 填充主评论数据
     */
    private void fillHeaderUI(NewsReviewModel model) {
        if (model == null) {
            return;
        }
        if (model.publisher != null) {
            ImageLoadParams avatarParams = new ImageLoadParams();
            avatarParams.width = DeviceUtils.dip2px(mActivity, 32);
            avatarParams.height = DeviceUtils.dip2px(mActivity, 32);
            avatarParams.defaultholder = R.drawable.apk_mine_photo;
            avatarParams.tag = mActivity.hashCode();
            avatarParams.round = true;
            ImageLoader.getInstance().displayImage(mActivity, ivUserAvatar, model.publisher.avatar, avatarParams, null);

            tvUserName.setText(model.publisher.screen_name);
        }
        tvPublishTime.setText(CalendarUtil.convertUtcTime(model.created_at));
        int emojiSize = (int) getResources().getDimension(R.dimen.list_icon_height_22);
        CharSequence content = EmojiConversionUtil.getInstace().getExpressionString(this, model.content, emojiSize, emojiSize);
        tvReviewContent.setText(content);
        btnPraise.setPraiseCount(model.praise_count);
        btnPraise.setPraiseState(model.is_praise);
        if (model.news_detail == null || !showNewsDetail) {
            llNews.setVisibility(View.GONE);
        } else {
            llNews.setVisibility(View.VISIBLE);

            tvNewsContent.setText(model.news_detail.title);
            ImageLoadParams newsParams = new ImageLoadParams();
            newsParams.width = DeviceUtils.dip2px(mActivity, 64);
            newsParams.height = DeviceUtils.dip2px(mActivity, 64);
            newsParams.defaultholder = R.drawable.tata_img_goodtopic;
            newsParams.tag = mActivity.hashCode();
            ImageLoader.getInstance().displayImage(mActivity, ivNews, model.news_detail.images, newsParams, null);
        }
        int drawableRight = 0;
//        if (model.is_author) {
//            drawableRight = R.drawable.tag_author;
//        }
        drawableRight = R.drawable.tag_author;
        tvUserName.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRight, 0);

        String reviewCount;
        if (model.review_count == 0) {
            reviewCount = getString(R.string.news_reply);
        } else {
            reviewCount = String.valueOf(model.review_count);
        }
        mReviewCountTv.setText(reviewCount);
    }

    @Override
    public void destroyLoader() {
        mLoader.onDestroy();
    }

    @Override
    public void setDataLoader(DataLoader loader) {

    }
}
