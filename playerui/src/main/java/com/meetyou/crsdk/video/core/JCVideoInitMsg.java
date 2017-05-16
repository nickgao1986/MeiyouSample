package com.meetyou.crsdk.video.core;

import android.view.Surface;

import com.meetyou.media.player.client.ui.MeetyouPlayerTextureView;

import java.io.Serializable;

/**
 * 视频播放信息
 * Created by wuzhongyou on 16/7/3.
 */
public class JCVideoInitMsg implements Serializable {

    private String url;
    private boolean isLocalFile;
    private boolean isNeedVoice;
    private MeetyouPlayerTextureView textureView;
    private Surface surface;

    private String uniqueRequestVideoId;

    public JCVideoInitMsg(String url, boolean isLocalFile, boolean isNeedVoice, MeetyouPlayerTextureView textureView, Surface surface, String uniqueRequestVideoId) {
        this.url = url;
        this.isLocalFile = isLocalFile;
        this.isNeedVoice = isNeedVoice;
        this.textureView = textureView;
        this.surface = surface;
        this.uniqueRequestVideoId = uniqueRequestVideoId;
    }

    public String getUrl() {
        return url;
    }

    public boolean isLocalFile() {
        return isLocalFile;
    }

    public boolean isNeedVoice() {
        return isNeedVoice;
    }

    public MeetyouPlayerTextureView getTextureView() {
        return textureView;
    }

    public Surface getSurface() {
        return surface;
    }

    public String getUniqueRequestVideoId() {
        return uniqueRequestVideoId;
    }
}
