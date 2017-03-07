package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import java.lang.ref.WeakReference;

/**
 * 自定义图片文字混排的
 * Created by Administrator on 2015/7/23.
 */
public class CustomImageSpan extends ImageSpan {
    private static final char[] ELLIPSIS_NORMAL = {'\u2026'};
    private static final char[] ELLIPSIS_TWO_DOTS = {'\u2025'};

    public CustomImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public CustomImageSpan(Context context, Bitmap resourceId) {
        super(context, resourceId);
    }

    public CustomImageSpan(Drawable d) {
        super(d);
    }

    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end,
                       Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable(paint);
        Rect rect = d.getBounds();

        if (fm != null) {
            Paint.FontMetricsInt fontMetrics = new Paint.FontMetricsInt();
            paint.getFontMetricsInt(fontMetrics);

            fm.ascent = fontMetrics.ascent;
            fm.descent = fontMetrics.descent;

            fm.top = fontMetrics.top;
            fm.bottom = fontMetrics.bottom;
        }
        return rect.right + 10;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        final String s = text.toString();
        String subS = s.substring(start, end);
        if (ELLIPSIS_NORMAL[0] == subS.charAt(0) || ELLIPSIS_TWO_DOTS[0] == subS.charAt(0)) {
            canvas.save();
            canvas.drawText(subS, x, y, paint);
            canvas.restore();
        } else {
            Drawable d = getCachedDrawable(paint);
            canvas.save();
            int transY;
            Paint.FontMetricsInt fontMetrics = new Paint.FontMetricsInt();
            paint.getFontMetricsInt(fontMetrics);
            transY = y + fontMetrics.ascent;
            canvas.translate(x + 10, transY);
            d.draw(canvas);
            canvas.restore();
        }
    }

    private Drawable getCachedDrawable(Paint paint) {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            d.setBounds(new Rect(0, 0, paint.getFontMetricsInt(null), paint.getFontMetricsInt(null)));
            mDrawableRef = new WeakReference<Drawable>(d);
        }

        return d;
    }

    private WeakReference<Drawable> mDrawableRef;
    /*
    public CustomImageSpan(Drawable drawable) {
        super(drawable);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;
            fontMetricsInt.ascent = -bottom;
            fontMetricsInt.top = -bottom;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        int transY = 0;
        transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }*/
}
