package com.lingan.seeyou.ui.view.skin.attr;

import android.view.View;

/**
 * style
 * Created by hxd on 16/5/12.
 */
public class StyleAttr extends MutableAttr {

    public StyleAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        this.type = TYPE.STYLE;

    }

    @Override
    public void apply(View view) {

    }
}
