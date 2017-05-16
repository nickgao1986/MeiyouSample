package com.meetyou.crsdk.video.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import com.meetyou.crsdk.video.core.ViewListener;
import com.meetyou.crsdk.video.event.FragmentVisibleEvent;
import com.meetyou.crsdk.video.event.ScreenStatusChangeEvent;

/**
 * Created by gaoyoujian on 2017/5/11.
 */

public class JCTopicVideoView extends JCVideoView {

    public JCTopicVideoView(Context context) {
        super(context);
    }

    public JCTopicVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置播放的信息
     *
     * @param position
     * @param videoPlayStatus
     * @param videoViewInfo
     * @param videoViewSetInfo
     * @param viewListener
     * @param surfaceCallback
     */
    public void setUpVideoInfo(int position, VideoPlayStatus videoPlayStatus, VideoViewInfo videoViewInfo, VideoViewSetInfo videoViewSetInfo, ViewListener viewListener, TextureView.SurfaceTextureListener surfaceCallback) {
        setUp(position, JCVideoPlayer.IV_TYPE_CENTER_CROP, false, true, videoPlayStatus, videoViewInfo, videoViewSetInfo, viewListener, surfaceCallback);
    }

    @Override
    protected void resetSetupInfo() {
        setUp(mPosition, JCVideoPlayer.IV_TYPE_CENTER_CROP, false, true, mVideoPlayStatus, mVideoViewInfo, mVideoViewSetInfo, mViewListener, mSurfaceCallback);
    }

    @Override
    protected int getVideoType() {
        return VIDEO_TYPE_TOPIC;
    }

    @Override
    protected void checkInitAutoPlay() {
        if (checkDataNormal()
                && mVideoViewSetInfo.isNeedAutoPlay
                && !mVideoPlayStatus.isScrolled
                && mSurface != null
                && !mVideoPlayStatus.isScrolling
                && !isLockScreen()) {
            setHadShow50Percent(true);
            doAutoPlay(true);
        }
    }

    @Override
    protected void doClickPlay(boolean isReplay) {
        try {
            if (!checkDataNormal() || isLockScreen())
                return;
            if (!isReplay && (mVideoViewSetInfo.isFullScreen || (mViewController.getCurrentStatus() == ViewStatus.PAUSE.value() && !mVideoPlayStatus.isPlaying))) {
                play();
            } else if (!mVideoPlayStatus.isPlaying && startPlay(mVideoPlayStatus.progress)) {
            } else {
                return;
            }
            mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
            if (mViewController.getViewListener() != null) {
                if (!isReplay) {
                    mViewController.getViewListener().onClickPlayOver();
                } else {
                    mViewController.getViewListener().onClickReplayOver();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFragmentVisibleChanged(FragmentVisibleEvent event) {
        try {
            // TODO 话题部分暂时只有首页才有，细节后期调整
            // 暂停掉
            scrollToStop(false, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleEventMainThread(ScreenStatusChangeEvent event) {

    }
}

