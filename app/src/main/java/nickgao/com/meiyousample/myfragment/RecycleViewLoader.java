package nickgao.com.meiyousample.myfragment;

import android.support.v4.widget.SwipeRefreshLayout;

import com.com.meetyou.news.IListViewHelper;
import com.com.meetyou.news.OnLoadMoreListener;
import com.com.meetyou.news.OnReloadListener;
import com.com.meetyou.news.RecyclerViewHelper;
import com.com.meetyou.news.model.ILoadStateHelper;
import com.com.meetyou.news.model.IRefreshViewHelper;
import com.com.meetyou.news.model.OnLoadFailureListener;
import com.com.meetyou.news.model.OnLoadListener;

import nickgao.com.framework.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/6/2.
 */

public class RecycleViewLoader<Response,ListItem> implements SwipeRefreshLayout.OnRefreshListener, OnReloadListener,OnLoadMoreListener {

    ILoadStateHelper mLoadStateViewHelper;  //显示什么视图，是显示空视图，还是error视图
    IRefreshViewHelper mRefreshViewHelper;  //刷新控件,看看是否在刷新中，还是刷新完成了
    OnLoadListSuccessListener<ListItem> onLoadSuccessListener;  //成功监听
    OnLoadFailureListener onLoadFailureListener; //失败监听
    boolean isSilenceRefresh;//是否手动刷新
    private IRecycleViewModel<Response,ListItem> model;  //数据模型
    private IRecycleViewLoaderView view;    //给activity设置dataLoader
    private RecyclerViewHelper mRecyclerViewHelper;
    private IListViewHelper mListViewHelper;


    public RecycleViewLoader(IRecycleViewLoaderView view, IRecycleViewModel<Response,ListItem> model) {
        this.view = view;
        this.model = model;
        this.view.setDataLoader(this);
    }

    public void setLoadStateHelper(ILoadStateHelper helper) {
        this.mLoadStateViewHelper = helper;
        this.mLoadStateViewHelper.setReloadListener(this);
    }

    public void setRefreshViewHelper(IRefreshViewHelper helper) {
        this.mRefreshViewHelper = helper;
        mRefreshViewHelper.setOnRefreshListener(this);
    }

    public void setOnLoadSuccessListener(OnLoadListSuccessListener<ListItem> listener) {
        this.onLoadSuccessListener = listener;
    }

    public void setOnLoadFailureListener(OnLoadFailureListener listener) {
        this.onLoadFailureListener = listener;
    }

    public void setListViewHelper(IListViewHelper helper) {
        this.mListViewHelper = helper;
        this.mListViewHelper.setOnLoadMoreListener(this);
        this.mListViewHelper.setOnReLoadMoreListener(this);
    }

    /**
     * 是否手动刷新
     *
     * @return
     */
    public boolean isSilenceRefresh() {
        return isSilenceRefresh;
    }

    /**
     * 重置手动刷新为false
     */
    public void setSilenceRefresh(boolean isManualRefresh) {
        this.isSilenceRefresh = isManualRefresh;
    }

    public void load(final boolean isFirstLoad) {
        cancel();
        model.load(new OnLoadListener<Response>() {
            @Override
            public void onStart() {
                showLoading(isFirstLoad);
            }

            @Override
            public void onSuccess(Response response) {
                LogUtils.d("======showcontent response="+response);
                showContent(response);
                isSilenceRefresh = false;
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                showError(throwable);
                isSilenceRefresh = false;
            }
        });
    }

    /**
     * 静默刷新
     */
    public void silenceRefresh() {
        isSilenceRefresh = true;
        onRefresh();
    }

    @Override
    public void onRefresh() {
        model.preRefresh();
        load(false);
    }

    @Override
    public void onReLoad() {
        model.preReLoad();
        load(false);
    }

    @Override
    public void onLoadMore() {
        model.loadMore();
        load(false);
    }

    /**
     * 取消加载
     */
    public void cancel() {
        model.cancel();
    }

    /**
     * 显示加载状态
     */
    private void showLoading(boolean isFirstLoad) {
        if (isRefreshing())
            return;
        if (mLoadStateViewHelper == null)
            return;
        mLoadStateViewHelper.showLoading(isFirstLoad);
    }


    /**
     * 是否在刷新中
     *
     * @return true:是，false:否
     */
    protected boolean isRefreshing() {
        return (mRefreshViewHelper != null && mRefreshViewHelper.isRefreshing()) || isSilenceRefresh;
    }

    /**
     * 显示内容
     */
    private void showContent(Response response) {
        boolean isRefresh = isRefreshing();
        if (isRefresh) {
            if (mRefreshViewHelper != null) {
                mRefreshViewHelper.refreshComplete(true);
            }
        }
        model.setData(isRefresh, response);
        setLoadMoreState(response);

        if (mLoadStateViewHelper != null) {
            if (model.isEmpty()) {
                mLoadStateViewHelper.showEmpty();
            } else {
                mLoadStateViewHelper.showContent();
            }
        }
        if (onLoadSuccessListener != null) {
            onLoadSuccessListener.onSuccess(isRefreshing(), (ListItem) model);
        }
    }

    /**
     * 设置加载更多状态
     */
    private void setLoadMoreState(Response response) {
        if (mListViewHelper == null)
            return;
        if (model.hasNext()) {
            mListViewHelper.showLoadMoreIdle();
        } else {
            mListViewHelper.showLoadMoreNoMore();
        }
    }

    /**
     * 显示错误
     */
    private void showError(Throwable t) {
        if (isRefreshing()) {
            if (mRefreshViewHelper != null) {
                mRefreshViewHelper.refreshComplete(false);
            }
        } else if (mLoadStateViewHelper != null) {
            //界面为空
            mLoadStateViewHelper.showError(model.isEmpty(), t);
        }
        if (onLoadFailureListener != null) {
            onLoadFailureListener.onFailure(isRefreshing(), t);
        }
    }

    public void onDestroy() {
        cancel();
        view = null;
        mLoadStateViewHelper = null;
        mRefreshViewHelper = null;
    }


}
