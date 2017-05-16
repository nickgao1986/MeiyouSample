package nickgao.com.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 应用内默认临时存储,APP_COMMON 以下的层临时保存数据,使用这个类
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/11/15 下午5:03
 */
public class Pref {

    /**
     * Preferences的存储文件名称
     */

    final static String SHARED_PREFERENCE = "seeyou_pref";

    /**
     * 存储字符串到Preference
     */

    public static void saveString(String key, String content, Context context) {

        SharedPreferences.Editor editor = context.getSharedPreferences(

                SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();

        editor.putString(key, content);

        editor.commit();

    }

    /**
     * 存储字符串到Preference
     */

    public static void saveString(int key_resID, String content, Context context) {

        String name = context.getResources().getString(key_resID);

        saveString(name, content, context);

    }

    /**
     * 获取存储在Preference中的字符串
     */

    public static String getString(int resId, Context context) {

        SharedPreferences shared = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return shared.getString(context.getString(resId), null);
    }

    public static String getString(String key, Context context) {
        if (context == null)
            return "";
        SharedPreferences shared = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return shared.getString(key, "");
    }

    /**
     * 存储整型数据到Preference
     */

    public static void saveInt(int key_resID, int content, Context context) {

        String name = context.getResources().getString(key_resID);

        saveInt(name, content, context);

    }

    /**
     * 存储整型数据到Preference
     */

    public static void saveInt(String name, int content, Context context) {

        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
                                                 .edit();

        editor.putInt(name, content);

        editor.commit();

    }

    public static int getInt(String name, Context context, int def) {

        SharedPreferences shared = context.getSharedPreferences(

                SHARED_PREFERENCE, Context.MODE_PRIVATE);

        return shared.getInt(name, def);

    }

    public static long getLong(String name, Context context, long def) {
        SharedPreferences shared = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return shared.getLong(name, def);
    }

    public static void saveLong(String name, Context context, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
                                                 .edit();
        editor.putLong(name, value);
        editor.commit();
    }

    public static void saveBoolean(Context context, String name, boolean vaule) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
                                                 .edit();
        editor.putBoolean(name, vaule);
        editor.commit();
    }

    public static void saveBoolean(Context context, int resId, boolean vaule) {
        saveBoolean(context, context.getString(resId), vaule);
    }

    public static boolean getBoolean(Context context, String key, boolean def) {
        SharedPreferences shared = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return shared.getBoolean(key, def);
    }

    public static boolean getBoolean(Context context, int resId, boolean def) {
        return getBoolean(context, context.getString(resId), def);
    }

    public static boolean containKey(Context context, String key) {
        SharedPreferences shared = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return shared.contains(key);
    }
}
