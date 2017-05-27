package com.meiyou.framework.share.data;

import java.io.Serializable;

/**
 * Created by hxd on 16/7/18.
 */
public class BaseShareInfo implements Serializable {
    /**
     * 分享页面上方的标题
     */
    protected String topTitle;
    /**
     * 分享的标题
     */
    protected String title;
    /**
     * 分享的内容
     */
    protected String content;
    /**
     * 分享的网址链接
     */
    protected String url;

    /**
     * 从哪里来
     */
    protected String from;

    protected ShareMediaInfo mShareMediaInfo;

    protected String currentUrl;
    /**
     * 是否自动拼接 @ from
     */
    protected boolean patchTextFrom = true;

    protected boolean directShare =false;

    //分享统计token
    protected String ptk;
    //来源
    protected String location;
    /**
     * location	对应功能模块位置	经期	孕期	育儿
     001	今日密报	1
     002	贴士详情	1	1	1
     003	她她圈话题	1	1	1
     004	柚子街商品	1	1	1
     005	幸运转盘	1
     006	试用中心	1
     007	任务中心	1
     008	星座运势	1
     009	能不能吃	1	1	1
     010	卡路里计算器	1
     011	姨妈神庙	1
     012	送子神庙	1
     013	月老阁	1
     014	资讯类	1	1
     015	小说类	1
     101	宝宝发育	1	1
     102	妈妈变化	1	1
     105	胎动看男女	1	1
     106	身份切换	1	1
     107	分享应用	1	1
     108	拉新测一测	1	1
     301	照片分享		1	1
     303	宝宝辅食		1	1
     304	潜能测评		1	1
     */

    protected ShareActionConf mActionConf = new ShareActionConf();


    public String getTopTitle() {
        return topTitle;
    }

    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isPatchTextFrom() {
        return patchTextFrom;
    }

    public void setPatchTextFrom(boolean patchTextFrom) {
        this.patchTextFrom = patchTextFrom;
    }

    public ShareActionConf getActionConf() {
        return mActionConf;
    }

    public void setActionConf(ShareActionConf actionConf) {
        mActionConf = actionConf;
    }

    public ShareMediaInfo getShareMediaInfo() {
        return mShareMediaInfo;
    }

    public void setShareMediaInfo(ShareMediaInfo shareMediaInfo) {
        mShareMediaInfo = shareMediaInfo;
    }

    public boolean isDirectShare() {
        return directShare;
    }

    public void setDirectShare(boolean directShare) {
        this.directShare = directShare;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPtk() {
        return ptk;
    }

    public void setPtk(String ptk) {
        this.ptk = ptk;
    }
}
