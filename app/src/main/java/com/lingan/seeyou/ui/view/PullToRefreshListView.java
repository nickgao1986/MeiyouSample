package com.lingan.seeyou.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import nickgao.com.framework.utils.LogUtils;


public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView> {

    private boolean blsitviewTouchEnable=true;
    public void setListviewTouchEnable(boolean enable){
        blsitviewTouchEnable =enable;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);

        } catch (StackOverflowError ex) {
            ex.printStackTrace();
            LogUtils.d(TAG,"-->StackOverflowErrorQQQ");
            //FileStoreProxy.setGlobalValue("pull_refresh_crash_stackoverflow",true);
            //ex.getMessage().contains("StackOverFlow");
        } catch (Throwable ex){
            ex.printStackTrace();
            LogUtils.d(TAG,"-->Throwable");
            //FileStoreProxy.setGlobalValue("pull_refresh_crash_stackoverflow",true);
        }
    }
    @Override
    protected View createHeaderView(Context context) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    public class InternalListView extends ListView implements EmptyViewMethodAccessor {

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public InternalListView(Context context, AttributeSet attrs) {
            super(context, attrs);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD)
                setOverScrollMode(OVER_SCROLL_NEVER);

        }

        private IScrollerListener scrollViewListener = null;

        public void setScrollViewListener(IScrollerListener scrollViewListener) {
            this.scrollViewListener = scrollViewListener;
        }

        @Override
        protected void onScrollChanged(int x, int y, int oldx, int oldy) {
            super.onScrollChanged(x, y, oldx, oldy);
            //LogUtils.i("onScrollChanged  ");
            if (scrollViewListener != null) {
                scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);

            }
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            try {
                super.dispatchDraw(canvas);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }



        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {

            return !blsitviewTouchEnable  ? false : super.onInterceptTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            try {
//				if(!blsitviewTouchEnable){
//					return false;
//				}
                return this.getCount() > 0 ? super.onTouchEvent(ev) : true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshListView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

        public ContextMenuInfo getContextMenuInfo() {
            return super.getContextMenuInfo();
        }

    }

    public PullToRefreshListView(Context context) {
        super(context);
        this.setDisableScrollingWhileRefreshing(false);

    }

    public PullToRefreshListView(Context context, int mode) {
        super(context, mode);
        this.setDisableScrollingWhileRefreshing(false);

    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDisableScrollingWhileRefreshing(false);

    }

    @Override
    public boolean isSetBackgroud() {
        return false;
    }


    @Override
    public ContextMenuInfo getContextMenuInfo() {
        return ((InternalListView) getRefreshableView()).getContextMenuInfo();
    }



    private InternalListView lv;
    @Override
    protected final ListView createRefreshableView(Context context,
                                                   AttributeSet attrs) {
        lv = new InternalListView(context, attrs);

        lv.setId(android.R.id.list);
        return lv;
    }

    private boolean isSetBackgroud = false;

    public void setSetBackgroud(boolean isSetBackgroud) {
        this.isSetBackgroud = isSetBackgroud;
    }

    public void destory(){
        try{

            lv=null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
