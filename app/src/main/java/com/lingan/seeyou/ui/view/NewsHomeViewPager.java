package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义的viewpager 主要是用于是否能左右滑动viewpager
 * Created by wuminjian on 17/2/20.
 */

public class NewsHomeViewPager extends ViewPager {
    private boolean scrollble = false;
    private float beforeX;

    public NewsHomeViewPager(Context context) {
        super(context);
    }

    public NewsHomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!scrollble) {
            return false;
        }
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
