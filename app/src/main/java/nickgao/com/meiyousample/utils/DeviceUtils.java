package nickgao.com.meiyousample.utils;

import android.content.Context;

/**
 * Created by gaoyoujian on 2017/3/5.
 */

public class DeviceUtils {


    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5F);
    }

    public static int getScreenWidth(Context context) {
        try {
            return context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception var2) {
            var2.printStackTrace();
            return 480;
        }
    }

    public static int getScreenHeight(Context context) {
        try {
            return context.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception var2) {
            var2.printStackTrace();
            return 800;
        }
    }
}
