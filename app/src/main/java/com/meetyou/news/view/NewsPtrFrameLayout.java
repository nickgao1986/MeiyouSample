package com.meetyou.news.view;

import android.content.Context;
import android.util.AttributeSet;

import com.meetyou.pullrefresh.PullRefreshHeadView;
import com.meetyou.pullrefresh.lib.PtrFrameLayout;

/**
 * Created by gaoyoujian on 2017/5/25.
 */

public class NewsPtrFrameLayout extends PtrFrameLayout {

    public NewsPtrFrameLayout(Context context) {
        super(context);
        init();
    }

    public NewsPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewsPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        PullRefreshHeadView headView = new PullRefreshHeadView(getContext());
        setHeaderView(headView);
        addPtrUIHandler(headView);
        disableWhenHorizontalMove(true);
        setEnabledNextPtrAtOnce(true);
    }
}
