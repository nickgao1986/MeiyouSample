package com.meiyou.message.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meiyou.message.dispatcher.MeetyouPushDispatcher;
import com.meiyou.message.model.SocketIntentKey;
import com.meiyou.message.processor.MsgProcessor;

import java.util.List;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;

/**
 * Author: lwh
 * Date: 1/3/17 18:10.
 * 推送接收器;
 */

public class MeetyouPushReceiver extends BroadcastReceiver {

    public static final String TAG = "MeetyouPushReceiver";
    private MeetyouPushDispatcher mMeetyouPushDispatcher;
    public MeetyouPushReceiver() {

    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            //获取action和type;
            String action = intent.getAction();
            //intent.hasExtra(SocketIntentKey.SOCKET_KEY);
            //小米推送发送过来的是leap_type(123,但model携带了push_type等信息);socket发送过来的的type(SocketMessageType)
            int    socketKey   = intent.getIntExtra(SocketIntentKey.SOCKET_KEY, 0);
            int    xiaomiKey   = intent.getIntExtra(SocketIntentKey.XIAOMI_KEY, 0);
            LogUtils.d(TAG,"MeetyouPushReceiver action:"+action+"===>socketKey:"+socketKey+"==>xiaomiKey:"+xiaomiKey);
            //LogUtils.d(TAG,"流程跟踪:MeetyouPushReceiver onReceive:"+action+"===>socketKey:"+socketKey+"==>xiaomiKey:"+xiaomiKey);
            if (!StringUtils.equals(action, SocketIntentKey.ACTION_SOCKET)) {
                LogUtils.d(TAG,"MeetyouPushReceiver action异常!!!!!");
            }
            //创建消息分发器
            if(mMeetyouPushDispatcher==null){
                mMeetyouPushDispatcher = new MeetyouPushDispatcher(context.getApplicationContext());
            }
            //消息处理
            List<MsgProcessor> msgProcessorList = mMeetyouPushDispatcher.dispatch(intent);
            if(msgProcessorList!=null && msgProcessorList.size()>0){
                for (MsgProcessor msgProcessor : msgProcessorList) {
                    msgProcessor.execute();
                }
            }

            /*
            TaskManager.getInstance().submit("receiver", new Runnable() {
                @Override
                public void run() {
                    //获取action和type;
                    String action = intent.getAction();
                    //intent.hasExtra(SocketIntentKey.SOCKET_KEY);
                    //小米推送发送过来的是leap_type(123,但model携带了push_type等信息);socket发送过来的的type(SocketMessageType)
                    int    socketKey   = intent.getIntExtra(SocketIntentKey.SOCKET_KEY, 0);
                    int    xiaomiKey   = intent.getIntExtra(SocketIntentKey.XIAOMI_KEY, 0);
                    LogUtils.d(TAG,"MeetyouPushReceiver action:"+action+"===>socketKey:"+socketKey+"==>xiaomiKey:"+xiaomiKey);
                    LogUtils.d(TAG,"流程跟踪:MeetyouPushReceiver onReceive:"+action+"===>socketKey:"+socketKey+"==>xiaomiKey:"+xiaomiKey);
                    if (!StringUtils.equals(action, SocketIntentKey.ACTION_SOCKET)) {
                        LogUtils.d(TAG,"MeetyouPushReceiver action异常!!!!!");
                    }
                    //创建消息分发器
                    if(mMeetyouPushDispatcher==null){
                        mMeetyouPushDispatcher = new MeetyouPushDispatcher(context.getApplicationContext());
                    }
                    //消息处理
                    List<MsgProcessor> msgProcessorList = mMeetyouPushDispatcher.dispatch(intent);
                    if(msgProcessorList!=null && msgProcessorList.size()>0){
                        for (MsgProcessor msgProcessor : msgProcessorList) {
                            msgProcessor.execute();
                        }
                    }

                }
            });*/
        } catch (Exception ex) {
            LogUtils.e(ex.getLocalizedMessage());
        }
    }
}
