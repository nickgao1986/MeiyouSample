package com.meiyou.framework.share;

import android.app.Activity;

import com.meiyou.framework.share.data.BaseShareInfo;

/**
 * Author: lwh
 * Date: 12/16/16 15:48.
 */

public abstract class ShareInterceptor {


    public abstract void  showShare(final Activity context, BaseShareInfo shareInfoDO);

    public abstract BaseShareInfo beforeExecute(final Activity context, ShareType type, BaseShareInfo shareInfoDO);

    public abstract BaseShareInfo afterExecute(ShareType type, BaseShareInfo baseShareInfo, ShareResult shareResult);


}
