package com.meiyou.framework.share;

/**
 * Created by gaoyoujian on 2017/5/27.
 */

public enum ShareResult {

    SUCCESS(0),
    FAIL(1),
    CANCLE(2);

    private int mResult;
    private ShareResult(int titleId) {
        this.mResult = titleId;
    }


    public int getValue() {
        return mResult;
    }

}
