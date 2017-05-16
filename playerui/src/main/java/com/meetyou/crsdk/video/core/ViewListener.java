package com.meetyou.crsdk.video.core;

/**
 * Author: lwh
 * Date: 5/27/16 16:14.
 */
public interface ViewListener {

    public void onProgressStatusCallback(VideoProgressStatus progressStatus);
    /** 点击返回 */
    public void onClickBack();
    /** 点击全屏 */
    public void onClickFullScreen();
    /** 点击播放按钮 */
    public void onClickPlayOver();
    /** 点击重播按钮 */
    public void onClickReplayOver();
    /** 点击视频区域 */
    public void onClickVideoView();
    /** 点击结束文案 */
    public void onClickComplte();
    /** 暂停回调按钮 */
    public void onClickPauseOver();
    //***********控制进度条*******
    /** 按下进度条
     * @param isPlaying*/
    public void onSeekTouchDown(boolean isPlaying);
    /** 松开进度条
     * @param isPlaying*/
    public void onSeekTouchUp(boolean isPlaying);
}
