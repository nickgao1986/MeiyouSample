package com.meiyou.message.mipush;

import android.content.Context;
import android.os.Bundle;

import nickgao.com.messages.PushAdapter;
import nickgao.com.messages.PushClientType;

/**
 * mi push adapter
 * Created by hxd on 15/12/10.
 */
public class MiPushAdapter implements PushAdapter {


    private Bundle mBundle=new Bundle();

    public MiPushAdapter(Bundle bundle) {
        if (bundle != null) {
            this.mBundle.putAll(bundle);
        }
    }

    public static Bundle getInitParams(String appkey, String appId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_APP_KEY, appkey);
        bundle.putString(KEY_APP_ID, appId);
        return bundle;
    }


    @Override
    public void init(Context context, Bundle bundle) {
        XiaomiClientManager.getInstance().init(
                context, bundle.getString(KEY_APP_ID), bundle.getString(KEY_APP_KEY));
    }

    @Override
    public void registerUser(long userId) {
        XiaomiClientManager.getInstance().registerUser(userId);
    }

    @Override
    public void registerUser(long userId, boolean isTest) {
        XiaomiClientManager.getInstance().registerUser(userId,isTest);
    }

    @Override
    public void unRegister() {
        //XiaomiClientManager.getInstance().unRegisterUser();
    }

    @Override
    public int getType() {
        return PushClientType.PUSH_TYPE_XIAOMI;
    }
}
