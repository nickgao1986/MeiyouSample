package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import nickgao.com.meiyousample.R;


/**
 * 某个text的某个文字的点击事件
 * Created by Administrator on 2015/7/29.
 */
public class MyURLSpan extends ClickableSpan {
    private Context context;
    private String mUrl;
    private int mColorId;
    private int mBlockId;

    public MyURLSpan(Context context, String url, int blockId, int urlColorId) {
        this.context = context;
        this.mUrl = url;
        this.mBlockId = blockId;
        this.mColorId = urlColorId;
    }
    @Override
    public void onClick(View widget) {
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
         if (mColorId > 0) {
            //ds.setColor(SkinEngine.getInstance().getResouceColor(context, urlColorId));
             ds.setColor(context.getResources().getColor(mColorId));
        } else {
             ds.setColor(context.getResources().getColor(R.color.red_a));
            //ds.setColor(SkinEngine.getInstance().getResouceColor(context, com.lingan.seeyou.util.R.color.red_a));
        }
        ds.bgColor = Color.TRANSPARENT;
        ds.setUnderlineText(false);
    }
}
