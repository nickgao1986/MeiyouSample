package com.meetyou.pullrefresh.lib;

/**
 * Created by gaoyoujian on 2017/5/26.
 */

public interface PtrUIHandler {
    void onUIReset(PtrFrameLayout var1);

    void onUIRefreshPrepare(PtrFrameLayout var1);

    void onUIRefreshBegin(PtrFrameLayout var1);

    void onUIRefreshComplete(PtrFrameLayout var1);

    void onUIPositionChange(PtrFrameLayout var1, boolean var2, byte var3, PtrIndicator var4);
}
