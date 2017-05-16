package com.meetyou.crsdk.video;

import android.content.Context;
import android.view.TextureView;
import android.view.ViewGroup;

import com.meetyou.crsdk.video.core.VideoProgressStatus;
import com.meetyou.crsdk.video.core.ViewListener;
import com.meetyou.crsdk.video.screen.BaseFullScreenController;
import com.meetyou.crsdk.video.view.JCTopicVideoView;
import com.meetyou.crsdk.video.view.JCVideoView;
import com.meetyou.crsdk.video.view.VideoPlayStatus;
import com.meetyou.crsdk.video.view.VideoViewInfo;
import com.meetyou.crsdk.video.view.VideoViewSetInfo;


/**
 * 全屏视频事件
 * Created by wuzhongyou on 2016/11/15.
 */
public class TopicFullScreenController implements BaseFullScreenController {
    private int mNewsId;//资讯id
    private String mClassName;//资讯分类tab
    private Context context;
    private String time;
    private VideoPlayStatus videoPlayStatus;
    private int position;

    public TopicFullScreenController(Context context, int mNewsId, String mClassName, String time, VideoPlayStatus videoPlayStatus) {
        this.context = context;
        this.mNewsId = mNewsId;
        this.mClassName = mClassName;
        this.time = time;
        this.videoPlayStatus = videoPlayStatus;
        this.position = 0;
    }

    @Override
    public void resetVideoInfo(JCVideoView videoView, int position, VideoPlayStatus videoPlayStatus, VideoViewInfo videoViewInfo,
                               ViewListener viewListener, TextureView.SurfaceTextureListener surfaceCallback) {
        ((JCTopicVideoView) videoView).setUpVideoInfo(position, videoPlayStatus, videoViewInfo, new VideoViewSetInfo(false, false, true, true, true, 0, 0), viewListener, surfaceCallback);
        videoView.initPlayStatues();
    }

    @Override
    public JCVideoView createJCVideoView(Context context) {
        JCTopicVideoView jcTopicVideoView = new JCTopicVideoView(context);
        jcTopicVideoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return jcTopicVideoView;
    }

    @Override
    public void releaseAll() {
    }

    @Override
    public boolean isCompleteBack() {
        return true;
    }

    @Override
    public void handleProgressStatusCallback(VideoProgressStatus progressStatus) {

    }
}
