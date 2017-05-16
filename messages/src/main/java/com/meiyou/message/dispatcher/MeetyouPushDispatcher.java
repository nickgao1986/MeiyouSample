package com.meiyou.message.dispatcher;

import android.content.Context;
import android.content.Intent;

import com.meiyou.message.model.PushMsgModel;
import com.meiyou.message.model.SocketIntentKey;
import com.meiyou.message.processor.MsgProcessor;
import com.meiyou.message.processor.XiaomiProcessor;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.LogUtils;

/**
 * Author: lwh
 * Date: 1/4/17 09:01.
 * 消息分发器
 *
 */

public class MeetyouPushDispatcher {
    private static final String TAG = "MeetyouPushDispatcher";
    private Context mContext;
    public MeetyouPushDispatcher(Context context){
        mContext = context;
    }

    /**
     * 分发push消息,并返回处理器;
     * @param intent
     * @return
     */
    public List<MsgProcessor> dispatch(Intent intent) {
        List<MsgProcessor> listResult = new ArrayList<>();
        try {
            //socket和xiaomi只会来其一
            int    socketKey   = -1;
            int    xiaomiKey   = -1;
            if(intent.hasExtra(SocketIntentKey.SOCKET_KEY)){
                socketKey   = intent.getIntExtra(SocketIntentKey.SOCKET_KEY, 0);
            }
            if(intent.hasExtra(SocketIntentKey.XIAOMI_KEY)){
                xiaomiKey   = intent.getIntExtra(SocketIntentKey.XIAOMI_KEY, 0);
            }
            //小米推送发送过来的是leap_type(123,但model携带了push_type等信息);socket发送过来的的type(SocketMessageType)
            if(xiaomiKey>0){
                LogUtils.d(TAG,"===》分发小米数据");
                listResult = handleXiaomiDispatcher(intent);
                return listResult;
            }
//            if(socketKey>0){
//                LogUtils.d(TAG,"===》分发Socket数据");
//                listResult = handleSocketDispatcher(intent);
//                return listResult;
//            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
        return listResult;
    }
    //处理小米事件分发
    private List<MsgProcessor> handleXiaomiDispatcher(Intent intent){
        List<MsgProcessor> listResult = new ArrayList<>();
        try{
            //1 通知栏 + 消息列表, 2 通知栏  3 消息列表
            int leap_type   = intent.getIntExtra(SocketIntentKey.XIAOMI_KEY, 0);
            LogUtils.d(TAG,"===》分发小米数据 leap_type:"+leap_type);
            PushMsgModel pushMsgModel = (PushMsgModel)intent.getSerializableExtra(SocketIntentKey.SOCKET_DATA);
            //小米推送处理
            XiaomiProcessor xiaomiProcessor = new XiaomiProcessor(mContext,pushMsgModel);
            listResult.add(xiaomiProcessor);


        }catch (Exception ex){
            ex.printStackTrace();
        }
        return listResult;
    }


}
