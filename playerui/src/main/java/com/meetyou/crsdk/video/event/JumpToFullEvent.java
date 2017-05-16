package com.meetyou.crsdk.video.event;

/**
 * 跳转进入全屏事件
 * Created by wuzhongyou on 16/6/29.
 */
public class JumpToFullEvent {
    private int position;

    /** 用于处理视频播放唯一性 */
    private String uniqueVideoListId;

    /**
     * 跳转进入全屏事件
     *
     * @param pos
     * @param uniqueVideoListId 界面和列表唯一，刷新时也会重置
     */
    public JumpToFullEvent(int pos, String uniqueVideoListId) {
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
