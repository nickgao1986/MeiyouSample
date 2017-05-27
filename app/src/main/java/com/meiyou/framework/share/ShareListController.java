package com.meiyou.framework.share;

import android.app.Activity;

import com.meiyou.framework.share.data.BaseShareInfo;

import java.util.ArrayList;
import java.util.List;

import biz.threadutil.ThreadUtil;

/**
 *
 * Created by hxd on 15/7/27.
 */
public class ShareListController {
    private static List<ShareInterceptor> mShareInterceptors = new ArrayList<>();
    private static List<ShareSyncInterceptor> mShareSyncInterceptors = new ArrayList<>();
    private ShareListController() {
    }

    static class Holder {
        static ShareListController controller = new ShareListController();
    }

    public static ShareListController getInstance() {
        return Holder.controller;
    }

    public static ShareItemController onShare(final Activity context, final ShareType type, BaseShareInfo shareInfoDO) {

        return onShare(context,type,shareInfoDO,null);
    }
    public static ShareItemController onShare(final Activity context, final ShareType type, BaseShareInfo shareInfoDO, final ShareResultCallback callback) {
        //调用拦截器
        BaseShareInfo tempInfo = invokeInterceptorBefore(context,type,shareInfoDO);
        if(tempInfo!=null){
            shareInfoDO=tempInfo;
        }

        if(mShareSyncInterceptors.size()>0){
            final BaseShareInfo oldShareInfo = shareInfoDO;
            //调用异步拦截器
            ThreadUtil.addTaskForIO(context, "", new ThreadUtil.ITasker() {
                @Override
                public Object onExcute() {
                    return invokeSyncInterceptorBefore(context,type,oldShareInfo);
                }

                @Override
                public void onFinish(Object result) {
                    BaseShareInfo shareInfo = (BaseShareInfo)result;
                    //onShare(context,type,shareInfo,null);
                    final ShareItemController controller = ShareBeanFactory.getController(type, context, shareInfo);
                    if(callback!=null && controller!=null)
                        controller.addCallback(callback);
                    if(controller!=null){
                        controller.startShareWithCheckAuth();
                    }
                }
            });
            final ShareItemController controller = ShareBeanFactory.getController(type, context, shareInfoDO);
            return controller;
        }else{
            final ShareItemController controller = ShareBeanFactory.getController(type, context, shareInfoDO);
            if(callback!=null && controller!=null)
                controller.addCallback(callback);
            if(controller!=null){
                controller.startShareWithCheckAuth();
            }
            return controller;
        }
    }

    /**
     * 新增拦截器
     * @param shareInterceptor
     */
    public static void addShareInterceptor(ShareInterceptor shareInterceptor){
        if(mShareInterceptors==null)
            mShareInterceptors = new ArrayList<>();
        mShareInterceptors.add(shareInterceptor);
    }

    /**
     * 移除拦截器
     * @param shareInterceptor
     * @return
     */
    public static boolean removeShareInterceptor(ShareInterceptor shareInterceptor){
        try {
            if(mShareInterceptors==null)
                mShareInterceptors = new ArrayList<>();
            return mShareInterceptors.remove(shareInterceptor);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }



    public static void invokeInterceptorShowShare(final Activity context, BaseShareInfo shareInfoDO){
        try {
            BaseShareInfo shareInfoTemp = shareInfoDO;
            //顺序调用
            for(ShareInterceptor shareInterceptor:mShareInterceptors){
                 shareInterceptor.showShare(context,shareInfoTemp);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static BaseShareInfo invokeInterceptorBefore(final Activity context, ShareType type, BaseShareInfo shareInfoDO){
        try {
            BaseShareInfo shareInfoTemp = shareInfoDO;
            //顺序调用
            for(ShareInterceptor shareInterceptor:mShareInterceptors){
                BaseShareInfo temp = shareInterceptor.beforeExecute(context,type,shareInfoTemp);
                if(temp!=null){
                    shareInfoTemp = temp;
                }
            }
            return shareInfoTemp;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static BaseShareInfo invokeInterceptorAfter(ShareType type, BaseShareInfo shareInfoDO, ShareResult shareResult){
        try {
            BaseShareInfo shareInfoTemp = shareInfoDO;
            //顺序调用
            for(ShareInterceptor shareInterceptor:mShareInterceptors){
                BaseShareInfo temp = shareInterceptor.afterExecute(type,shareInfoTemp,shareResult);
                if(temp!=null){
                    shareInfoTemp = temp;
                }
            }
            return shareInfoTemp;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 新增拦截器
     * @param shareInterceptor
     */
    public static void addShareSyncInterceptor(ShareSyncInterceptor shareInterceptor){
        if(mShareSyncInterceptors==null)
            mShareSyncInterceptors = new ArrayList<>();
        mShareSyncInterceptors.add(shareInterceptor);
    }

    /**
     * 移除拦截器
     * @param shareInterceptor
     * @return
     */
    public static boolean removeShareSyncInterceptor(ShareSyncInterceptor shareInterceptor){
        try {
            if(mShareSyncInterceptors==null)
                mShareSyncInterceptors = new ArrayList<>();
            return mShareSyncInterceptors.remove(shareInterceptor);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }


    public static BaseShareInfo invokeSyncInterceptorBefore(final Activity context, ShareType type, BaseShareInfo shareInfoDO){
        try {
            BaseShareInfo shareInfoTemp = shareInfoDO;
            //顺序调用
            for(ShareSyncInterceptor shareInterceptor:mShareSyncInterceptors){
                BaseShareInfo temp = shareInterceptor.beforeSyncExecute(context,type,shareInfoTemp);
                if(temp!=null){
                    shareInfoTemp = temp;
                }
            }
            return shareInfoTemp;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static BaseShareInfo invokeSyncInterceptorAfter(ShareType type, BaseShareInfo shareInfoDO, ShareResult shareResult){
        try {
            BaseShareInfo shareInfoTemp = shareInfoDO;
            //顺序调用
            for(ShareSyncInterceptor shareInterceptor:mShareSyncInterceptors){
                BaseShareInfo temp = shareInterceptor.afterExecute(type,shareInfoTemp,shareResult);
                if(temp!=null){
                    shareInfoTemp = temp;
                }
            }
            return shareInfoTemp;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
