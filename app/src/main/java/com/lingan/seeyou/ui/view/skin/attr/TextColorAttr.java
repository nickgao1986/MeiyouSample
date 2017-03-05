package com.lingan.seeyou.ui.view.skin.attr;

import android.view.View;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;


/**
 * Created by hxd on 15/10/30.
 */
public class TextColorAttr extends MutableAttr {
    public static final String RES_TYPE_NAME_HINT_COLOR = "hintTextColor";

    public TextColorAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        this.type = TYPE.TEXT_COLOR;
    }

    @Override
    public void apply(View view) {
        if (view == null) {
            return;
        }
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if ((RES_TYPE_NAME_COLOR.equals(attrValueTypeName))
                    || (RES_TYPE_NAME_HINT_COLOR.equals(attrValueTypeName))) {
                tv.setTextColor(SkinManager.getInstance().getAdapterColorStateList(
                        attrValueTypeName, attrValueRefName, attrValueRefId));

            }
        }
    }
}
