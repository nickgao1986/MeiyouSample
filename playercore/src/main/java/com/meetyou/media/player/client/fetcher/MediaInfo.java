package com.meetyou.media.player.client.fetcher;

import java.util.Map;

/**
 * Created by Linhh on 17/1/17.
 */

public class MediaInfo {

    public String source;
    public long totalSize;
    public Map<Long, Integer> statusMap;

    public String getSource() {
        return source;
    }

    public void setSource(String mSource) {
        this.source = mSource;
    }

    public Map<Long, Integer> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<Long, Integer> mStatusMap) {
        this.statusMap = mStatusMap;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long mTotalSize) {
        this.totalSize = mTotalSize;
    }


}
