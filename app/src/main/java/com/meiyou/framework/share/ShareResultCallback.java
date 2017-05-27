package com.meiyou.framework.share;

/**
 * Created by gaoyoujian on 2017/5/27.
 */

public interface ShareResultCallback {
    public void onStart(ShareType shareType);
    public void onSuccess(ShareType shareType);
    public void onFailed(ShareType shareType,int code,String error);
    public void onEditViewDisappear(ShareType shareType);// shareActivity 界面取消的时候回调
}
