package com.meiyou.framework.share;

import java.util.Map;

/**
 * Created by hxd on 15/7/30.
 */
public interface GetDataListener  {
    public void onStart();
    public void onComplete(int i, Map<String, String> map) ;
    public void onError(int code, String message) ;
    public void onCancle();
}
