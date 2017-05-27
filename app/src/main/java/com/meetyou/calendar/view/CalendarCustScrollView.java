package com.meetyou.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by gaoyoujian on 2017/5/23.
 */

public class CalendarCustScrollView extends ScrollView {
    private CalendarCustScrollViewListener scrollViewListener = null;

    public CalendarCustScrollView(Context context) {
        super(context);
    }

    public CalendarCustScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarCustScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public void setOnScrollChangedListener(CalendarCustScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public interface CalendarCustScrollViewListener {
        void onScrollChanged(CalendarCustScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    int lastX;
    int lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int tempX = x - lastX;

                int tempY = y - lastY;

                if (Math.abs(tempX) > Math.abs(tempY)) {
                    //如果ScrollView里面含有图表，
                    View chartview = this.findViewWithTag("chartview");
                    if (chartview != null && isTouchPointInView(chartview, x, y)) {
                        return false;
                    }
                }
                break;
        }
        lastX = x;
        lastY = y;

        return super.onInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    private boolean isTouchPointInView(View view, float x, float y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

}
