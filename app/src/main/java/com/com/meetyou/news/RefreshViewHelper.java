package com.com.meetyou.news;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.com.meetyou.news.model.IRefreshViewHelper;
import com.meetyou.pullrefresh.PtrDefaultHandler;
import com.meetyou.pullrefresh.lib.PtrFrameLayout;
import com.meetyou.pullrefresh.lib.PtrHandler;

/**
 * 刷新帮助类
 * Created by LinXin on 2017/3/2.
 */
public class RefreshViewHelper implements IRefreshViewHelper {

    private PtrFrameLayout mPtrFrameLayout;
    private PtrHandler mPtrHandler;

    public RefreshViewHelper(PtrFrameLayout layout) {
        this.mPtrFrameLayout = layout;
    }

    public RefreshViewHelper(PtrFrameLayout layout, PtrHandler handler) {
        this.mPtrFrameLayout = layout;
        this.mPtrHandler = handler;
    }

    @Override
    public void refreshComplete(boolean isSuccess) {
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void setOnRefreshListener(final SwipeRefreshLayout.OnRefreshListener listener) {
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
                if (mPtrHandler != null) {
                    return mPtrHandler.checkCanDoRefresh(ptrFrameLayout, view, view1);
                }
                return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view1);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (mPtrHandler != null) {
                    mPtrHandler.onRefreshBegin(ptrFrameLayout);
                }
                listener.onRefresh();
            }
        });
    }

    @Override
    public boolean isRefreshing() {
        return mPtrFrameLayout.isRefreshing();
    }
}
