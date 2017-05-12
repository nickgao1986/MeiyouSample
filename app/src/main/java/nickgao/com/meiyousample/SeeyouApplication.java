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

        ImageLoader.initialize(this, false);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getContext() {
        return mContext;
    }
}
