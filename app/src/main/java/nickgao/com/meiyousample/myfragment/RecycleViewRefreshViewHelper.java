package nickgao.com.meiyousample.myfragment;

import android.support.v4.widget.SwipeRefreshLayout;

import com.com.meetyou.news.model.IRefreshViewHelper;

/**
 * Created by gaoyoujian on 2017/6/3.
 */

public class RecycleViewRefreshViewHelper implements IRefreshViewHelper {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public RecycleViewRefreshViewHelper(SwipeRefreshLayout layout) {
        this.mSwipeRefreshLayout = layout;
    }


    @Override
    public void refreshComplete(boolean isSuccess) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setOnRefreshListener(final SwipeRefreshLayout.OnRefreshListener listener) {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onRefresh();
            }
        });
    }

    @Override
    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }
}
