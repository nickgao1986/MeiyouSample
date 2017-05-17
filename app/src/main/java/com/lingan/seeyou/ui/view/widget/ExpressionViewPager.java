/**
 * ****************************************************************************
 * Copyright (c) 2013, 2015 linggan.com
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * *****************************************************************************
 */
package com.lingan.seeyou.ui.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 重写表情ViewPager
 * Created by YiRong on 2015/7/28.
 */
public class ExpressionViewPager extends ViewPager {

    private boolean willIntercept = true;

    public ExpressionViewPager(Context context) {
        super(context);
    }

    public ExpressionViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (willIntercept) {
            // 这个地方直接返回true会很卡
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }

    /**
     * 设置ViewPager是否拦截点击事件
     *
     * @param value true则ViewPager拦截点击事件；
     *              false则ViewPager将不能滑动，ViewPager的子View可以获得点击事件 主要受影响的点击事件为横向滑动
     */
    public void setTouchIntercept(boolean value) {
        willIntercept = value;
    }
}

