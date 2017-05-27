package com.meetyou.news.view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.meetyou.crsdk.util.ImageLoader;


/**
 *
 * Created by LinXin on 2017/3/3.
 */
public class OnRecycleViewScrollListener extends RecyclerView.OnScrollListener {

    private Activity mActivity;

    public OnRecycleViewScrollListener(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE://停止滚动
            case RecyclerView.SCROLL_STATE_DRAGGING://正在被外部拖拽,一般为用户正在用手指滚动
                ImageLoader.getInstance().resume(mActivity, mActivity.hashCode());
                break;
            case RecyclerView.SCROLL_STATE_SETTLING://自动滚动
                ImageLoader.getInstance().pause(mActivity, mActivity.hashCode());
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }
}