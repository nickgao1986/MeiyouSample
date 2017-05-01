package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import nickgao.com.meiyousample.personal.ImageViewWithMask;


/**
 * 下拉刷新的view
 * Created by zhuangyufeng on 16/9/5.
 */
public class ParallaxScrollListView extends ListView implements AbsListView.OnScrollListener {
    private static final int sDURATION = 500;//回弹速度控制，数值越大，速度越慢
    private static final float s_REFRESH_WIDTH_AND_HEIGHT = 25.0f;//dip
    private ImageViewWithMask mImageView;
    private int mDrawableMaxHeight = -1;
    private int mImageViewHeight = -1;//拉伸的背景图初始化高度
    private int mDefaultImageViewHeight = 0;
    private double mZoomRatio;
    //
    private ImageView mRefreshView;//刷新的view
    private RelativeLayout mScaleView;//拉伸背景view
    private int mTopMargin = -1;//初始化刷新view距离
    private int mRefreshHeight = -1;//刷新高度的临界点
    private boolean mIsRefreshing = false;//当前是否正在刷新
    private boolean mEnableRefresh = false;//当前是否可刷新
    private boolean mIsRefreshHeightReset = true;//记录拉开刷新后是否重置过原来的高度
    private int mRefreshingTag = -1;//记录刷新view到达刷新点时候背景拉伸的高度
    //
    // 底部
    private ParallaxScrollListViewFooter footerView;//底部刷新的view
    private boolean mIsLoadingMore; // 是否正在加载更多
    private boolean mAutoLoadMore; // 是否开启自动加载更多----当滚动到倒数第3条的时候自动加载
    private boolean mIsFooterAdded = false;//标识：是否已经添加过 footer了。
    private boolean m0penWifiLoad = false;//在wifi情况下才自动加载更多
    private boolean mCanPullAlways = false;//总是可以拉动
    private boolean mCanLoadMore = false;//是否可加载更多
    private boolean isFootCanPull=false;//底部是否开启弹性
    //
    private int mItemsCount;// listview 一共有多少项
    private int mVisibleItemCount;//一个页面可展示的条数
    //
    private OnScrollListener scrollListener;
    //
    public OnRefreshLinstener refreshLinstener;
    public OnLoadMoreListener loadMoreListener;


    public ParallaxScrollListView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ParallaxScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ParallaxScrollListView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        mDefaultImageViewHeight = dp2px(160.0f);
        setOnScrollListener(this);
        //
        footerView = new ParallaxScrollListViewFooter(context);
        footerView.hide();//默认情况下不开启加载更多，只有在设置了加载更多接口监听时才开启
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    /**
     * 设置加载背景的父view
     * 如有其它的head添加，请先调用这个方法
     *
     * @param mScaleView
     */
    public void setScaleView(RelativeLayout mScaleView) {
        if (mScaleView == null)
            return;
        this.mScaleView = mScaleView;
        addHeaderView(mScaleView);
    }


    /**
     * 设置背景的子view
     *
     * @param iv
     * @param height iv 的高度 单位是dp
     */
    public void setParallaxImageView(ImageViewWithMask iv, int height) {
        mImageView = iv;
        mRefreshHeight =  dp2px(height) / 4 * 1;
        //  mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * 设置总是可以拉动
     */
    public void enableCanPullAlways() {
        mCanPullAlways = true;
    }

    public void enableFootCanPull(){
        isFootCanPull=true;
    }


    /**
     * 不显示加载图标
     */
    public void unShowProgress() {
        footerView.unShowProgress();
    }

    /**
     * 设置 mRefreshView view
     *
     * @param mRefreshView
     */
    public void setRefreshView(ImageView mRefreshView) {
        if (mScaleView != null && mRefreshView != null) {
            this.mRefreshView = mRefreshView;
            //初始化刷新view的位置
            int widthAndHeight = dp2px(s_REFRESH_WIDTH_AND_HEIGHT);
            mTopMargin = -widthAndHeight;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(widthAndHeight, widthAndHeight);
            lp.leftMargin = dp2px(15.0f);
            lp.topMargin = mTopMargin;
            mScaleView.addView(this.mRefreshView, lp);
        }
    }

    /**
     * 设置 mRefreshView view
     *
     * @param imageResource 背景图片
     */
    public void setRefreshView(int imageResource) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(imageResource);
        setRefreshView(imageView);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        //保证footer 是最后一个view,所以在设置adapter的时候才添加，并且最多只能添加一次
        if (!mIsFooterAdded) {
            mIsFooterAdded = true;
            addFooterView(footerView);
        }
        super.setAdapter(adapter);
    }


    /**
     * 设置刷新回调接口
     *
     * @param refreshListener
     */
    public void setOnRefreshListener(OnRefreshLinstener refreshListener) {
        this.refreshLinstener = refreshListener;
    }

    /**
     * 设置加载更多监听
     *
     * @param listener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        openLoadMore();
        loadMoreListener = listener;
    }

    /**
     * 底部拉伸回到原来位置监听
     *
     * @param listener
     */
    public void setOnFooterViewResetListener(OnFooterViewResetListener listener) {
        footerView.setOnFooterViewResetListener(listener);
    }

    /**
     * 打开wifi的情况下才自动加载更多
     */
    public void openWifiLoadMore() {
        m0penWifiLoad = true;
    }

    /**
     * 手动开启刷新
     */
    public void startRefresh() {
        RelativeLayout.LayoutParams lp = getRefreshLayoutParams();
        if (lp.topMargin != mRefreshHeight) {
            lp.topMargin = mRefreshHeight;
            mRefreshView.requestLayout();
        }
        mIsRefreshing = true;
        mIsRefreshHeightReset = false;
        refreshViewStartAnimation(new RefreshingAnimation());
        if (refreshLinstener != null)
            refreshLinstener.onRefresh();
    }

    /**
     * 停止刷新
     */
    public void stopRefresh() {
        mIsRefreshing = false;
        mEnableRefresh = false;
        if (mImageView.getLayoutParams().height == mImageViewHeight)
            mIsRefreshHeightReset = true;
        if (mTopMargin < getRefreshLayoutParams().topMargin) {
            refreshViewStartAnimation(new RefreshAnimation(mRefreshView, mTopMargin));
        }
    }

    //加载更多
    public void startLoadMore() {
        if (mIsLoadingMore || !mCanLoadMore) {
            return;
        }
        mIsLoadingMore = true;
        changeFooterViewState(ParallaxScrollListViewFooter.sLOADING);
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        } else
            stopLoadMore();
    }

    /**
     * 加载更多的时候网络断开
     */
    public void loadMoreNetError() {
        if (mIsLoadingMore)
            mIsLoadingMore = false;
        footerView.setState(ParallaxScrollListViewFooter.sNONET);
    }

    /**
     * 加载更多结束，由外部调用
     */
    public void stopLoadMore() {
        if (mIsLoadingMore) {
            mIsLoadingMore = false;
            footerView.setState(ParallaxScrollListViewFooter.sNORMAL);
        }
    }

    //开启加载更多功能
    public void openLoadMore() {
        footerView.show();
        mCanLoadMore = true;
        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadMore();
            }
        });
    }

    /**
     * 隐藏加载更多
     */
    public void hideFooter() {
        mCanLoadMore = false;
        footerView.hide();
    }

    /**
     * 开启自动加载更多模式
     */
    public void openaAutoLoadMore() {
        mAutoLoadMore = true;
    }

    //改变底部加载item的显示状态
    private void changeFooterViewState(int state) {
        footerView.setState(state);
    }


    //获取是否达到加载更多条件
    private boolean isCanLoadMore() {
        if (mIsRefreshing || mIsLoadingMore)//正在刷新的时候禁止加载更多
            return false;
        return footerView.getBottomMargin() > footerView.getmHeight() / 4;
    }


    //dp转成px
    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    //获取刷新view的LayoutParams
    private RelativeLayout.LayoutParams getRefreshLayoutParams() {
        if (mRefreshView == null) setRefreshView(new ImageView(getContext()));
        return (RelativeLayout.LayoutParams) mRefreshView.getLayoutParams();
    }


    //判断wifi是否打开
    private boolean isWifiOpen() {
        try {
            WifiManager wm = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
            return (wm != null && WifiManager.WIFI_STATE_ENABLED == wm.getWifiState());
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initViewsBounds(mZoomRatio);
    }

    //设置背景图片的展示
    private void initViewsBounds(double zoomRatio) {
        if (mImageViewHeight == -1 && mImageView != null) {
            mImageViewHeight = mImageView.getHeight();
            if (mImageViewHeight <= 0) {
                mImageViewHeight = mDefaultImageViewHeight;
            }
            if (mRefreshHeight==-1)
                mRefreshHeight = mImageViewHeight / 4 * 1;
        }
        //
        if (mImageView != null && mDrawableMaxHeight == -1) {

            double ratio = ((double) mImageView.getDrawable().getIntrinsicWidth()) / ((double) mImageView.getWidth());
            mDrawableMaxHeight = (int) ((mImageView.getDrawable().getIntrinsicHeight() / ratio) * (zoomRatio > 1 ? zoomRatio : 1));
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        if (listener.hashCode() == this.hashCode())
            super.setOnScrollListener(listener);
        else
            scrollListener = listener;

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollListener != null)
            scrollListener.onScrollStateChanged(view, scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (scrollListener != null)
            scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        //
        mItemsCount = totalItemCount;
        mVisibleItemCount = visibleItemCount;
        int loadItem = (firstVisibleItem + visibleItemCount + 1);
        if (loadItem <= totalItemCount && loadItem >= (totalItemCount - 2)) {
            if (m0penWifiLoad && !isWifiOpen()) {
                return;
            }
            if (mAutoLoadMore) {
                startLoadMore();
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    float lastY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (lastY == -1)
            lastY = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //
                mEnableRefresh = false;
                if (mImageView != null)
                    mImageView.clearAnimation();
                int deltaY = (int) (ev.getRawY() - lastY);//小于0 往上滚动 大于0 往下滚动
                if (deltaY > 100)
                    deltaY = 0;
                lastY = ev.getRawY();
                //footer 弹性变化
                if (footerView.getBottomMargin() > -1 && getLastVisiblePosition() == (mItemsCount - 1) && (mVisibleItemCount < mItemsCount || deltaY < 0) && mImageView.getHeight() == mImageViewHeight) {
                    if (!isFootCanPull){
                        return super.onTouchEvent(ev);
                    }
                    int OFFSET_RADIO = 2;
                    if (deltaY > 0)
                        OFFSET_RADIO = 1;

                    footerView.setBottomMargin((footerView.getBottomMargin() - deltaY / OFFSET_RADIO) > 0 ? (footerView.getBottomMargin() - deltaY / OFFSET_RADIO) : 0);
                    if (mIsLoadingMore)
                        return super.onTouchEvent(ev);
                    if (deltaY > 0 && footerView.getBottomMargin() > 0)
                        setSelection(mItemsCount - 1);
                    if (isCanLoadMore())
                        changeFooterViewState(ParallaxScrollListViewFooter.sREADY);
                    else if (!isCanLoadMore())
                        changeFooterViewState(ParallaxScrollListViewFooter.sNORMAL);

                } else {//头部弹性
                    if (deltaY > 0 && getFirstVisiblePosition() == 0 && mScaleView != null && mScaleView.getTop() == 0) {
                        if ((mIsRefreshing || mIsLoadingMore) && !mCanPullAlways)
                            return super.onTouchEvent(ev);
                        if (mImageView != null && mImageView.getHeight() <= mDrawableMaxHeight) {
                            if (mImageView.getHeight() + deltaY >= mImageViewHeight) {
                                mImageView.getLayoutParams().height = mImageView.getHeight() + deltaY < mDrawableMaxHeight ?
                                        mImageView.getHeight() + deltaY : mDrawableMaxHeight;
                                mImageView.requestLayout();
                                //刷新的view
                                if (!mIsRefreshing && mIsRefreshHeightReset) {
                                    RelativeLayout.LayoutParams lp = getRefreshLayoutParams();
                                    lp.topMargin = lp.topMargin + deltaY < mRefreshHeight ? lp.topMargin + deltaY : mRefreshHeight;
                                    mRefreshView.requestLayout();
                                    if (lp.topMargin == mRefreshHeight && mRefreshingTag <= 0)
                                        mRefreshingTag = mImageView.getLayoutParams().height;
                                    refreshViewPushAnimation(lp.topMargin - mTopMargin);
                                }
                            }
                        }
                    } else {//头部回滚
                        if (mImageView == null || getFirstVisiblePosition() != 0)
                            return super.onTouchEvent(ev);
                        // firstView.getTop < getPaddingTop means mImageView will be covered by top padding,
                        // so we can layout it to make it shorter
                        //往回推的时候背景图回收
                        if (mImageView.getHeight() > mImageViewHeight) {
                            // to set the firstView.mTop to 0,
                            // maybe use View.setTop() is more easy, but it just support from Android 3.0 (API 11)
                            //
                            mImageView.getLayoutParams().height = Math.max(mImageView.getHeight() + deltaY, mImageViewHeight);
                            mImageView.requestLayout();
                            //判断背景拉伸是否还原过高度
                            if (!mIsRefreshHeightReset && mImageView.getLayoutParams().height == mImageViewHeight && !mIsRefreshing)
                                mIsRefreshHeightReset = true;
                            //正在刷新中不处理
                            if (!mIsRefreshing && mRefreshingTag >= mImageView.getLayoutParams().height) {
                                getRefreshLayoutParams().topMargin = Math.max(getRefreshLayoutParams().topMargin + deltaY, mTopMargin);
                                mRefreshView.requestLayout();
                                refreshViewPushAnimation(getRefreshLayoutParams().topMargin - mTopMargin);
                            }
                            setSelection(0);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                //背景view的回弹
                if (mImageView != null && mImageViewHeight < mImageView.getHeight()) {
                    if ((mImageView.getHeight() - mImageViewHeight) >= (mRefreshHeight + dp2px(s_REFRESH_WIDTH_AND_HEIGHT))
                            && !mIsRefreshing && mIsRefreshHeightReset && !mIsLoadingMore) {//是否可进行刷新加载判断
                        mEnableRefresh = true;
                    } else if (!mCanPullAlways || !mIsRefreshing) {
                        stopRefresh();
                    }
                    //
                    ResetAnimation animation = new ResetAnimation(
                            mImageView, mImageViewHeight);
                    animation.setDuration(sDURATION);
                    mImageView.startAnimation(animation);
                }
                //加载更多的处理
                if (footerView.getBottomMargin() > 0) {
                    if (isCanLoadMore()) {
                        startLoadMore();
                    }
                    footerView.resetFooterMargin(300);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setZoomRatio(double zoomRatio) {
        mZoomRatio = zoomRatio;
    }


    //开启刷新动画
    private void refreshViewStartAnimation(Animation animation) {
        mRefreshView.clearAnimation();
        animation.setDuration(sDURATION);
        mRefreshView.startAnimation(animation);
    }

    float nowRotate = 0f;//当前的旋转角度

    //拉动的时候动画
    private void refreshViewPushAnimation(int delta) {
        mRefreshView.clearAnimation();
        float newRotate = delta * 5.0f;
        RotateAnimation rotateDownAnim = new RotateAnimation(nowRotate, newRotate, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(180);
        rotateDownAnim.setFillAfter(true);// 动画执行完停留在执行完的状态
        mRefreshView.setAnimation(rotateDownAnim);
        nowRotate = newRotate;
    }

    //
    public class ResetAnimation extends Animation {
        int targetHeight;//最初原本的高度
        int extraHeight;//当前高度与原本高度的距离
        View mView;
        // int extraBindTopMargin;//绑定的view当前距离与原本距离的之间差


        protected ResetAnimation(View view, int targetHeight) {
            this.mView = view;
            this.targetHeight = targetHeight;
            extraHeight = this.targetHeight - view.getHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mView.getLayoutParams().height = (int) (targetHeight - extraHeight * (1 - interpolatedTime));
            mView.requestLayout();
            if (interpolatedTime == 1) {
                mView.clearAnimation();
                //
                if (!mIsRefreshing)
                    mIsRefreshHeightReset = true;
            }
            if (interpolatedTime >= 1.0f && mEnableRefresh && !mIsRefreshing) {//做延时刷新，防止用户手指切换滑动时触发刷新
                startRefresh();
               /* mIsRefreshing = true;
                mIsRefreshHeightReset = false;
                if (refreshLinstener != null)
                    refreshLinstener.onRefresh();
                refreshViewStartAnimation(new RefreshingAnimation());*/
            }
        }
    }


    //refreshview 的回弹效果
    public class RefreshAnimation extends Animation {
        int targetTopMargin;//原本的距离
        int extraTopMargin;//当前距离与原本距离的之间差
        View mView;

        protected RefreshAnimation(View view, int targetTopMargin) {
            mView = view;
            this.targetTopMargin = targetTopMargin;
            extraTopMargin = ((RelativeLayout.LayoutParams) mView.getLayoutParams()).topMargin - this.targetTopMargin;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            ((RelativeLayout.LayoutParams) mView.getLayoutParams()).topMargin = (int) (targetTopMargin + extraTopMargin * (1 - interpolatedTime));
            mView.requestLayout();
            if (interpolatedTime == 1)
                mView.clearAnimation();
        }
    }

    //刷新的时候旋转动画
    public class RefreshingAnimation extends RotateAnimation {

        public RefreshingAnimation() {
            super(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            setRepeatCount(-1);
            setInterpolator(new LinearInterpolator());
        }

        @Override
        public void setDuration(long durationMillis) {
            super.setDuration(300);
        }
    }

    /**
     * 刷新接口
     */
    public interface OnRefreshLinstener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /**
     * 底部拉伸回到原来位置接口
     */
    public interface OnFooterViewResetListener {
        void onFooterViewRest();
    }
}
