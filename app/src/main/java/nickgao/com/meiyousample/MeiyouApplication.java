package nickgao.com.meiyousample;

import android.app.Application;
import android.content.Context;

/**
 * Created by gaoyoujian on 2017/3/16.
 */

public class MeiyouApplication extends Application {


    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
