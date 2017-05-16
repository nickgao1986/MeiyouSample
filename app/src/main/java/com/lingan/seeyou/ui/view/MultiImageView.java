package com.lingan.seeyou.ui.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meetyou.crsdk.util.ImageLoader;

import java.util.List;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.UrlUtil;
import nickgao.com.okhttpexample.view.ImageLoadParams;
import nickgao.com.okhttpexample.view.LoaderImageView;
import nickgao.com.okhttpexample.view.RichDrawable;

/**
 * 她她圈列表多图View
 * Created by hyx on 6/20/2016.
 */

public class MultiImageView extends LinearLayout {
    private static final int DEFAULT_IMAGE_COUNT = 3;
    private RelativeLayout[] mRelativeLayouts;
    private LoaderImageView[] mLoaderImageViews;
    private BadgeImageView[] mBadgeImageViews;
    private TextView[] textViews;
    private Context mContext;
    private int mCount = DEFAULT_IMAGE_COUNT;

    public MultiImageView(Context context) {
        super(context);
        init(context, DEFAULT_IMAGE_COUNT);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, DEFAULT_IMAGE_COUNT);
    }

    /**
     * 初始化控件
     */
    private void init(Context context, int count) {
        if (count <= 0) {
            throw new RuntimeException("MultiImageView must be greater than 0");
        }
        int space = DeviceUtils.dip2px(context, 5);
        this.mCount = count;
        this.mContext = context;
        removeAllViews();
        mRelativeLayouts = new RelativeLayout[count];
        mLoaderImageViews = new LoaderImageView[count];
        mBadgeImageViews = new BadgeImageView[count];
        textViews = new TextView[count];
        for (int i = 0; i <= count - 1; i++) {
            mRelativeLayouts[i] = new RelativeLayout(mContext);

            mLoaderImageViews[i] = new LoaderImageView(mContext);
            RelativeLayout.LayoutParams ivLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mRelativeLayouts[i].addView(mLoaderImageViews[i], ivLayoutParams);

            mBadgeImageViews[i] = new BadgeImageView(mContext, mLoaderImageViews[i]);
            mBadgeImageViews[i].setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);

            textViews[i] = new TextView(context);
            RelativeLayout.LayoutParams lpTextView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpTextView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lpTextView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lpTextView.rightMargin = space;
            lpTextView.bottomMargin = space;
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setTextSize(10);
            textViews[i].setTextColor(context.getResources().getColor(R.color.white_a));
            textViews[i].setPadding(space, 0, space, DeviceUtils.dip2px(context, 1));
            mRelativeLayouts[i].addView(textViews[i], lpTextView);

            addView(mRelativeLayouts[i]);
        }
    }

    /**
     * 设置图片的张数, 重新初始化count个LoaderImageView
     *
     * @param count
     */
    private void setImageCount(int count) {
        init(mContext, count);
    }

    /**
     * 显示图片
     *
     * @param displayImageModels 图片URI及一些特殊属性
     * @param width              每张图宽度 px
     * @param height             每张图高度 px
     * @param spaceDpValue       两张图之间的间隔 dp
     * @param imageLoadParams    imageLoadParams
     */
    public void displayImage(List<DisplayImageModel> displayImageModels, int width, int height, int spaceDpValue, ImageLoadParams imageLoadParams) {
        if (displayImageModels == null || displayImageModels.size() == 0) {
            setVisibility(View.GONE);
            return;
        }
        //列表页gif都不自动播放
        imageLoadParams.anim = false;

        for (int i = 0; i <= mCount - 1; i++) {
            RelativeLayout rl = mRelativeLayouts[i];
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl.getLayoutParams();
            params.height = height;
            params.width = width;
            if (i != 0) {
                params.leftMargin = DeviceUtils.dip2px(mContext, spaceDpValue);
            }
            rl.requestLayout();
            rl.setVisibility(View.GONE);
        }
        int len = displayImageModels.size();
        for (int i = 0; i < len; i++) {
            DisplayImageModel displayImageModel = displayImageModels.get(i);
            if (i > mCount - 1) {
                //最多只展示mLoaderImageViews.length张图
                break;
            }

            if (StringUtils.isNull(displayImageModel.imageUrl)) {
                continue;
            }
            //清空RichDrawable
           // mLoaderImageViews[i].setRichDrawable(null);

            mRelativeLayouts[i].setVisibility(View.VISIBLE);
            int[] widthHeight = UrlUtil.getWidthHeightByUrl(displayImageModel.imageUrl);

            if (displayImageModel.isShowNewsTopic) {//首页资讯贴要显示视频和图集数
                mBadgeImageViews[i].hide();
                if (i == len - 1) {
                    textViews[i].setVisibility(View.GONE);
                    if (!StringUtils.isNull(displayImageModel.video_time) && displayImageModel.news_type==4) {
                        textViews[i].setText(displayImageModel.video_time);
                        textViews[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.apk_ic_feeds_video, 0, 0, 0);
                        textViews[i].setCompoundDrawablePadding(2);
                        textViews[i].setBackgroundResource(R.drawable.btn_black_transparent_normal);
                        textViews[i].setVisibility(View.VISIBLE);
                    } else if (displayImageModel.imgs_count > 0) {
                        if (displayImageModel.news_type == 3 || displayImageModel.news_type==2) {// 2资讯图文 3图集 需要显示图片右下角图片数
                            textViews[i].setText(displayImageModel.imgs_count + "图");
                            textViews[i].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            textViews[i].setBackgroundResource(R.drawable.btn_black_transparent_normal);
                            textViews[i].setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    textViews[i].setVisibility(View.GONE);
                }
            } else {
                textViews[i].setVisibility(View.GONE);
                if (displayImageModel.isVideo) {
                    mBadgeImageViews[i].setBadgePosition(BadgeView.POSITION_CENTER);
                    mBadgeImageViews[i].setImageResource(R.drawable.video_btn_play);
                    mBadgeImageViews[i].show();
                } else {
                    mBadgeImageViews[i].hide();
//                    if (displayImageModel.isJudgeGif && displayImageModel.imageUrl.contains(".gif")) {
//                        mLoaderImageViews[i].setRichDrawable(getRightBottomRichDrawable(R.drawable.apk_gif));
//                    } else if (displayImageModel.isJudgeLongPicture && null != widthHeight && widthHeight.length > 1 && BitmapUtil
//                            .isLongPicture(widthHeight)) { // 显示长图标记,长图标准 高/宽 > 3.0则为长图
//                        mLoaderImageViews[i].setRichDrawable(getRightBottomRichDrawable(R.drawable.apk_longpic));
//                    }
                }
            }
            ImageLoader.getInstance().displayImage(mContext.getApplicationContext(), mLoaderImageViews[i], displayImageModel.imageUrl, imageLoadParams, null);
        }
    }

    private RichDrawable getRightBottomRichDrawable(int imageResId){
        RichDrawable richDrawable = new RichDrawable(mContext.getResources().getDrawable(imageResId));
        int margin = DeviceUtils.dip2px(mContext, 5);
        richDrawable.setPosition(RichDrawable.POS_RIGHT_BOTTOM);
        richDrawable.setMargin(0, margin, 0, margin);
        return richDrawable;
    }

    public static class DisplayImageModel {
        public String imageUrl;
        public boolean isVideo;
        public boolean isJudgeGif;  //是否判断是否是gif
        public boolean isJudgeLongPicture;  //是否判断是否是长图
        public boolean isShowNewsTopic;//是否显示首页资讯贴子的图集和视频
        public String video_time;//视频时间
        public int imgs_count;//图片数量
        public int news_type;//是不是图集类型
    }
}
