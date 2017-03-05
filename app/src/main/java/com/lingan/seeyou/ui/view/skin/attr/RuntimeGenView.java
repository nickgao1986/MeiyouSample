package com.lingan.seeyou.ui.view.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by gaoyoujian on 2017/3/5.
 */

public interface RuntimeGenView {
    void addRuntimeView(View var1, List<MutableAttr> var2);

    MutableAttr createMutableAttr(String var1, int var2);
}
