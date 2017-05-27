package com.meiyou.framework.share.controller;

import android.app.Activity;

import com.meiyou.framework.share.BizResult;
import com.meiyou.framework.share.ShareItemController;
import com.meiyou.framework.share.data.BaseShareInfo;

/**
 *
 * Created by hxd on 15/7/28.
 */
public abstract class ShareWithoutEidtViewController extends ShareItemController {

    public ShareWithoutEidtViewController(Activity activity, BaseShareInfo shareInfoDO) {
        super(activity,shareInfoDO);
    }

    @Override
    protected BizResult<String> startShareView() {
       return  null;
    }

    @Override
    public boolean actionDriven() {
        return false;
    }
}
