package com.meiyou.framework.share.data;

/**
 * Created by hxd on 16/7/18.
 */
public class ShareVideo extends ShareMediaInfo {

    private String videoUrl;
    private String ThumbUrl;
    private int ThumbRes = -1;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbUrl() {
        return ThumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        ThumbUrl = thumbUrl;
    }

    public int getThumbRes() {
        return ThumbRes;
    }

    public void setThumbRes(int thumbRes) {
        ThumbRes = thumbRes;
    }

    @Override
    public TYPE getType() {
        return TYPE.VIDEO;
    }
}
