package com.com.meetyou.news;

import android.content.Context;
import android.view.View;

import com.com.meetyou.news.model.ILoadStateHelper;
import com.lingan.seeyou.ui.view.LoadingView;
import com.meetyou.crsdk.util.NetWorkStatusUtil;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.skin.ToastUtils;


/**
 * 加载状态帮助类
 * Created by LinXin on 2017/2/24.
 */
public class LoadStateHelper implements ILoadStateHelper {

    private View mContentView;
    private LoadingView mLoadingView;

    public LoadStateHelper(View contentView, LoadingView loadingView) {
        this.mContentView = contentView;
        this.mLoadingView = loadingView;
    }

    @Override
    public void showContent() {
        mContentView.setVisibility(View.VISIBLE);
        mLoadingView.setStatus(LoadingView.STATUS_HIDDEN);
    }

    @Override
    public void showLoading(boolean isFirstLoad) {
        if(isFirstLoad) {
            mContentView.setVisibility(View.INVISIBLE);
            mLoadingView.setStatus(LoadingView.STATUS_LOADING);
        }
    }

    @Override
    public void showEmpty() {
        mContentView.setVisibility(View.INVISIBLE);
        mLoadingView.setStatus(LoadingView.STATUS_NODATA);
    }

    @Override
    public void showError(boolean isEmpty, Throwable t) {
        Context context = mContentView.getContext();
        if (isEmpty) {
            mContentView.setVisibility(View.INVISIBLE);
            if (NetWorkStatusUtil.queryNetWork(context)) {
                mLoadingView.setStatus(LoadingView.STATUS_NODATA);
            } else {
                mLoadingView.setStatus(LoadingView.STATUS_NONETWORK);
            }
        } else {
            ToastUtils.showToast(context, R.string.no_internetbroken);
        }
    }

    @Override
    public void setReloadListener(final OnReloadListener listener) {
        mLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadingView.getStatus() == LoadingView.STATUS_NONETWORK
                        || mLoadingView.getStatus() == LoadingView.STATUS_NODATA) {
                    listener.onReLoad();
                }
            }
        });
    }
}
