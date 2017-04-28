package fresco.painter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gaoyoujian on 2017/4/26.
 */

public class ImageLoaderConfigure {

    private static final String sTAG = "ImageLoaderConfigure";
    private static final String CONF_SP_NAME = "conf-image-loader";
    private static final String CONF_KEY = "switch";
    private static final int DEFAULT_VALUE = 0;
    private int mConfigValue = DEFAULT_VALUE;

    public enum Configure {
        FRESCO(100),
        PICASSO(10),
        UIL(1),;

        private Configure(int flag) {
            this.flag = flag;
        }

        public int getFlagValue() {
            return flag;
        }

        private int flag;
    }

    public boolean configValid() {
        return mConfigValue >= DEFAULT_VALUE;
    }

    /**
     * FRESCO | PICASSO | UIL
     * 1        1       1
     *
     * @param context
     * @param value
     */
    public void saveImageLoaderConfig(Context context, int value) {
        SharedPreferences sp = context.getSharedPreferences(CONF_SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(CONF_KEY, value).apply();
    }

    private int getConfValue(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONF_SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(CONF_KEY, 0);
    }

    /**
     * 本应该按照配置来决定
     * 但是目前无法做到完全排出UIL 所以这里还是会同时启用UIL
     *
     * @return
     */
    public boolean useUIL() {
        return true;
    }

    /**
     * 如果是888就是painter
     * @return
     */
    public boolean useFrescoPainter(){
        return mConfigValue == 888;
    }

    public boolean usePicasso() {
        int flag = mConfigValue % 100;
        return flag / Configure.PICASSO.getFlagValue() >= 1;
    }

    public boolean useFresco() {
        int flag = mConfigValue % 1000;
        return mConfigValue == DEFAULT_VALUE
                || flag / Configure.FRESCO.getFlagValue() >= 1;
    }

    public void initConfig(Context context) {
        mConfigValue = getConfValue(context);
       // LogUtils.d(sTAG, "config " + mConfigValue);
    }

}
