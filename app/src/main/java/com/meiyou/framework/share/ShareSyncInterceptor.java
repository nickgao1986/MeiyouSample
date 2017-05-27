package com.meiyou.framework.share;

import android.app.Activity;

import com.meiyou.framework.share.data.BaseShareInfo;

/**
 * 异步的拦截器
 * Author: lwh
 * Date: 3/13/17 14:50.
 */

public abstract class ShareSyncInterceptor {

    public abstract BaseShareInfo beforeSyncExecute(final Activity context, ShareType type, BaseShareInfo shareInfoDO);

    public abstract BaseShareInfo afterExecute(ShareType type, BaseShareInfo baseShareInfo, ShareResult shareResult);
}
