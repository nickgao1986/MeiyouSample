package com.lingan.seeyou.ui.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends LinearLayout {

    static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;


    @SuppressLint("ResourceAsColor")
    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Deprecated
    //回收星星
    public void recycleBitmap() {

    }

    @Deprecated
    public void setIconVisible(boolean visible) {

    }

    public abstract void reset();


    public static interface Time {
        public static final long SECOND = 1000l;
        public static final long MINUTES = 60 * SECOND;
        public static final long HOUR = 60 * MINUTES;
        public static final long DAY = 24 * HOUR;
    }

    public abstract void releaseToRefresh();

    @Deprecated
    public void setPullLabel(String pullLabel) {

    }

    /**
     * 下拉 松手之后就是 refreshing
     */
    public abstract void refreshing();

    public void  stableRefreshing(){

    }

    @Deprecated
    public void setRefreshingLabel(String refreshingLabel) {

    }

    @Deprecated
    public void setReleaseLabel(String releaseLabel) {

    }

    @Deprecated
    public void setTimeTextVisibility(int visibility) {

    }

    public abstract void setTimeText();

    public abstract void pullToRefresh();

    @Deprecated
    public void setTextColor(int color) {

    }

    public void onScroll(int scroll) {

    }

    /**
     * 释放下拉刷新,应该回弹到loading layout 的哪个高度
     *
     * @return
     */
    public int getScrollMiniHeight() {
        return getMeasuredHeight();
    }

    /**
     * 下拉到哪个高度 能够触发刷新动作
     * @return
     */
    public int getScrollSwitchHeight(){
        return getScrollMiniHeight();
    }

    public int getScrollMaxHeight() {
        return getMeasuredHeight();
    }

}
