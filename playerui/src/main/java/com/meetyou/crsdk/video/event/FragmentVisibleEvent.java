package com.meetyou.crsdk.video.event;

/**
 * Created by gaoyoujian on 2017/5/11.
 */

public class FragmentVisibleEvent {

    private String mFragmentName;

    public FragmentVisibleEvent(String name) {
        this.mFragmentName = name;
    }

    public String getFragmentName() {
        return this.mFragmentName;
    }

}
