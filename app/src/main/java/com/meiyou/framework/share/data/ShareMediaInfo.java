package com.meiyou.framework.share.data;

import java.io.Serializable;

/**
 * Created by hxd on 16/7/19.
 */
abstract public class ShareMediaInfo implements Serializable{

    public static enum TYPE {
        IMAGE, VIDEO;
    }

    public abstract TYPE getType();
}
