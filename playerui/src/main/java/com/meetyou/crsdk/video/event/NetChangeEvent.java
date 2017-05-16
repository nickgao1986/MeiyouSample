package com.meetyou.crsdk.video.event;

/**
 * Created by gaoyoujian on 2017/5/11.
 */

public class NetChangeEvent {
    private int netType;

    public NetChangeEvent(int netType) {
        this.netType = netType;
    }

    public int getNetType() {
        return this.netType;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }
}
