package com.lingan.seeyou.ui.view.photo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ClipView extends View {
    private Context context;
    private double zoomValue;
    private Paint paint;
    private int width;
    private int height;

    public ClipView(Context context) {
        super(context);
        this.context = context;
        intValue();
    }

    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        intValue();
    }

    public ClipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        intValue();
    }

    public void intValue() {
        paint = new Paint();
        paint.setColor(0xaa000000);
    }

    public void setZoomValue(double zoomValue) {
        this.zoomValue = zoomValue;
        invalidate();
    }

    /**
     * 绘制4个长方形  剩下中间的就是 空白的区域 也是要截取的区域
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = this.getWidth();
        height = this.getHeight();
        int viewWidth = (int) (width * zoomValue);
        int topY = (height - viewWidth) / 2;
        int leftX = (width - viewWidth) / 2;
        // left
        canvas.drawRect(0, topY, leftX, topY + viewWidth, paint);
        // top
        canvas.drawRect(0, 0, width, topY, paint);
        // right
        canvas.drawRect(leftX + viewWidth, topY, width, topY + viewWidth, paint);
        // bottom
        canvas.drawRect(0, topY + viewWidth, width, height, paint);
    }
}
