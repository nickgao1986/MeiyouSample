package com.meetyou.crsdk.video.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;

import com.meetyou.media.player.client.ui.MeetyouPlayerTextureView;

/**
 * Created by wuzhongyou on 2017/3/20.
 */

public class JCMediaPlayerTextureView extends MeetyouPlayerTextureView {

    private Surface mJCSurface;

    public JCMediaPlayerTextureView(Context context) {
        super(context);
    }

    public JCMediaPlayerTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JCMediaPlayerTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Surface getSurface() {
        return mJCSurface;
    }

    public void setJCSurface(Surface jcSurface) {
        this.mJCSurface = jcSurface;
    }
}
