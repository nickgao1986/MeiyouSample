package com.meiyou.media.player.tv;

/**
 * 播放器数据
 * Created by Linhh on 17/1/3.
 */

public class MeetyouTrackInfo {

    public String url;//播放地址
    public long duration;//播放间断时间

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


}
