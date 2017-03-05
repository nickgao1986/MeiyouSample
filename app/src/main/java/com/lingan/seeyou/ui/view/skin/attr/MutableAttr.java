package com.lingan.seeyou.ui.view.skin.attr;

import android.view.View;

import nickgao.com.meiyousample.utils.StringUtils;


/**
 * Created by hxd on 15/10/30.
 */
public abstract class MutableAttr {
    protected static final String TAG="MutableAttr";
    public static final String RES_TYPE_NAME_COLOR = "color";
    public static final String RES_TYPE_NAME_DRAWABLE = "drawable";

    public enum TYPE {
        BACKGROUND("background"),
        SRC("src"),
        HINT_TEXT_COLOR("hintTextColor"),
        TEXT_COLOR("textColor"),
        STYLE("style"),
        DRAWABLE_LEFT("drawableLeft"),
        DRAWABLE_RIGHT("drawableRight"),
        DRAWABLE_TOP("drawableTop"),
        DRAWABLE_BOTTOM("drawableBottom"),
        ;
        private String realName;

        private TYPE(String realName) {
            this.realName = realName;
        }

        public String getRealName() {
            return realName;
        }
    }


    public MutableAttr(
                       String attrName, int attrValueRefId,
                       String attrValueRefName, String typeName) {
        this.attrName = attrName;
        this.attrValueRefId = attrValueRefId;
        this.attrValueRefName = attrValueRefName;
        this.attrValueTypeName = typeName;
    }

    /**
     * name of the attr, ex: background or textSize or textColor
     */
    public String attrName;

    /**
     * id of the attr value refered to, normally is [2130745655]
     */
    public int attrValueRefId;

    /**
     * entry name of the value , such as [app_exit_btn_background]
     */
    public String attrValueRefName;

    /**
     * type of the value , such as color or drawable
     */
    public String attrValueTypeName;

    public TYPE type;

    abstract public void apply(View view);


    public static boolean support(String attrName) {
        for (TYPE type : TYPE.values()) {
            if (StringUtils.equals(type.getRealName(), attrName)) {
                return true;
            }

        }
        return false;
    }

}
