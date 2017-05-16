package com.meiyou.message.processor;

import android.content.Context;

import com.meiyou.message.model.PushMsgModel;

import nickgao.com.framework.utils.LogUtils;

/**
 * 小米消息处理器,处理器都是在子线程运行的;
 * Author: lwh
 * Date: 1/4/17 16:00.
 */
public class XiaomiProcessor implements MsgProcessor {

    private static final String TAG = "XiaomiProcessor";
    private Context mContext;
    private PushMsgModel mPushMsgModel;

    public XiaomiProcessor(Context context, PushMsgModel pushMsgModel) {
        mContext = context;
        mPushMsgModel = pushMsgModel;
    }

    @Override
    public void execute() {
        //点击事件
        LogUtils.d("====execute");
    }


}
