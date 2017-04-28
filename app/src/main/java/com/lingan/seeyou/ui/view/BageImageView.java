package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fresco.view.ImageLoadParams;
import fresco.view.ImageLoader;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/4/28.
 */

public class BageImageView extends RelativeLayout {

    private Context context;
    private LoaderImageView loaderImageView;
    private TextView textView;

    private boolean shouldDisplayBage = false;

    public BageImageView(Context context) {
        super(context);
        init(context);
    }

    public BageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BageImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int space = DeviceUtils.dip2px(context, 5);
        this.context = context;
        //一个图片控件
        loaderImageView = new LoaderImageView(context);
        RelativeLayout.LayoutParams lpLoaderImageView = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        loaderImageView.setLayoutParams(lpLoaderImageView);

        //一个textview
        textView = new TextView(context);
        LayoutParams lpTextView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpTextView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpTextView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpTextView.rightMargin = space;
        lpTextView.bottomMargin = space;
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(10);
        textView.setTextColor(context.getResources().getColor(R.color.white_a));
        textView.setPadding(space, 0, space, DeviceUtils.dip2px(context, 1));
        textView.setLayoutParams(lpTextView);


        addView(loaderImageView);
        addView(textView);
    }


    public void setShouldDisplayBage(boolean shouldDisplayBage) {
        this.shouldDisplayBage = shouldDisplayBage;
    }

    /**
     * 显示图片
     */
    public void displayImage(String imageUrl, ImageLoadParams imageLoadParams, String video_time) {
        ImageLoader.getInstance().displayImage(context, loaderImageView, imageUrl, imageLoadParams, null);
        textView.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(video_time)) {//视频角标
            textView.setText(video_time);
            textView.setCompoundDrawablePadding(2);
            textView.setVisibility(View.VISIBLE);

            textView.setBackgroundResource(R.drawable.btn_black_transparent_normal);
        }
    }

}

