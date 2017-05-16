package com.meetyou.crsdk.video.event;

/**
 * Created by gaoyoujian on 2017/5/11.
 */

public class ScreenStatusChangeEvent {
    private String screenAction;

    public ScreenStatusChangeEvent(String screenAction) {
        this.screenAction = screenAction;
    }

    public String getScreenAction() {
        return this.screenAction;
    }

    public void setScreenAction(String screenAction) {
        this.screenAction = screenAction;
    }
}
