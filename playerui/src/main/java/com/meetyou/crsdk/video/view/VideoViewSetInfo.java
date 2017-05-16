package com.meetyou.crsdk.video.view;

import java.io.Serializable;

/**
 * 视频视图设置信息
 * Created by wuzhongyou on 2016/11/22.
 */
public class VideoViewSetInfo implements Serializable {

    //*****************************设置信息**********************************
    /**
     * 是否显示控制条
     */
    public boolean isNeedControllerBar;
    /**
     * 是否需要结束文案
     */
    public boolean isNeedFinishContent;
    /**
     * 是否需要声音
     */
    public boolean isNeedVoice;
    /**
     * 是否自动播放
     */
    public boolean isNeedAutoPlay;
    /**
     * 是否全屏
     */
    public boolean isFullScreen;
    /**
     * 视图宽
     */
    public int viewWidth;
    /**
     * 视图高
     */
    public int viewHeight;

    /**
     * 视频视图设置信息
     *
     * @param isNeedControllerBar 是否播放时显示控制条
     * @param isNeedFinishContent 是否需要结束文案
     * @param isNeedVoice         是否需要播放声音
     * @param isNeedAutoPlay      是否自动播放
     * @param isFullScreen        是否全屏
     * @param viewWidth           视图宽
     * @param viewHeight          视图高
     */
    public VideoViewSetInfo(boolean isNeedControllerBar, boolean isNeedFinishContent,
                         boolean isNeedVoice, boolean isNeedAutoPlay, boolean isFullScreen,
                         int viewWidth, int viewHeight) {
        this.isNeedControllerBar = isNeedControllerBar;
        this.isNeedFinishContent = isNeedFinishContent;
        this.isNeedVoice = isNeedVoice;
        this.isFullScreen = isFullScreen;
        this.isNeedAutoPlay = isNeedAutoPlay;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    @Override
    public String toString() {
        return "VideoViewSetInfo{" +
                "isNeedControllerBar=" + isNeedControllerBar +
                ", isNeedFinishContent=" + isNeedFinishContent +
                ", isNeedVoice=" + isNeedVoice +
                ", isNeedAutoPlay=" + isNeedAutoPlay +
                ", isFullScreen=" + isFullScreen +
                ", viewWidth=" + viewWidth +
                ", viewHeight=" + viewHeight +
                '}';
    }
}
