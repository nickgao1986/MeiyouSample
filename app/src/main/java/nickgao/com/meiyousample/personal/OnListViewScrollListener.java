package nickgao.com.meiyousample.personal;

import android.content.Context;
import android.widget.AbsListView;

import com.meetyou.crsdk.util.ImageLoader;


/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class OnListViewScrollListener implements AbsListView.OnScrollListener {
    private AbsListView.OnScrollListener externalListener = null;
    private Context mContext;
    private Object tag;

    public OnListViewScrollListener(Context context, AbsListView.OnScrollListener customListener) {
        this.externalListener = customListener;
        this.mContext = context;
    }

    /**
     *
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.externalListener != null) {
            this.externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    /**
     *
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
            case SCROLL_STATE_TOUCH_SCROLL:
                if (mContext != null && tag!=null ) ImageLoader.getInstance().resume(mContext, tag);
                break;
            case SCROLL_STATE_FLING:
                if (mContext != null && tag!=null )  ImageLoader.getInstance().pause(mContext, tag);
                break;

        }

        if (this.externalListener != null) {
            this.externalListener.onScrollStateChanged(view, scrollState);
        }

    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
