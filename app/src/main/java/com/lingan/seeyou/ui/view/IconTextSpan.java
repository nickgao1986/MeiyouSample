package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/28.
 */

public class IconTextSpan extends ReplacementSpan {
    private static final String TAG = "IconTextSpan";
    private Context mContext;
    private int mBgColorResId;
    private String mText;
    private float mBgHeight;
    private float mBgWidth;
    private float mRadius;
    private float mRightMargin;
    private float mTextSize;
    private int mTextColorResId;

    public IconTextSpan(Context context, int bgColorResId, String text){
        if(StringUtils.isNull(text)){
            return;
        }
        initDefaultValue(context, bgColorResId, text);
        //计算背景的宽度
        this.mBgWidth = caculateBgWidth(text);
    }

    /**
     * 计算icon背景宽度
     */
    private float caculateBgWidth(String text){
        if(text.length() > 1){
            //多字
            Rect textRect = new Rect();
            Paint paint = new Paint();
            paint.setTextSize(mTextSize);
            paint.getTextBounds(text, 0, text.length(), textRect);
            float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics());
            return textRect.width() + padding * 2;
        }else{
            //单字，宽与高一致
            return mBgHeight;
        }
    }

    /**
     * 初始化基础
     * @param context
     */
    private void initDefaultValue(Context context, int bgColorResId, String text){
        this.mContext = context.getApplicationContext();
        this.mBgColorResId = bgColorResId;
        this.mText = text;
        this.mBgHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17f, mContext.getResources().getDisplayMetrics());
        this.mRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mContext.getResources().getDisplayMetrics());
        this.mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mContext.getResources().getDisplayMetrics());
        this.mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, mContext.getResources().getDisplayMetrics());
        this.mTextColorResId = R.color.white_a;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) (mBgWidth + mRightMargin);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        //画背景
        Paint bgPaint = new Paint();
        bgPaint.setColor(mContext.getResources().getColor(mBgColorResId));
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float bgStartY = metrics.ascent - metrics.top + ((metrics.descent - metrics.ascent) - mBgHeight) / 2; //算出背景开始画的y坐标
        if(bgStartY < 0){
            bgStartY = 0;
        }
        RectF bgRect = new RectF(x, bgStartY, x + mBgWidth, bgStartY + mBgHeight);
        canvas.drawRoundRect(bgRect, mRadius, mRadius, bgPaint);
        //把字画在背景中间
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(mContext.getResources().getColor(mTextColorResId));
        textPaint.setTextSize(mTextSize);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        canvas.drawText(mText, x + mBgWidth / 2, bgStartY + ((mBgHeight - textHeight) / 2 - fontMetrics.top), textPaint);
    }

    /**
     * 设置TextSize
     * @param textSize
     * @param refreshBgWidth 是否重新计算背景宽度
     */
    public void setTextSizeSpValue(float textSize, boolean refreshBgWidth) {
        this.mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, mContext.getResources().getDisplayMetrics());
        if(refreshBgWidth){
            this.mBgWidth = caculateBgWidth(mText);
        }
    }

    public void setBgHeightDpValue(int bgHeightDpValue){
        this.mBgHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bgHeightDpValue, mContext.getResources().getDisplayMetrics());
    }

    public void setBgWidthDpValue(int bgWidthDpValue){
        this.mBgWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bgWidthDpValue, mContext.getResources().getDisplayMetrics());
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public void setRightMarginDpValue(int rightMarginDpValue){
        this.mRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightMarginDpValue, mContext.getResources().getDisplayMetrics());
    }

    public void setTextColorResId(int textColorResId) {
        this.mTextColorResId = textColorResId;
    }
}
