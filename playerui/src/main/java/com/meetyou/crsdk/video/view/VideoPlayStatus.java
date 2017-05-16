package com.meetyou.crsdk.video.view;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by wuzhongyou on 16/6/23.
 */
public class VideoPlayStatus implements Serializable {

    /**
     * 用于处理视频播放唯一性
     */
    public String uniqueVideoListId = "";

    //*****************************状态信息**********************************
    /** 播放进度记录 */
    public long progress;
    /** 是否播放过 */
    public boolean isPlayed;
    /** 是否暂停状态 */
    public boolean isPaused;
    /** 是否播放结束 */
    public boolean isCompleted;
    /** 是否播放中 */
    public boolean isPlaying;

    //************************************************************************
    /** 是否未播放完进入全屏(播放中其他视屏跳转全屏) */
    public boolean isJumpFull;
    /** 是否从全屏返回继续播放 */
    public boolean isBackFullScreenContinue = false;
    /** 是否滑动过 */
    public boolean isScrolled;
    /** 是否延迟初始化过 */
    public boolean isInited;

    //*****************************上报信息**********************************
    /** 上报到的位置 0-都没报 1-上报到1/4 2-上报到1/2 3-上报到3/4 */
    public int reportStatus;
    /** 是否刚刚上报播放 0-没有播放过 1-播放中 2-暂停 */
    public int reportPlay;

    //******************************历史播放信息用于界面传递状态******************************
    /** 是否销毁前正在播放 */
    public boolean isLastPlaying = false;
    /** 是否从全屏返回继续播放 */
    public boolean isFromFullContinuePlay = false;
    /** 是否从全屏返回 */
    public boolean isFromFullScreen = false;
    /** 本次destory是由于跳转至全屏 */
    public boolean isJumpToFull = false;
    /** 是否滚动中 */
    public boolean isScrolling = false;
    /** 是否显示超过50%区域 */
    public boolean isHadShow50Percent = false;

    /**
     * 视频播放控制状态信息
     * @param context
     * @param uniqueVideoListId 列表唯一值，用于处理播放和停止状态信息使用 不可传null
     */
    public VideoPlayStatus(Context context, @NonNull String uniqueVideoListId) {
        this.uniqueVideoListId = uniqueVideoListId;
//        File file = new File(CacheDisc.getCacheFile(context.getApplicationContext()) + "/" + MD5Utils.convert(url));
//        if (file.exists()) {
//            isPlayed = true;
//        } else {
//            isPlayed = false;
//        }
    }

    public synchronized void changeVideoPlayStatus(boolean isPlaying, boolean isPlayed, boolean isPaused, boolean isCompleted) {
        this.isPlaying = isPlaying;
        this.isPlayed = isPlayed;
        this.isPaused = isPaused;
        this.isCompleted = isCompleted;
    }

    public void changeComplete() {
        changeVideoPlayStatus(false, true, false, true);
        progress = 0;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void resetReportStatus() {
        this.reportStatus = 0;
    }

    @Override
    public String toString() {
        return "VideoPlayStatus{" +
                "uniqueVideoListId='" + uniqueVideoListId + '\'' +
                ", progress=" + progress +
                ", isPlayed=" + isPlayed +
                ", isPaused=" + isPaused +
                ", isCompleted=" + isCompleted +
                ", isPlaying=" + isPlaying +
                ", isJumpFull=" + isJumpFull +
                ", isBackFullScreenContinue=" + isBackFullScreenContinue +
                ", isScrolled=" + isScrolled +
                ", isInited=" + isInited +
                ", reportStatus=" + reportStatus +
                '}';
    }
}
