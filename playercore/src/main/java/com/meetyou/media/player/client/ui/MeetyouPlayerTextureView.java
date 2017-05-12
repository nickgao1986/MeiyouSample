package com.meetyou.media.player.client.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

/**
 * Created by Linhh on 17/1/19.
 */

public class MeetyouPlayerTextureView extends TextureView implements MeetyouPlayerView {

    private MeasureHelper mMeasureHelper;

    private Surface mSurface;

    public MeetyouPlayerTextureView(Context context) {
        super(context);
        initView(context);
    }

    public MeetyouPlayerTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MeetyouPlayerTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mMeasureHelper = new MeasureHelper(this);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean shouldWaitForResize() {
        return false;
    }

    //--------------------
    // Layout & Measure
    //--------------------
    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            requestLayout();
        }
    }

    @Override
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
//        if (videoSarNum > 0 && videoSarDen > 0) {
//            mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
//            requestLayout();
//        }
    }

    @Override
    public void setVideoRotation(int degree) {
        mMeasureHelper.setVideoRotation(degree);
        setRotation(degree);
    }

    @Override
    public void setAspectRatio(int aspectRatio) {
//        mMeasureHelper.setAspectRatio(aspectRatio);
//        requestLayout();
    }

    @Override
    public Surface getSurface() {
        if(mSurface == null){
            mSurface = new Surface(getSurfaceTexture());
        }
        return mSurface;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
    }
}
