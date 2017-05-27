package com.meiyou.framework.share.controller;

import android.app.Activity;
import android.os.AsyncTask;

import com.meiyou.framework.share.BizResult;
import com.meiyou.framework.share.ShareItemController;
import com.meiyou.framework.share.ShareType;
import com.meiyou.framework.share.data.BaseShareInfo;
import com.meiyou.framework.share.data.ShareImage;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.media.SimpleShareContent;
import com.umeng.socialize.media.UMImage;

import java.io.File;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;

/**
 * 新浪分享图片 缩略图<32K  大图 < 2M
 * Created by hxd on 15/7/28.
 */
public class ShareSinaController extends ShareItemController/*ShareWithEditViewController*/ {
    long limit = 1024 * 1024;
    private AsyncTask asyncTask;

    public ShareSinaController(Activity activity, BaseShareInfo shareInfoDO) {
        super(activity, shareInfoDO);
    }

    protected String getTopTitle() {
        if (!StringUtils.isBlank(shareInfoDO.getTopTitle())) {
            return shareInfoDO.getTopTitle();
        }
        return context.getResources().getString(R.string.share_title_sina);
    }

    @Override
    public ShareType getShareType() {
        return ShareType.SINA;
    }


    @Override
    protected SimpleShareContent genShareContent() {
        //QQShareContent qqShareContent = new QQShareContent();
        SimpleShareContent shareContent = new SimpleShareContent(new ShareContent());
        shareContent.setTitle(shareInfoDO.getTitle());
        String newContent = shareInfoDO.getContent() /*+ (StringUtils.isEmpty(shareInfoDO.getUrl()) ? "" : shareInfoDO.getUrl())*/;
        shareContent.setText(handleAppendFrom(newContent));
        shareContent.setTargeturl(shareInfoDO.getUrl());
        setShareMediaInfo(shareContent);

        return shareContent;
    }


    public boolean actionDriven() {
        return false;
    }

    @Override
    protected BizResult<String> startShareView() {
        return null;
    }

    @Override
    protected SimpleShareContent setImage(SimpleShareContent baseShareContent) {
        ShareImage shareImageInfoDO = (ShareImage) shareInfoDO.getShareMediaInfo();
        if (shareImageInfoDO == null) {
            return baseShareContent;
        }
        UMImage image = null;
        if (StringUtils.isNotEmpty(shareImageInfoDO.getImageUrl())) {
            if (shareImageInfoDO.getImageUrl().startsWith("/")) {
                File file = new File(shareImageInfoDO.getImageUrl());
                image = new UMImage(context, file);
            } else if (shareImageInfoDO.getImageUrl().startsWith("file://")) {
                String orginPath = shareImageInfoDO.getImageUrl();
                String path = orginPath.substring(7, orginPath.length());
                File file = new File(path);
                image = new UMImage(context, file);
            } else if (shareImageInfoDO.getImageUrl().startsWith("http")) {
                image = new UMImage(context, shareImageInfoDO.getImageUrl());
            }
            image.setTargetUrl(shareImageInfoDO.getImageUrl());
        }
        if (shareImageInfoDO.hasLocalImage()) {
            image = new UMImage(context, shareImageInfoDO.getLocalImage());
        }
        if (image != null) {
            baseShareContent.setImage(image);
        }

        return baseShareContent;
    }

    int getMaxCount() {
        return 280;
    }

    private String handleAppendFrom(String content) {
        if (shareInfoDO.isPatchTextFrom()) {
            content += " " + (StringUtils.isEmpty(shareInfoDO.getFrom()) ? "" : "@" + shareInfoDO.getFrom());
        }
        return content;
    }

    protected boolean needAuth() {
        return false;
    }

    @Override
    protected BizResult<String> doShare() {
        //后台分享
        if (shareInfoDO != null && shareInfoDO.isDirectShare()) {
            Config.OpenEditor = false;//此参数对微信和QQ无效
        }else{
            Config.OpenEditor = true;//此参数对微信和QQ无效
        }
        return super.doShare();
    }

}
