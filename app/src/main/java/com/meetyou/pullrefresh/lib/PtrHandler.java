package com.meetyou.pullrefresh.lib;

import android.view.View;

/**
 * Created by gaoyoujian on 2017/5/26.
 */

public interface PtrHandler {
    boolean checkCanDoRefresh(PtrFrameLayout var1, View var2, View var3);

    void onRefreshBegin(PtrFrameLayout var1);
}
