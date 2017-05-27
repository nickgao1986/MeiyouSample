package com.com.meetyou.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.lingan.seeyou.ui.view.ListFooterUtil;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import nickgao.com.meiyousample.R;


/**
 * 底部加载更多帮助类
 * Created by LinXin on 2016/6/27 14:38.
 */
class FooterViewHelper implements IFooterViewHelper, View.OnClickListener {

    private View mFooterView;
    private OnReloadListener mReloadListener;
    private LoadMoreState state = LoadMoreState.IDLE;
    private ListFooterUtil listFooterUtil;
    private String emptyText;

    FooterViewHelper(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        listFooterUtil = ListFooterUtil.getInstance();
        mFooterView = ViewFactory.from(context).getLayoutInflater().inflate(R.layout.layout_list_footer_load_item, recyclerView, false);
        mFooterView.setOnClickListener(this);
        emptyText = context.getString(R.string.load_no_more);
    }

    FooterViewHelper(ListView listView) {
        Context context = listView.getContext();
        listFooterUtil = ListFooterUtil.getInstance();
        mFooterView = listFooterUtil.getListViewFooter(ViewFactory.from(context).getLayoutInflater());
        mFooterView.setOnClickListener(this);
        emptyText = context.getString(R.string.load_no_more);
    }

    void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    @Override
    public View getFooterView() {
        return mFooterView;
    }

    @Override
    public void setOnReloadListener(OnReloadListener listener) {
        mReloadListener = listener;
    }

    @Override
    public void showIdle() {
        this.state = LoadMoreState.IDLE;
        listFooterUtil.updateListViewFooter(mFooterView, ListFooterUtil.ListViewFooterState.NORMAL, "");
    }

    @Override
    public void showLoading() {
        this.state = LoadMoreState.LOADING;
        listFooterUtil.updateListViewFooter(mFooterView, ListFooterUtil.ListViewFooterState.LOADING, "");
    }

    @Override
    public void showNoMore() {
        this.state = LoadMoreState.NO_MORE;
        listFooterUtil.updateListViewFooter(mFooterView, ListFooterUtil.ListViewFooterState.COMPLETE, emptyText);
    }

    @Override
    public void showError() {
        this.state = LoadMoreState.ERROR;
        listFooterUtil.updateListViewFooter(mFooterView, ListFooterUtil.ListViewFooterState.ERROR, "");
    }

    @Override
    public boolean isIdle() {
        return state == LoadMoreState.IDLE;
    }

    @Override
    public boolean isLoadingMore() {
        return state == LoadMoreState.LOADING;
    }

    @Override
    public boolean isGoneWhenNoMore() {
        return mFooterView.getVisibility() == View.GONE;
    }

    @Override
    public void onClick(View v) {
        if (state == LoadMoreState.ERROR && mReloadListener != null) {
            showLoading();
            mReloadListener.onReLoad();
        }
    }
}
