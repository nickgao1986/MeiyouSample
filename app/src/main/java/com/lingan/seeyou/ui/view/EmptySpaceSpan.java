package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;

/**
 * Created by gaoyoujian on 2017/4/28.
 */

public class EmptySpaceSpan extends ReplacementSpan {
    private int mWidth;
    public EmptySpaceSpan(Context context, int widthDpValue){
        this.mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDpValue, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return mWidth;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

    }
}
