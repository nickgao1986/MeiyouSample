package com.meiyou.message.mipush;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

import nickgao.com.framework.utils.Base64Str;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.messages.MessageController;

/**
 * Created by WenHui on 2015/9/2.
 */
public class XiaomiReceiver extends PushMessageReceiver {
    private static final String TAG = "XiaomiReceiver";
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mStartTime;
    private String mEndTime;
    private Context mContext;
    private String mRegId;


    @Override
    public void onReceiveMessage(Context context, MiPushMessage message) {
        mContext = context;
        MessageController.getInstance().setContext(mContext);
        mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
        //通知栏推送点击
       /* if(message.isNotified() && message.getPassThrough()==0){
            String content = message.getContent();
            String messageId = message.getMessageId();
            handleClickData(content, messageId);
        }*/
    }


    private void handleReceiveData(String str,String messageId) {
        try {
            String strDecode = new String(Base64Str.decode(str));
            LogUtils.d(TAG, "===》handleReceiveData:" + strDecode);
            //handlePushData(strDecode, str,messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理通知栏点击
     */
//    private void handleClickData(String str,String messageId){
//        try {
//            String strDecode = new String(Base64Str.decode(str));
//            LogUtils.d(TAG, "===》handleClickData:" + strDecode);
//            PushMsgModel pushModel = new PushMsgModel(strDecode,str);
//            pushModel.msg_id = messageId;
//            pushModel.setClick(true);
//            sendBroadcast(pushModel);
//            //handlePushData(strDecode, str,messageId);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//
//    private void handlePushData(String str ,String strBase64 , String messageId) {
//        try {
//            PushMsgModel pushModel = new PushMsgModel(str,strBase64);
//            pushModel.msg_id = messageId;
//            sendBroadcast(pushModel);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private void sendBroadcast(BaseBizMsgModel basePushModel) {
//        Intent intent = new Intent();
//        intent.setPackage(mContext.getPackageName());
//        intent.setAction(SocketIntentKey.ACTION_SOCKET);
//        intent.putExtra(SocketIntentKey.XIAOMI_KEY, basePushModel.getLeapType()>0?basePushModel.getLeapType():basePushModel.getType());
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(SocketIntentKey.SOCKET_DATA, basePushModel);
//        intent.putExtras(bundle);
//        mContext.sendBroadcast(intent);//发送广播
//    }

    //透传
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        LogUtils.d(TAG, "===》onReceivePassThroughMessage is called. " + message.toString());
        mContext = context;
        MessageController.getInstance().setContext(mContext);
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }

        //透传
        if (message != null && message.getPassThrough() == 1) {
            String content = message.getContent();
            String messageId = message.getMessageId();
            handleReceiveData(content, messageId);
        }
    }

    //通知栏到达
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        //LogUtils.d(TAG, "onNotificationMessageArrived is called. " + message.toString());
//        mContext = context;
//        MessageController.getInstance().setContext(mContext);
//        if (!TextUtils.isEmpty(message.getTopic())) {
//            mTopic = message.getTopic();
//        } else if (!TextUtils.isEmpty(message.getAlias())) {
//            mAlias = message.getAlias();
//        }
//        try {
//            String content = message.getContent();
//            LogUtils.d(TAG, "===》onNotificationMessageArrived content :" + content);
//            String strDecode = new String(Base64Str.decode(content));
//            PushMsgModel pushModel = new PushMsgModel(strDecode,content);
//            pushModel.msg_id = message.getMessageId();
//            //通知外部,小米通知栏消息到达
//            MessageController.getInstance().notifyXiaomiNotificationMessagePushArrivedCallback(pushModel);
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }

    //通知栏点击
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mContext = context;
//        MessageController.getInstance().setContext(mContext);
//        if (!TextUtils.isEmpty(message.getTopic())) {
//            mTopic = message.getTopic();
//        } else if (!TextUtils.isEmpty(message.getAlias())) {
//            mAlias = message.getAlias();
//        }
//        if (message != null) {
//            String content = message.getContent();
//            String messageId = message.getMessageId();
//            handleClickData(content, messageId);
//        }
    }


    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        LogUtils.d(TAG, "===》onReceiveRegisterResult:" + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = "注册成功";
            } else {
                log = "注册失败";
            }
        } else {
            log = message.getReason();
        }
        Message msg = Message.obtain();
        msg.obj = log;
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        mContext = context;
        MessageController.getInstance().setContext(mContext);
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        LogUtils.d(TAG, "===》onCommandResult：" + command);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                LogUtils.d(TAG, "===》收到注册ID：" + cmdArg1);
                mRegId = cmdArg1;
                //通知外部,小米注册成功
                MessageController.getInstance().notifyXiaomiRegCallback(cmdArg1);
            } else {
                LogUtils.d(TAG, "注册失败");
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                LogUtils.d(TAG, "注册别名成功：" + mAlias);
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }
}