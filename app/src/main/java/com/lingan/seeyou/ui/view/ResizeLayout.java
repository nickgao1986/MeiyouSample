package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/3/5.
 */

public class ResizeLayout extends RelativeLayout {

    private static int count = 0;
    private ResizeLayout.OnKeyboardListener mListener;

    public ResizeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(this.mListener != null) {
            if(Math.abs(h - oldh) < DeviceUtils.getScreenHeight(this.getContext()) / 5) {
                return;
            }

            if(h < oldh) {
                this.mListener.onKeyboardShow();
            } else {
                this.mListener.onKeyboardHide();
            }
        }

    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnKeyboardListener(ResizeLayout.OnKeyboardListener listener) {
        this.mListener = listener;
    }

    public interface OnKeyboardListener {
        void onKeyboardHide();

        void onKeyboardShow();
    }
}
