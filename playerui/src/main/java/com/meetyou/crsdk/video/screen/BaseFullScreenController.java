package com.meetyou.crsdk.video.screen;

import android.content.Context;
import android.view.TextureView;

import com.meetyou.crsdk.video.core.VideoProgressStatus;
import com.meetyou.crsdk.video.core.ViewListener;
import com.meetyou.crsdk.video.view.JCVideoView;
import com.meetyou.crsdk.video.view.VideoPlayStatus;
import com.meetyou.crsdk.video.view.VideoViewInfo;

/**
 * Created by wuzhongyou on 2016/11/15.
 */
public interface BaseFullScreenController {

    public void resetVideoInfo(JCVideoView videoView, int position, VideoPlayStatus videoPlayStatus, VideoViewInfo videoViewInfo,
                               ViewListener viewListener, TextureView.SurfaceTextureListener surfaceCallback);

    public JCVideoView createJCVideoView(Context context);

    public void releaseAll();

    public boolean isCompleteBack();

    public void handleProgressStatusCallback(VideoProgressStatus progressStatus);
}
