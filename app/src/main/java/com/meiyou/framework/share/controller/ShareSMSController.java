package com.meiyou.framework.share.controller;

import android.app.Activity;

import com.meiyou.framework.share.ShareType;
import com.meiyou.framework.share.data.BaseShareInfo;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.media.SimpleShareContent;

/**
 * 短信
 * Created by hyx on 2015/9/2.
 */
public class ShareSMSController extends ShareWithoutEidtViewController{
    public ShareSMSController(Activity activity, BaseShareInfo shareInfoDO) {
        super(activity, shareInfoDO);
    }

    @Override
    public ShareType getShareType() {
        return ShareType.SMS;
    }

   /* @Override
    protected SimpleShareContent genShareContent() {
        RealSmsShareContent smsContent = new RealSmsShareContent();
        smsContent.setShareContent(shareInfoDO.getContent());
        setShareMediaInfo(smsContent);
        return smsContent;
    }*/

    @Override
    protected SimpleShareContent genShareContent() {
        //QQShareContent qqShareContent = new QQShareContent();
        SimpleShareContent shareContent = new SimpleShareContent(new ShareContent());
        shareContent.setTitle(shareInfoDO.getTitle());
        shareContent.setText(shareInfoDO.getContent());
        shareContent.setTargeturl(shareInfoDO.getUrl());
        setShareMediaInfo(shareContent);
        return shareContent;
    }

}
