package com.meiyou.framework.share;

import com.meiyou.framework.share.controller.ShareQQFriendsController;
import com.meiyou.framework.share.controller.ShareSMSController;
import com.meiyou.framework.share.controller.ShareSinaController;
import com.meiyou.framework.share.controller.ShareWxCirclesController;
import com.meiyou.framework.share.controller.ShareWxFriendsController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hxd on 15/7/30.
 */
public class Config {

    private Map<ShareType, Class<?>> controllerBeanMap = new HashMap<>();
    private Map<ShareType, AppConfig> appConfigMap = new HashMap<>();

    public Config() {
        controllerBeanMap.put(ShareType.QQ_FRIENDS, ShareQQFriendsController.class);
        //controllerBeanMap.put(ShareType.QQ_ZONE, ShareQQZoneController.class);
        controllerBeanMap.put(ShareType.SINA, ShareSinaController.class);
        controllerBeanMap.put(ShareType.WX_CIRCLES, ShareWxCirclesController.class);
        controllerBeanMap.put(ShareType.WX_FRIENDS, ShareWxFriendsController.class);
        controllerBeanMap.put(ShareType.SMS, ShareSMSController.class);
    }

    public Class<?> getControllerByType(ShareType type) {
        return controllerBeanMap.get(type);
    }

    public void initAppConf(ShareType type, String appId, String appKey) {
        this.initAppConf(type,appId,appKey,null);
    }
    public void initAppConf(ShareType type, String appId, String appKey,String callbackUrl) {
        appConfigMap.put(type, new AppConfig(appId, appKey,callbackUrl));
    }

    public Map<ShareType, Class<?>> getControllerBeanMap() {
        return controllerBeanMap;
    }

    public Map<ShareType, AppConfig> getAppConfigMap() {
        return appConfigMap;
    }

    public static class AppConfig {
        public String appId;
        public String appKey;
        public String callbackUrl;
        public AppConfig(String appId, String appKey,String callbackUrl) {
            this.appId = appId;
            this.appKey = appKey;
            this.callbackUrl = callbackUrl;
        }

        public AppConfig(String appId, String appKey) {
            this(appId,appKey,null);
        }

    }
}
