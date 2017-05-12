package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meetyou.crsdk.util.ImageLoader;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.firstPage.NewsType;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.StringUtils;
import nickgao.com.okhttpexample.view.ImageLoadParams;
import nickgao.com.okhttpexample.view.LoaderImageView;


/**
 * relativeLayout包含一个图片和图片上面有一个角标
 * Created by wuminjian on 2016/8/10.
 */
public class BadgeRelativeLaoutView extends RelativeLayout {

    private Context context;
    public FrameLayout container;
    private LoaderImageView loaderImageView;
    private TextView textView;
    private ImageView imageView;//显示在中间的位置 比如视频

    public BadgeRelativeLaoutView(Context context) {
        super(context);
        init(context);
    }

    public BadgeRelativeLaoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BadgeRelativeLaoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int space = DeviceUtils.dip2px(context, 5);
        this.context = context;
        //一个图片控件
        loaderImageView = new LoaderImageView(context);
        LayoutParams lpLoaderImageView = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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

        //中间显示控件
        imageView = new ImageView(context);
        LayoutParams lpImageView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpImageView.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(lpImageView);
        imageView.setBackgroundResource(R.drawable.apk_video_selector);
        imageView.setVisibility(View.GONE);

        addView(loaderImageView);
        addView(textView);
        addView(imageView);
    }

    /**
     * 显示图片
     */
    public void displayImage(String imageUrl, ImageLoadParams imageLoadParams, MultiImageView.DisplayImageModel displayImageModel) {
        displayImage(imageUrl, imageLoadParams, displayImageModel, true);
    }

    /**
     * 显示图片
     */
    public void displayImage(String imageUrl, ImageLoadParams imageLoadParams, MultiImageView.DisplayImageModel displayImageModel, boolean isShowVideoPlayButton) {
        ImageLoader.getInstance().displayImage(context, loaderImageView, imageUrl, imageLoadParams, null);
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        //设置角标
        if (displayImageModel == null || (!displayImageModel.isShowNewsTopic))
            return;
        if (!StringUtils.isNull(displayImageModel.video_time) && displayImageModel.news_type == NewsType.NEWS_VIDEO.getNewsType()) {//视频角标
            textView.setText(displayImageModel.video_time);
//            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.apk_ic_feeds_video, 0, 0, 0);
            textView.setCompoundDrawablePadding(2);
            textView.setVisibility(View.VISIBLE);
            if(isShowVideoPlayButton) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
            textView.setBackgroundResource(R.drawable.btn_black_transparent_normal);
        } else if (displayImageModel.imgs_count > 0) {//图集角标
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            if (displayImageModel.news_type == NewsType.NEWS_IMAGE.getNewsType()) {
                textView.setText(displayImageModel.imgs_count + "图");
                textView.setBackgroundResource(R.drawable.btn_black_transparent_normal);
                textView.setVisibility(View.VISIBLE);
            }
        }

    }
}

