package com.meiyou.framework.share.controller;

import android.app.Activity;

import com.meiyou.framework.share.BizResult;
import com.meiyou.framework.share.ShareType;
import com.meiyou.framework.share.SocialService;
import com.meiyou.framework.share.data.BaseShareInfo;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.media.SimpleShareContent;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.skin.ToastUtils;

/**
 * qq friends
 * Created by hyx on 2015/9/2.
 */
public class ShareQQFriendsController extends ShareWithoutEidtViewController {
    public ShareQQFriendsController(Activity activity, BaseShareInfo shareInfoDO) {
        super(activity, shareInfoDO);
    }

    @Override
    public ShareType getShareType() {
        return ShareType.QQ_FRIENDS;
    }

   /* @Override
    protected BaseShareContent genShareContent() {
        QQShareContent shareContent = new QQShareContent();
        shareContent.setTitle(shareInfoDO.getTitle());
        shareContent.setShareContent(shareInfoDO.getContent());
        shareContent.setTargetUrl(shareInfoDO.getUrl());
        setShareMediaInfo(shareContent);
        return shareContent;
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

    @Override
    protected BizResult<String> doShare() {
        if (!SocialService.getInstance().getQQInstalled(context)){
            ToastUtils.showToast(context, context.getResources().getString(R.string.share_failed_qq_uninstalled));
            BizResult<String> result = new BizResult<>();
            result.setSuccess(false);
            return result;
        }
        return super.doShare();
    }
}
