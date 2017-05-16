package com.meiyou.message.mipush;

import android.app.ActivityManager;
import android.content.Context;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import nickgao.com.framework.utils.LogUtils;

/**
 * 小米 push 的client端
 * Created by hxd on 15/12/10.
 */
public class XiaomiClientManager {

    private static final String TAG = "XiaomiClientManager";
    private Context context;

    private XiaomiClientManager() {
    }

    static class Holder {
        static XiaomiClientManager instance = new XiaomiClientManager();
    }

    public static XiaomiClientManager getInstance() {
        return Holder.instance;
    }

    /**
     * 小米push client 初始化
     *
     * @param context context
     * @param appId   客户端在小米push注册的appId
     * @param appkey  客户端在小米push注册的appkey
     */
    public void init(Context context, String appId, String appkey) {
        this.context = context;
        if (shouldInit()) {
            MiPushClient.registerPush(context, appId, appkey);
        }
    }

    /**
     * 注册用户
     */
    @Deprecated
    public void registerUser(long usrId) {
        List<String> userList = MiPushClient.getAllAlias(context);
        String newUser = String.valueOf(usrId);
        if (userList != null && userList.contains(newUser)) {
            return;
        } else {
            MiPushClient.getAllAlias(context).clear();
//            MiPushClient.setAlias(context, StringToolUtils.buildString("test_",usrId), "");
            MiPushClient.setAlias(context, String.valueOf(usrId), "");
        }
    }

    /**
     * according to params to set mi push in environment
     *
     * @param usrId
     * @param isTest
     */
    public void registerUser(long usrId, boolean isTest) {
        List<String> userList = MiPushClient.getAllAlias(context);
        String userIdString = String.valueOf(usrId);
        String alias = userIdString;
        if (userList != null && userList.contains(userIdString)) {
            LogUtils.d(TAG, "同名，无需反注册");
        } else {
            //先反注册别名
            List<String> list =  MiPushClient.getAllAlias(context);
            if(list!=null){
                for(String user:list){
                    if(!alias.equals(user)){
                        LogUtils.d(TAG, "xiaomi 反注册别名： "+user);
                        MiPushClient.unsetAlias(context,user,"");
                    }
                }
            }
        }
        MiPushClient.getAllAlias(context).clear();
        //再注册新的
        LogUtils.d(TAG, "xiaomi 注册别名： "+alias);
        MiPushClient.setAlias(context, alias, "");
    }

    public void unRegisterUser() {
        MiPushClient.unregisterPush(context);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
