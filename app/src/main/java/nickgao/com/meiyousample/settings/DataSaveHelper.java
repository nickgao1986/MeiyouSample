package nickgao.com.meiyousample.settings;

import android.content.Context;
import android.content.SharedPreferences;

import nickgao.com.meiyousample.controller.UserController;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class DataSaveHelper {

    public static final String SETTING_FILE = "data_saver";
    //private static final String TAG = "DataSaveHelper";

    private SharedPreferences preferences;
    private Context mContext;
    private static DataSaveHelper mInstance;

    public static DataSaveHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataSaveHelper(context);
        }
        return mInstance;
    }

    public DataSaveHelper(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.preferences = mContext.getSharedPreferences(SETTING_FILE, 0);

    }

    public void saveSkinNightName(String skinName) {
        //BeanManager.getUtilSaver().saveSkinNightName(this.mContext, skinName);
        this.getEditor().putString("skin_night_name" + UserController.getInstance().getUserId(this.mContext), skinName).commit();
    }


    /**
     * 获取是否是夜间模式
     *
     * @return
     */
    public boolean getIsNightMode() {
        return preferences.getBoolean("is_night_mode" + UserController.getInstance()
                .getUserId(mContext), false);
    }

    /**
     * 设置是否是夜间模式
     *
     * @param isNightMode
     */
    public void setIsNightMode(boolean isNightMode) {
        getEditor().putBoolean("is_night_mode" + UserController.getInstance()
                .getUserId(mContext), isNightMode)
                .commit();
       // BeanManager.getUtilSaver().setIsNightMode(mContext, isNightMode);
    }

    private SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

    /**
     * 保存夜间模式皮肤 apk文件名
     *
     * @param apk
     */
    public void saveNightSkinApkName(String apk) {

       // BeanManager.getUtilSaver().saveNightSkinApkName(mContext, apk);
        getEditor().putString("night_skin_apk_name" + UserController.getInstance()
                .getUserId(mContext), apk)
                .commit();
    }

    /**
     * 获取当前皮肤名
     *
     * @return
     */
    public String getSkinNightName() {
        return preferences.getString("skin_night_name" + UserController.getInstance()
                .getUserId(mContext), "默认");
    }

    public void saveSkinNightId(int skinId) {
        this.getEditor().putInt("skin_Night_id" + UserController.getInstance().getUserId(this.mContext), skinId).commit();
    }

    public boolean getNightSkinNew() {
        return this.preferences.getBoolean("night_skin_new" + UserController.getInstance().getUserId(this.mContext), false);
    }
    /**
     * 获取当前皮肤名
     *
     * @return
     */
    public String getSkinName() {
        return preferences.getString("skin_name" + UserController.getInstance()
                .getUserId(mContext), "默认");
    }

}
