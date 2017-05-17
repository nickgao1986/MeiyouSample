package nickgao.com.meiyousample;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.meetyou.crsdk.util.ImageLoader;
import com.meetyou.media.player.client.MeetyouPlayerEngine;
import com.meiyou.message.mipush.MiPushAdapter;

import nickgao.com.meiyousample.utils.EmojiConversionUtil;
import nickgao.com.messages.MessageController;
import nickgao.com.messages.PushClientType;


/**
 * Created by gaoyoujian on 2017/3/16.
 */

public class SeeyouApplication extends Application {


    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        MultiDex.install(this);
        MeetyouPlayerEngine.Instance().init(this,true);
       // FreelineCore.init(this);
        ImageLoader.initialize(this, false);

//        new BaseContentResolver(this, SeeyouContentProvider.SEEYOU_AUTHORITY);
//        SeeyouBeanFactory.init();
//        BeanManager.getUtilSaver().setContext(mContext);

        Bundle bundle = MiPushAdapter.getInitParams(GlobalConfig.XIAOMI_APP_KEY,GlobalConfig.XIAOMI_APP_ID);
        MessageController.getInstance().init(this, PushClientType.PUSH_TYPE_XIAOMI,bundle);


        EmojiConversionUtil.getInstace().handleGetEmojiFromCache(mContext);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getContext() {
        return mContext;
    }
}
