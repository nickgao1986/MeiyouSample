package com.lingan.seeyou.ui.view;

import android.widget.ImageView;

/**
 * Created by gaoyoujian on 2017/3/8.
 */

public class ImageLoadParams {
    public int defaultholder;
    public int failholder;
    public int retryholder;
    public int bgholder;
    public int roundBgColor;
    public int width;
    public int height;
    public int radius;
    public boolean forbidenModifyUrl;
    public int[] radiusArray;
    public ImageView.ScaleType scaleType;
    public boolean round;
    public int loadMode = 0;
    public static final int LOAD_MODE_NORMAL = 0;
    public boolean enableRetry;
    public boolean anim = false;
    public boolean isFade = true;
    public Object tag;

    public ImageLoadParams() {
    }
}

