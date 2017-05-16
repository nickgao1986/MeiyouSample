package com.meetyou.crsdk.video.event;

/**
 * 播放完成事件
 * Created by wuzhongyou on 16/6/28.
 */
public class VideoPlayCompleteEvent {

    private int position;

    /** 用于处理视频播放唯一性 */
    private String uniqueVideoListId;

    /**
     * 播放完成事件
     *
     * @param pos
     * @param uniqueVideoListId 界面和列表唯一，刷新时也会重置
     */
    public VideoPlayCompleteEvent(int pos, String uniqueVideoListId) {
        this.position = pos;
        this.uniqueVideoListId = uniqueVideoListId;
    }

    public int getPosition() {
        return position;
    }

    public String getUniqueVideoListId() {
        return uniqueVideoListId;
    }
}
