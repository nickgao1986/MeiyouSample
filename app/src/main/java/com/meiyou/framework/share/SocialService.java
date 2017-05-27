package com.meiyou.framework.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.meiyou.framework.share.data.BaseShareInfo;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Iterator;
import java.util.Map;

import nickgao.com.framework.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/5/27.
 */

public class SocialService {

    private Config config;
    private UMShareAPI mController;

    public static SocialService getInstance() {
        return Holder.instance;
    }

    static class Holder {
        static SocialService instance = new SocialService();
    }

    private SocialService() {
        config = new Config();
    }

    /**
     * 在每一个使用SocialService 的Activity里面调用
     *
     * @param activity
     */
    public SocialService prepare(Activity activity) {
        //这里全部初始化，后续可以按需加载
        initKey();
        return this;
    }
    public void initKey(){
        //这里全部初始化，后续可以按需加载
        attachSina()
                .attachShareQQ()
                .attachShareQQZone()
                .attachShareWXCircles()
                .attachShareWXFriends()
                .attachShareSms();
    }
    // 支持微信朋友圈
    private SocialService attachShareWXCircles() {
        PlatformConfig.setWeixin(config.getAppConfigMap().get(ShareType.WX_CIRCLES).appId, config.getAppConfigMap().get(ShareType.WX_CIRCLES).appKey);

        return this;
    }

    // 添加微信平台
    private SocialService attachShareWXFriends() {
        PlatformConfig.setWeixin( config.getAppConfigMap().get(ShareType.WX_FRIENDS).appId, config.getAppConfigMap().get(ShareType.WX_FRIENDS).appKey);

        return this;
    }

    //添加qq
    private SocialService attachShareQQ() {
        PlatformConfig.setQQZone(config.getAppConfigMap().get(ShareType.QQ_ZONE).appId, config.getAppConfigMap().get(ShareType.QQ_ZONE).appKey);

        return this;
    }

    //添加qq空间
    private SocialService attachShareQQZone(){
        PlatformConfig.setQQZone(config.getAppConfigMap().get(ShareType.QQ_ZONE).appId, config.getAppConfigMap().get(ShareType.QQ_ZONE).appKey);

        return this;
    }

    //添加新浪
    private SocialService attachSina() {
        PlatformConfig.setSinaWeibo(config.getAppConfigMap().get(ShareType.SINA).appId, config.getAppConfigMap().get(ShareType.SINA).appKey);
        return this;
    }

    //添加短信分享
    private SocialService attachShareSms(){
        return this;
    }

    public ShareListDialog showShareDialog(Activity activity, BaseShareInfo shareInfoDO, ShareTypeChoseListener listener) {
        try {
            ShareListDialog dialog = new ShareListDialog(activity, shareInfoDO, listener);
            dialog.show();
            return dialog;

        } catch (Exception e) {
            LogUtils.e(e.getLocalizedMessage());
        }
        return  null;
    }


    /**
     * 更多参数建议使用ShareListDialog的builder构建
     * @see  ShareListDialog#newBuilder()
     */
    public void showShareDialog(ShareListDialog shareListDialog) {
        try {
            shareListDialog.show();
        } catch (Exception e) {
            LogUtils.e(e.getLocalizedMessage());
        }
    }

    public ShareItemController directShare(Activity activity,ShareType shareType, BaseShareInfo shareInfoDO) {
        if(shareInfoDO!=null)
            shareInfoDO.setDirectShare(true);
        return ShareListController.onShare(activity, shareType, shareInfoDO);
    }
    public ShareItemController directShareWithUI(Activity activity,ShareType shareType, BaseShareInfo shareInfoDO) {
        return ShareListController.onShare(activity, shareType, shareInfoDO);
    }

    public Config getConfig() {
        return config;
    }

    public void doAuthVerify(Activity activity, final ShareType shareType,
                             final AuthListener authListener) {
        if(authListener!=null)
            authListener.onStart(shareType);
        mController.doOauthVerify(activity,shareType.getShareMedia(),new UMAuthListener(){

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Bundle bundle = new Bundle();
                if(map!=null){
                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String)entry.getKey();
                        String value = (String)entry.getValue();
                        bundle.putCharSequence(key,value);
                    }
                }
                if(authListener!=null)
                    authListener.onComplete(bundle, shareType);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if(authListener!=null)
                    authListener.onError(new AuthException(i, throwable.getMessage()), shareType);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if(authListener!=null)
                    authListener.onCancel(shareType);
            }
        });

    }

    public void getPlatformInfo(Activity activity,ShareType shareType, final GetDataListener getDataListener) {
        //开始获取
        if(getDataListener!=null)
            getDataListener.onStart();
        //类型纠正
        SHARE_MEDIA share_media = shareType.getShareMedia();
        if(share_media==SHARE_MEDIA.QZONE){
            share_media = SHARE_MEDIA.QQ;
        }
        if(shareType==ShareType.WX_FRIENDS || shareType==ShareType.WX_CIRCLES){
            share_media = SHARE_MEDIA.WEIXIN;
        }
        mController.getPlatformInfo(activity, share_media, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if(getDataListener!=null)
                    getDataListener.onComplete(i, map);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if(getDataListener!=null)
                    getDataListener.onError(i,throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if(getDataListener!=null)
                    getDataListener.onCancle();
            }
        });
    }

    /**
     * 在有可能出现 sso授权 的Activity 的onActivityResult 方法里调用
     * 比如 可能需要 新浪微博/qqzone   分享/授权
     * @param requestCode
     * @param resultCode
     * @param data
     */
    /**
     * 在有可能出现 sso授权 的Activity 的onActivityResult 方法里调用
     * 比如 可能需要 新浪微博/qqzone   分享/授权
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(mController!=null)
            mController.onActivityResult(requestCode, resultCode, data);
    }

    public UMShareAPI getUMSocialService() {
        return mController;
    }

    //TODO MODIFY
    public boolean getWechatInstalled(Context context) {
        return isAppInstall(context.getApplicationContext(),"com.tencent.mm");
    }

    public boolean getQQInstalled(Context context) {
        //return qqSsoHandler.isClientInstalled();
        return isAppInstall(context.getApplicationContext(),"com.tencent.mobileqq");

    }

    private boolean isAppInstall(Context context,String packagename){
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packagename, 0);
            if (info==null){
                return false;
            }else {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
       /* final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packagename)) {
                    return true;
                }
            }
        }
        return false;*/
    }


}
