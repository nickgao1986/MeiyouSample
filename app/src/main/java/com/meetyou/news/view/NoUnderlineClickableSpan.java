package com.meetyou.news.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by huangyuxiang on 2016/11/22.
 */

public abstract class NoUnderlineClickableSpan extends ClickableSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}
