package com.meiyou.framework.share.controller;

import android.app.Activity;

import com.meiyou.framework.share.BizResult;
import com.meiyou.framework.share.ShareType;
import com.meiyou.framework.share.SocialService;
import com.meiyou.framework.share.data.BaseShareInfo;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.media.SimpleShareContent;

/**
 * Created by hxd on 15/7/28.
 */
public class ShareWxFriendsController extends ShareWxController {
    public ShareWxFriendsController(Activity activity, BaseShareInfo shareInfoDO) {
        super(activity, shareInfoDO);
    }

    @Override
    public ShareType getShareType() {
        return ShareType.WX_FRIENDS;
    }

    /*   @Override
    protected SimpleShareContent genShareContent() {
        WeiXinShareContent shareContent = new WeiXinShareContent();
        shareContent.setTitle(shareInfoDO.getTitle());
        shareContent.setTargetUrl(shareInfoDO.getUrl());
        shareContent.setShareContent(shareInfoDO.getContent());
        setShareMediaInfo(shareContent);
        return shareContent;
    }*/

    @Override
    protected SimpleShareContent genShareContent() {
        SimpleShareContent shareContent = new SimpleShareContent(new ShareContent());
        shareContent.setTitle(shareInfoDO.getTitle());
        shareContent.setText(shareInfoDO.getContent());
        shareContent.setTargeturl(shareInfoDO.getUrl());
        setShareMediaInfo(shareContent);
        return shareContent;
    }


    @Override
    public BizResult<Boolean> beforeShare() {
        BizResult<Boolean> bizResult = super.beforeShare();
        if (SocialService.getInstance().getWechatInstalled(context)) {
            return bizResult;
        } else {
            bizResult.setSuccess(false);
            bizResult.setErrorMsg("未安装微信");
            bizResult.setErrorCode(400);
        }
        return bizResult;

    }

}
