package com.lingan.seeyou.ui.view.skin.attr;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.lingan.seeyou.ui.view.skin.SkinManager;


/**
 * Created by hxd on 15/10/30.
 */
public class BackgroundAttr extends MutableAttr {

    public BackgroundAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        this.type = TYPE.BACKGROUND;
    }

    @Override
    public void apply(View view) {
        if (view==null){
            return;
        }
        if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
            view.setBackgroundColor(SkinManager.getInstance().getAdapterColor(
                    attrValueTypeName, attrValueRefName, attrValueRefId));
        } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
            Drawable bg = SkinManager.getInstance().getAdapterDrawable(
                    attrValueTypeName, attrValueRefName, attrValueRefId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(bg);
            } else {
                view.setBackgroundDrawable(bg);
            }

        }
       //LogUtils.d(TAG, "view apply ,id "+attrValueRefId+" " + attrValueTypeName+" "+attrValueRefName );
    }
}
