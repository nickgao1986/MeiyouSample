package com.meetyou.chartview.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.meetyou.chartview.renderer.LineChartRenderer;
import com.meetyou.chartview.util.ChartUtils;

/**
 * Created by ckq on 6/24/16.
 */

public class AnimatedLineChartView extends LineChartView {

    private static final String TAG = "AnimatedLineChartView";

    public static final int ANIMATION_DURATION = 800;

    private float visibleViewPortRight;

    public AnimatedLineChartView(Context context) {
        super(context);
    }

    public AnimatedLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedLineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isEnabled()) {

            // draw animated area
            int chartRestoreCount = canvas.save();
            canvas.clipRect(chartComputator.getContentRectMinusAllMargins());
            chartRenderer.draw(canvas);
            canvas.restoreToCount(chartRestoreCount);

            chartRenderer.drawUnclipped(canvas);
            axesRenderer.drawInBackground(canvas);
            axesRenderer.drawInForeground(canvas);
        } else {
            canvas.drawColor(ChartUtils.DEFAULT_COLOR);
        }
    }

    public void animateArea() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "visibleViewPortRight", 0.0f, 1.0f);
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
    }

    public void setVisibleViewPortRight(float phase) {
        visibleViewPortRight = chartComputator.getContentRectMinusAllMargins().left + phase * chartComputator.getContentRectMinusAllMargins().width();
        ((LineChartRenderer) chartRenderer).setVisibleViewPortRight(visibleViewPortRight);
        invalidate();
    }

}
