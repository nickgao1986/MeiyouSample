package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;

import fresco.view.FrescoImageView;

/**
 * Created by gaoyoujian on 2017/4/26.
 */

public class LoaderImageView extends FrescoImageView {
    public LoaderImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LoaderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoaderImageView(Context context) {
        super(context);
    }

    public LoaderImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }
}
