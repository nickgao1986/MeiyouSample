package com.meetyou.pullrefresh;

import android.view.View;

import com.meetyou.pullrefresh.lib.PtrFrameLayout;
import com.meetyou.pullrefresh.lib.PtrHandler;

/**
 * Created by gaoyoujian on 2017/5/26.
 */

public abstract class PtrDefaultHandler implements PtrHandler {
    public PtrDefaultHandler() {
    }

    public static boolean canChildScrollUp(View view) {
        return view.canScrollVertically(-1);

    }

    public static boolean checkContentCanBePulledDown(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollUp(content);
    }

    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return checkContentCanBePulledDown(frame, content, header);
    }
}

