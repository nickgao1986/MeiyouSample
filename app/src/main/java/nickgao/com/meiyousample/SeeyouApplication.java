package nickgao.com.meiyousample;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.meetyou.crsdk.util.ImageLoader;
import com.meetyou.media.player.client.MeetyouPlayerEngine;
import com.meiyou.sdk.common.database.BaseContentResolver;

import nickgao.com.framework.utils.BeanManager;
import nickgao.com.framework.utils.SeeyouBeanFactory;
import nickgao.com.meiyousample.contentprovider.SeeyouContentProvider;


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
        SeeyouBeanFactory.init();
        BeanManager.getUtilSaver().setContext(mContext);
        new BaseContentResolver(this, SeeyouContentProvider.SEEYOU_AUTHORITY);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getContext() {
        return mContext;
    }
}
