package com.lingan.seeyou.ui.view.skin.attr;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxd on 15/10/30.
 */
public class MutableAttrHolder {

    public WeakReference<View> viewRef;
    public List<MutableAttr> mutableAttrList = new ArrayList<>();

    public MutableAttrHolder(View view, List<MutableAttr> list) {
        viewRef = new WeakReference<>(view);
        this.mutableAttrList = list;
    }
}
