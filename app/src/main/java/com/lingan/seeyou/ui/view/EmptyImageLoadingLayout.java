package com.lingan.seeyou.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.LogUtils;


@SuppressLint("ViewConstructor")
public class EmptyImageLoadingLayout extends LoadingLayout {

    ImageView mContentView;
    HeartView mHeartView;
    int state = PullToRefreshBase.PULL_TO_REFRESH;


    public EmptyImageLoadingLayout(Context context) {
        this(context, null);
    }

    public EmptyImageLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = ViewFactory.from(context).getLayoutInflater().inflate(R.layout.empty_loadinglayout_show_iamge, this);
        mContentView = (ImageView) view.findViewById(R.id.content_view);
        mHeartView = (HeartView) view.findViewById(R.id.process_view);
    }

    @Override
    public void reset() {
        LogUtils.d("HeartView reset");
        mHeartView.reset();
        state = PullToRefreshBase.PULL_TO_REFRESH;
    }

    @Override
    public void releaseToRefresh() {
        LogUtils.d("HeartView", " releaseToRefresh");
        state = PullToRefreshBase.RELEASE_TO_REFRESH;
    }

    @Override
    public void stableRefreshing() {
        super.stableRefreshing();
        if (state == PullToRefreshBase.REFRESHING) {
            mHeartView.start();
        }
    }

    @Override
    public void setTimeText() {

    }

    @Override
    public void refreshing() {
        LogUtils.d("HeartView", " refreshing");
        state = PullToRefreshBase.REFRESHING;

    }

    @Override
    public void onScroll(int scroll) {
        switch (state) {
            case PullToRefreshBase.PULL_TO_REFRESH:
                refreshHeartView(scroll);
                break;
            case PullToRefreshBase.REFRESHING:
                break;
            case PullToRefreshBase.RELEASE_TO_REFRESH:
                break;

        }

    }

    private void refreshHeartView(int scroll) {
        if (scroll == 0) {
            //mHeartView.reset();
        }
        int height = Math.abs(scroll);
        float phase = height * 1.0f / getScrollSwitchHeight();
        //LogUtils.d("HeartView onScroll", "" + phase);
        mHeartView.drawingPath(phase);
    }

    @Override
    public int getScrollSwitchHeight() {
        return Float.valueOf(mHeartView.getHeight() * 3.0f / 2).intValue();
    }

    @Override
    public void pullToRefresh() {
        state = PullToRefreshBase.PULL_TO_REFRESH;
    }

    @Override
    public int getScrollMiniHeight() {
        return mHeartView.getHeight();
    }

    public int getScrollMaxHeight() {
        return getMeasuredHeight()+ 8;
    }

    public ImageView getContentView() {
        return mContentView;
    }

}
