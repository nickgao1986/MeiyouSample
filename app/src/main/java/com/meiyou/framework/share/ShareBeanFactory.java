package com.meiyou.framework.share;

import android.app.Activity;

import com.meiyou.framework.share.data.BaseShareInfo;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import nickgao.com.framework.utils.LogUtils;

/**
 * 管理  ShareItemController bean
 * Created by hxd on 15/7/28.
 */
public class ShareBeanFactory {
    //TODO 静态变量 注意内存泄露
    private static Map<String, ShareItemController> controllerMap = new HashMap<>();

    public static String getKey(ShareType type, BaseShareInfo shareInfoDO) {
        return type.name() + (shareInfoDO == null ? "" : shareInfoDO.hashCode());
    }


    public static ShareItemController getController(String key){
        return controllerMap.get(key);
    }

    public static ShareItemController getController(ShareType type, Activity activity, BaseShareInfo shareInfoDO) {
        String key = getKey(type, shareInfoDO);
        ShareItemController controller = controllerMap.get(key);
        if (controller == null) {
            Class clazz = SocialService.getInstance().getConfig().getControllerByType(type);
            try {
                Constructor<?> constructor = clazz.getConstructor(Activity.class,BaseShareInfo.class);
                controller = (ShareItemController) constructor.newInstance(activity,shareInfoDO);
            } catch (Exception e) {
                LogUtils.e(e.getLocalizedMessage());
            }
            controllerMap.put(key, controller);
        }
        return controller;
    }
}
