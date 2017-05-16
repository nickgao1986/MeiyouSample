package com.meetyou.crsdk.video.view;

/**
 *
 * Author: lwh
 * Date: 5/27/16 16:05.
 */
public enum  ViewStatus {
    NORAML(1),
    PREPARE(2),
    PLAYING(3),
    PAUSE(4),
    COMPLETE(5),
    ERROR(6),
    NO_NET(7),
    NET_CHANGE(8);

    public static ViewStatus valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {
            case 1:
                return NORAML;
            case 2:
                return PREPARE;
            case 3:
                return PLAYING;
            case 4:
                return PAUSE;
            case 5:
                return COMPLETE;
            case 6:
                return ERROR;
            case 7:
                return NO_NET;
            case 8:
                return NET_CHANGE;
            default:
                return null;
        }
    }

    public int value() {
        return this.nCode;
    }

    private int nCode;
    private ViewStatus(int _nCode) {
        this.nCode = _nCode;
    }

}
