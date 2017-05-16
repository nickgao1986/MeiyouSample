package com.lingan.seeyou.ui.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;


/**
 * 绘制心形曲线
 * * -keep com.meiyou.framework.ui.views.HeartView  { *; }
 * Created by hxd on 16/1/14.
 */
public class HeartView extends View {
    private static final String sTAG = "HeartView";
    private Path mPath;
    private Paint mPaint;
    private float mLength;
    private float mLastPhase;
    float scale = 0.6f * DeviceUtils.dip2px(getContext(), 1);
    float tmpPhase = 0f;
    Boolean flags = false;//loading 模式
    static final int ANIMATION_FPS = 1000 / 60;
    PathMeasure measure;
    Path mSegmentPath;
    Paint mSegmentPaint;
    float mSegmentLength = 0.1f;

    boolean layout = false;

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initPaint();
        fillPath();
        mSegmentPath = new Path();
        initSegmentPaint();

    }


    private void fillPath() {
        mPath = new Path();
        mPath.moveTo(33.705f * scale, 6.15f * scale);
        mPath.rCubicTo(-5.243f * scale, 0f * scale, -8.429f * scale, 2.622f * scale, -10.204f * scale, 4.96f * scale);
        mPath.cubicTo(21.726f * scale, 8.772f * scale, 18.542f * scale, 6f * scale, 13.299f * scale, 6f * scale);
        mPath.rCubicTo(-0.869f * scale, 0f * scale, -1.774f * scale, 0.153f * scale, -2.693f * scale, 0.308f * scale);
        mPath.cubicTo(7.058f * scale, 6.752f * scale, 1.5f * scale, 10.52f * scale, 1.51f * scale, 18.724f * scale);
        mPath.rCubicTo(0f * scale, 3.89f * scale, 2.49f * scale, 9.499f * scale, 6.196f * scale, 13.926f * scale);
        mPath.rCubicTo(4.558f * scale, 5.443f * scale, 10.167f * scale, 8.45f * scale, 15.794f * scale, 8.45f * scale);
        mPath.rCubicTo(12.484f * scale, 0f * scale, 21.992f * scale, -14.892f * scale, 21.992f * scale, -22.346f * scale);
        mPath.cubicTo(45.503f * scale, 10.553f * scale, 39.946f * scale, 6.825f * scale, 36.4f * scale, 6.38f * scale);
        mPath.cubicTo(35.481f * scale, 6.226f * scale, 34.575f * scale, 6.15f * scale, 33.705f * scale, 6.15f * scale);

        measure = new PathMeasure(mPath, false);
        mLength = measure.getLength();
    }

    private void initSegmentPaint() {
        mSegmentPaint = new Paint();
        mSegmentPaint.setColor(getResources().getColor(R.color.black_A));
        mSegmentPaint.setAntiAlias(true);
        mSegmentPaint.setStyle(Paint.Style.STROKE);
        mSegmentPaint.setStrokeWidth(6.0f);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.red_b));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4.0f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!layout) {
            layout = true;
            int width = this.getWidth();
            scale = width * 1.0f / 50f;
            fillPath();
        }
    }

    /**
     * 绘制白色轨迹
     *
     * @param phase
     */
    private void setOtherPos(float phase) {

        float next = phase + mSegmentLength >= 1.0 ? 1.0f : (phase + mSegmentLength);

        /**
         * <p>On {@link android.os.Build.VERSION_CODES#KITKAT} and earlier
         * releases, the resulting path may not display on a hardware-accelerated
         * Canvas. A simple workaround is to add a single operation to this path,
         * such as <code>dst.rLineTo(0, 0)</code>.</p>
         */
        Path tmpPath = new Path();
        measure.getSegment(phase * mLength, next * mLength, tmpPath, true);
        tmpPath.rLineTo(0, 0);//must be
        mSegmentPath = tmpPath;
        invalidate();
    }

    /**
     * 开始loading 循环
     */
    public void start() {
        flags = true;
        tmpPhase = 0f;
        startMsg();
    }

    public void reset() {
        mLastPhase = 0;
        flags = false;
        tmpPhase = 0f;
        mPaint.setPathEffect(createPathEffect(mLength, mLastPhase, 0.0f));
        invalidate();
    }

    /**
     * 按照路径百分比绘制
     *
     * @param pathNormalize
     */
    public void drawingPath(float pathNormalize) {
        setPhase(pathNormalize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath, mPaint);

        if (flags) {
            canvas.drawPath(mSegmentPath, mSegmentPaint);
        }

    }

    //is called by animtor object
    public void setPhase(float phase) {
        //防抖动 防无效数据
        float normal = (float) (Math.round(100 * phase)) / 100;
        if (normal >= 0.95) {
            normal = 1f;
        }

        if (normal <= 1.0f && normal >= mLastPhase) {
            mLastPhase = normal;
            LogUtils.d(sTAG, "normal, mLastPhase " + normal + "," + mLastPhase);
            mPaint.setPathEffect(createPathEffect(mLength, mLastPhase, 0.0f));
            invalidate();
        }
    }

    private PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[]{phase * pathLength, pathLength}, 0);
        /*
        if (!flags) {
            return new DashPathEffect(new float[]{phase * pathLength, pathLength}, 0);
        } else {
            return new DashPathEffect(new float[]{pathLength, 0.1f * pathLength}, 1 - phase * pathLength);
        }*/

    }

    public void startMsg() {
        if (!flags) {
            return;
        }
        tmpPhase = (tmpPhase + 0.01f) % 0.9999f;

        Handler handler = getHandler();
        if (handler != null) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (flags) {
                        setOtherPos(tmpPhase);
                        startMsg();
                    }
                }
            };
            handler.postDelayed(runnable, ANIMATION_FPS);
        }
    }


}
