package com.com.meetyou.news;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AbsListView;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.adapter.BaseQuickAdapter;


/**
 * RecyclerView帮助类基类
 * Created by LinXin on 2016/11/1 11:08.
 */
public class RecyclerViewHelper implements IListViewHelper {

    private FooterViewHelper mFooterViewHelper;
    private BaseQuickAdapter mAdapter;
    private int mCurrentScrollState = -1;
    private OnLoadMoreListener mLoadMoreListener;

    public RecyclerViewHelper(RecyclerView recyclerView) {
        mAdapter = (BaseQuickAdapter) recyclerView.getAdapter();
        mFooterViewHelper = new FooterViewHelper(recyclerView);
        mAdapter.addFooterView(mFooterViewHelper.getFooterView());
        mFooterViewHelper.showIdle();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LogUtils.d("====newState="+newState);
                mCurrentScrollState = newState;
                ifNeedLoadMore(recyclerView);
            }
        });
    }

    /**
     * Description 是否需要自动加载更多
     */
    private void ifNeedLoadMore(RecyclerView recyclerView) {
        boolean isNeedLoadMore;
        int lastVisiblePosition = getLastVisibleItem(recyclerView);
        final int count = recyclerView.getAdapter().getItemCount();// 列表中子项总数
        isNeedLoadMore = mCurrentScrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastVisiblePosition == count - 1;
        LogUtils.d("====lastVisiblePosition="+lastVisiblePosition+"count="+count+"isNeedLoadMore="+isNeedLoadMore);
        LogUtils.d("===mFooterViewHelper.isIdle()="+mFooterViewHelper.isIdle()+"state="+mFooterViewHelper.getState());
        if (isNeedLoadMore && mAdapter.getItemCount() != 0 && mFooterViewHelper.isIdle() && mLoadMoreListener != null) {//正在加载中，无更多，加载失败三种状态均不自动执行该动作
            LogUtils.d("====showloadmore");
            showLoadMoreLoading();
            mLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public void showLoadMoreError() {
        mFooterViewHelper.showError();
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    @Override
    public void setOnReLoadMoreListener(OnReloadListener listener) {
        mFooterViewHelper.setOnReloadListener(listener);
    }

    @Override
    public boolean isLoadingMore() {
        return mFooterViewHelper.isLoadingMore();
    }

    @Override
    public void notifyAdapter(int oldCount, int newAddDataSize) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadMoreIdle() {
        mFooterViewHelper.showIdle();
    }

    @Override
    public void showLoadMoreLoading() {
        mFooterViewHelper.showLoading();
    }

    @Override
    public void showLoadMoreNoMore() {
        if (!mFooterViewHelper.isGoneWhenNoMore()) {
            mFooterViewHelper.getFooterView().setVisibility(View.VISIBLE);
        } else {
            mFooterViewHelper.getFooterView().setVisibility(View.GONE);
        }
        mFooterViewHelper.showNoMore();
    }

    /**
     * 显示或者隐藏FooterView
     *
     * @param isShow
     */
    public void showOrHideFooterView(boolean isShow) {
        if (isShow) {
            mFooterViewHelper.getFooterView().setVisibility(View.VISIBLE);
        } else {
            mFooterViewHelper.getFooterView().setVisibility(View.GONE);
        }
    }

    /**
     * 获取最后一项的位置
     */
    private static int getLastVisibleItem(RecyclerView recyclerView) {
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        int lastVisiblePosition = 0;
        if (lm instanceof LinearLayoutManager) {
            lastVisiblePosition = ((LinearLayoutManager) lm).findLastVisibleItemPosition();
        } else if (lm instanceof StaggeredGridLayoutManager) {
            int[] positions = ((StaggeredGridLayoutManager) lm).findLastVisibleItemPositions(null);
            lastVisiblePosition = getMax(positions);
        }
        return lastVisiblePosition;
    }

    /**
     * 取出数组中的最大值
     *
     * @param arr
     * @return
     */
    private static int getMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
}
