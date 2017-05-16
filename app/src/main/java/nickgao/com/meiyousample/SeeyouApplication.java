package nickgao.com.meiyousample;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.meetyou.crsdk.util.ImageLoader;
import com.meetyou.media.player.client.MeetyouPlayerEngine;


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

//        Bundle bundle = MiPushAdapter.getInitParams(GlobalConfig.XIAOMI_APP_KEY,GlobalConfig.XIAOMI_APP_ID);
//        MessageController.getInstance().init(this, PushClientType.PUSH_TYPE_XIAOMI,bundle);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getContext() {
        return mContext;
    }
}
