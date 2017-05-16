package com.meetyou.crsdk.video.core;

/**
 * Created by wuzhongyou on 2016/12/26.
 */

public enum VideoProgressStatus {

    CLICKSTART(0),      // 点击播放,从0：00开始
    START(1),           // 开始播放,从0：00开始
    COMPLETE(2),        // 播放完成
    ONEQUARTER(3),      // 播放1/4
    HALF(4),            // 播放一半
    THREEQUARTER(5),    // 播放3/4
    ERROR(6),           // 播放出错
    PAUSE(7),           // 暂停
    CONTINUE(8),        // 续播
    SEEKBARTOUCH(9);    // 进度条拖动

    public static VideoProgressStatus valueOf(int value) {
        VideoProgressStatus[] arr = VideoProgressStatus.values();
        for (VideoProgressStatus id: arr) {
            if (id.progressStatus == value) {
                return id;
            }
        }
        return null;
    }

    public int value() {
        return this.progressStatus;
    }

    private int progressStatus;

     VideoProgressStatus(int progressStatus) {
        this.progressStatus = progressStatus;
    }
}
