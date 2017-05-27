package com.meiyou.framework.share;


import com.umeng.socialize.bean.SHARE_MEDIA;

import nickgao.com.framework.utils.BeanManager;
import nickgao.com.meiyousample.R;


/**
 * Created by hxd on 15/7/27.
 */
public enum ShareType {
    /**
     * * 1:微博分享
     * 2:微信好友分享
     * 3:微信朋友圈分享
     * 4:QQ空间分享
     * 5:QQ分享
     * 6:短信
     * 7 美柚蜜友圈
     * 8: 复制链接
     */

    SINA(R.string.share_sina, R.drawable.all_share_btn_weibo, "fx-xlwb", SHARE_MEDIA.SINA, 1),
    WX_FRIENDS(R.string.share_wx_friends, R.drawable.all_share_btn_wechat, "fx-wxhy", SHARE_MEDIA.WEIXIN, 2),
    WX_CIRCLES(R.string.share_wx_circles, R.drawable.all_share_btn_moments, "fx-wxpyq", SHARE_MEDIA.WEIXIN_CIRCLE, 3),
    QQ_ZONE(R.string.share_qq_zone, R.drawable.all_share_btn_qzone, "fx-qqkj", SHARE_MEDIA.QZONE, 4),
    QQ_FRIENDS(R.string.share_qq_friends, R.drawable.all_share_btn_qq, "fx-qq", SHARE_MEDIA.QQ, 5),
    SMS(R.string.share_sms, R.drawable.apk_more_message, "fx-sms", SHARE_MEDIA.SMS, 6),
    SHARE_TALK(R.string.share_talk, R.drawable.all_share_btn_myfeed, "fx-talk", null, 7),
    COPY_URL(R.string.share_copy_url, R.drawable.all_share_btn_copylink, "fx-copy",
            null, 8);
    private int titleId;
    private int iconId;
    private String traceString;
    private SHARE_MEDIA shareMedia;
    private final int mShareType;
    private int mHashCode;
    private ShareType(int titleId, int iconId, String traceString,
                      SHARE_MEDIA shareMedia, int shareType) {
        this.titleId = titleId;
        this.iconId = iconId;
        this.traceString = traceString;
        this.shareMedia = shareMedia;
        this.mShareType = shareType;
    }

    public int getTitleId() {
        return titleId;
    }

    public String getTraceString() {
        return traceString;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setTraceString(String traceString) {
        this.traceString = traceString;
    }

    public int getIconId() {
        return iconId;
    }

    /**
     * 经期的shareType,已经持久化,底层和经期不一致,于是进行兼容
     * package com.lingan.seeyou.share;
     * public class ShareLoginType {
     * public static final int WX_LOGIN = 3;
     * public static final int SINA_LOGIN = 2;
     * public static final int QZONE_LOGIN = 1;
     * public static final int QQ= 4;
     * public static final int LOGIN_ERROR = -1;
     * }
     **/
    public final int getShareType() {
        if (BeanManager.getUtilSaver().getPlatFormAppId().equals("0") || BeanManager.getUtilSaver().getPlatFormAppId().equals("1")//经期
                || BeanManager.getUtilSaver().getPlatFormAppId().equals("2")|| BeanManager.getUtilSaver().getPlatFormAppId().equals("8")//柚宝宝
                ) {
            //微信登录
            if (mShareType == 2)
                return 3;
            //新浪
            if (mShareType == 1)
                return 2;
            //QQ
            if (mShareType == 4)
                return 1;
        }


        return mShareType;
    }

    /**
     * 不转换ShareType;
     * @return
     */
    public final int getShareTypeOrigin() {
        return mShareType;
    }

    public SHARE_MEDIA getShareMedia() {
        return shareMedia;
    }

    /**
     * 获取不含密友圈的选项
     *
     * @return
     */
    public static ShareType[] getDefaultShareTypeValues() {
        return new ShareType[]{ShareType.WX_CIRCLES, ShareType.WX_FRIENDS, ShareType.QQ_FRIENDS, ShareType.QQ_ZONE, ShareType.SINA, ShareType.SMS};
    }

    public static ShareType getShareType(SHARE_MEDIA shareMedia) {
        for (ShareType type : ShareType.values()) {
            if (type.getShareMedia() == shareMedia) {
                return type;
            }
        }
        return null;
    }

    public int getHashCode() {
        return mHashCode;
    }

    public ShareType setHashCode(int hashCode) {
        mHashCode = hashCode;
        return this;
    }
}
