package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import nickgao.com.meiyousample.utils.EmojiConversionUtil;

/**
 * Created by gaoyoujian on 2017/5/17.
 */

public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setText(String str) {
        try {
            super.setText(EmojiConversionUtil.getInstace().getExpressionString(getContext(), str,0,0));
        } catch (ArrayIndexOutOfBoundsException e) {
            super.setText(getText().toString());
        }
    }

    @Override
    public void setGravity(int gravity) {
        try {
            super.setGravity(gravity);
        } catch (ArrayIndexOutOfBoundsException e) {
            super.setText(getText().toString());
            super.setGravity(gravity);
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (ArrayIndexOutOfBoundsException e) {
            super.setText(getText().toString());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            e.printStackTrace();
        }
    }
}
