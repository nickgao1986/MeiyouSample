package com.meetyou.crsdk.util;

import android.content.Context;

import com.meetyou.crsdk.video.JCFullScreenActivity;
import com.meetyou.crsdk.video.TopicFullScreenController;
import com.meetyou.crsdk.video.core.VideoProgressStatus;
import com.meetyou.crsdk.video.view.JCVideoView;
import com.meetyou.crsdk.video.view.VideoPlayStatus;
import com.meetyou.crsdk.video.view.VideoViewInfo;

import java.util.HashMap;

import nickgao.com.framework.utils.LogUtils;

/**
 * Created by zhuangyufeng on 16/11/16.
 */

public class VideoPlayUtil {
    protected static final String TAG = "VideoPlayUtil";


    /**
     * 播放视频
     *
     * @param jcVideoView
     * @param videoPlayStatus
     */
    public static void play(JCVideoView jcVideoView, VideoPlayStatus videoPlayStatus) {
        LogUtils.d(TAG, "视频处于屏幕内,开始自动播放");
        try {
            if (!jcVideoView.checkDataNormal()) {
                return;
            }
            if (jcVideoView.startPlay(videoPlayStatus.progress)) {
                videoPlayStatus.changeVideoPlayStatus(true, true, false, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置初始化
     *
     * @param jcVideoView
     * @param videoPlayStatus
     */
    public static void complete(JCVideoView jcVideoView, VideoPlayStatus videoPlayStatus) {
    }

    /**
     * 跳转到全屏
     *
     * @param context
     * @param jcVideoView
     * @param videoPlayStatus
     * @param videoViewInfo
     */
    public static void fullPlay(Context context, JCVideoView jcVideoView, VideoPlayStatus videoPlayStatus, VideoViewInfo videoViewInfo, int mNewsId, String mClassName, String time) {
        videoPlayStatus.changeVideoPlayStatus(false, true, false, false);
        jcVideoView.stopAndRelease(false, false, false);
        jcVideoView.changeJumpToFull(true);
        JCFullScreenActivity.toJCFullScreenActivity(context, jcVideoView.getPosition(), videoPlayStatus, videoViewInfo, new TopicFullScreenController(context, mNewsId, mClassName, time, videoPlayStatus));
    }

    /**
     * 视频ga逻辑处理
     *
     * @param position
     * @param progressStatus
     * @param endDuration    这个时间给的long  我们自己转成string类型
     */
    public static void handleVideoPalyBi(Context context, int vid, int position, VideoProgressStatus progressStatus, String duration, long endDuration) {
        int endType = 0;
        String endDurationStr = null;
        if (progressStatus.value() == VideoProgressStatus.COMPLETE.value()) {
            endType = 1;
            endDurationStr = duration;
        } else if (progressStatus.value() == VideoProgressStatus.PAUSE.value()) {
            endType = 2;
            endDurationStr = getEndDuration(endDuration);
        }
        if (endType > 0) {
            postVideoPlayEndBi(context, vid, position, duration, endType, endDurationStr);
        }
    }

    /**
     * 发送视频结束bi
     *
     * @param vid         视频id
     * @param position    视频位置（1是首页，2是详情页，3是专题）
     * @param duration    视频总时长
     * @param endType     终止类型（1是播放完成，2是暂停)
     * @param endDuration 终止时长，记录终止时所处的时长
     */
    public static void postVideoPlayEndBi(Context context, int vid, int position, String duration, int endType, String endDuration) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("topic_id", vid);
            params.put("position", position);
            params.put("duration", duration);
            params.put("end type", endType);
            params.put("end duration", endDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束时间字符串
     *
     * @return
     */
    private static String getEndDuration(long endDuration) {
        int hours = (int) (endDuration / 60 / 60 / 1000);
        int minutes = (int) ((endDuration / 60 / 1000) % 60);
        int seconds = (int) (endDuration / 1000) % 60 % 60;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTimeNumber(hours)).append(":").append(getTimeNumber(minutes)).append(":").append(getTimeNumber(seconds));
        return stringBuilder.toString();
    }

    private static String getTimeNumber(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return String.valueOf(num);
    }
}
