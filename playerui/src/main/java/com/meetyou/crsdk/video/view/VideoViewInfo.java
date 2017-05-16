package com.meetyou.crsdk.video.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.meetyou.crsdk.util.NetWorkStatusUtil;

import java.io.Serializable;

import nickgao.com.framework.utils.StringUtil;

/**
 * 视频视图显示信息
 * Created by wuzhongyou on 2016/11/21.
 */
public class VideoViewInfo implements Serializable {

    //*****************************基本信息**********************************
    /**
     * 蒙版图片
     */
    public String imageUrl = "";
    /**
     * 普通视频源
     */
    private String videoUrl = "";
    /**
     * 高清视频源
     */
    private String hdVideoUrl = "";
    /**
     * 标题文案
     */
    public String title = "";
    /**
     * 结束文案
     */
    public String finishContent = "";
    /**
     * 视频总时长
     */
    public String totalTimeStr = "";
    /**
     * 视频大小
     */
    public String totalSizeStr = "";

    /**
     * 视频视图显示信息
     *
     * @param imageUrl            蒙版图片 不可传null
     * @param videoUrl            视频源,必填 不可传null
     * @param hdVideoUrl          高清视频源，wifi情况下优先使用，可以没有 不可传null
     * @param title               标题信息 不可传null
     * @param finishContent       结束文案 不可传null
     * @param totalTimeStr        总时长 不可传null
     */
    public VideoViewInfo(@NonNull String imageUrl, @NonNull String videoUrl, @NonNull String hdVideoUrl,
                         @NonNull String title, @NonNull String finishContent, @NonNull String totalTimeStr) {
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.hdVideoUrl = hdVideoUrl;
        this.title = title;
        this.finishContent = finishContent;
        this.totalTimeStr = totalTimeStr;
    }

    /**
     * 获取需要当前可播放的链接
     */
    public String getCurrentVideoUrl(Context context) {
        if (NetWorkStatusUtil.isWifi(context.getApplicationContext()) && !StringUtil.isNull(hdVideoUrl)) {
            return hdVideoUrl;
        }
        return videoUrl;
    }

    @Override
    public String toString() {
        return "VideoViewInfo{" +
                "imageUrl='" + imageUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", hdVideoUrl='" + hdVideoUrl + '\'' +
                ", title='" + title + '\'' +
                ", finishContent='" + finishContent + '\'' +
                ", totalTimeStr='" + totalTimeStr + '\'' +
                ", totalSizeStr='" + totalSizeStr + '\'' +
                '}';
    }
}
