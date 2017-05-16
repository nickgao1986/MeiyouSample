package com.meetyou.crsdk.video.event;

/**
 * 全屏返回事件
 * Created by wuzhongyou on 16/6/28.
 */
public class BackFullScreenEvent {

    /** 0：播放完成 1：播放中 2：暂停 */
    private int status;

    private int position;

    private long progress;

    /** 上报到的位置 0-都没报 1-上报到1/4 2-上报到1/2 3-上报到3/4 */
    private int reportStatus;

    /** 用于处理列表视频播放唯一性 */
    private String uniqueVideoListId;

    /**
     * 全屏返回事件
     *
     * @param status 0：播放完成 1：播放中 2：暂停
     * @param position
     * @param progress
     * @param uniqueVideoListId 界面和列表唯一，刷新时也会重置
     */
    public BackFullScreenEvent(int status, int position, long progress, int reportStatus, String uniqueVideoListId) {
        this.status = status;
        this.position = position;
        this.progress = progress;
        this.reportStatus = reportStatus;
        this.uniqueVideoListId = uniqueVideoListId;
    }

    public int getStatus() {
        return status;
    }

    public int getPosition() {
        return position;
    }

    public long getProgress() {
        return progress;
    }

    public int getReportStatus() {
        return reportStatus;
    }

    public String getUniqueVideoListId() {
        return uniqueVideoListId;
    }
}
