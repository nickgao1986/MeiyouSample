package com.lingan.seeyou.ui.view.skin.attr;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import nickgao.com.framework.utils.LogUtils;


/**
 * img  src
 * Created by hxd on 16/5/12.
 */
public class ImageSrcAttr extends MutableAttr {
    public ImageSrcAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        this.type = TYPE.SRC;
    }

    @Override
    public void apply(View view) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
                Drawable bg = SkinManager.getInstance().getAdapterDrawable(
                        attrValueTypeName, attrValueRefName, attrValueRefId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageView.setImageDrawable(bg);
                } else {
                    imageView.setImageDrawable(bg);
                }
            }else if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)){
                int color = SkinManager.getInstance().
                        getAdapterColor(attrValueTypeName, attrValueRefName, attrValueRefId);
                imageView.setImageDrawable(new ColorDrawable(color));
            }
        }else {
            LogUtils.w(TAG,"ImageSrcAttr apply src not ImageView! ");
        }

    }
}
