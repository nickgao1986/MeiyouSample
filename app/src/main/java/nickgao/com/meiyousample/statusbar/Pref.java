package nickgao.com.meiyousample.statusbar;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gaoyoujian on 2017/5/2.
 */

public class Pref {

    static final String SHARED_PREFERENCE = "seeyou_pref";

    public Pref() {
    }

    public static void saveString(String key, String content, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("seeyou_pref", 0).edit();
        editor.putString(key, content);
        editor.commit();
    }

    public static void saveString(int key_resID, String content, Context context) {
        String name = context.getResources().getString(key_resID);
        saveString(name, content, context);
    }

    public static String getString(int resId, Context context) {
        SharedPreferences shared = context.getSharedPreferences("seeyou_pref", 0);
        return shared.getString(context.getString(resId), (String)null);
    }

    public static String getString(String key, Context context) {
        if(context == null) {
            return "";
        } else {
            SharedPreferences shared = context.getSharedPreferences("seeyou_pref", 0);
            return shared.getString(key, "");
        }
    }

    public static void saveInt(int key_resID, int content, Context context) {
        String name = context.getResources().getString(key_resID);
        saveInt(name, content, context);
    }

    public static void saveInt(String name, int content, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("seeyou_pref", 0).edit();
        editor.putInt(name, content);
        editor.commit();
    }

    public static int getInt(String name, Context context, int def) {
        SharedPreferences shared = context.getSharedPreferences("seeyou_pref", 0);
        return shared.getInt(name, def);
    }

    public static long getLong(String name, Context context, long def) {
        SharedPreferences shared = context.getSharedPreferences("seeyou_pref", 0);
        return shared.getLong(name, def);
    }

    public static void saveLong(String name, Context context, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("seeyou_pref", 0).edit();
        editor.putLong(name, value);
        editor.commit();
    }

    public static void saveBoolean(Context context, String name, boolean vaule) {
        SharedPreferences.Editor editor = context.getSharedPreferences("seeyou_pref", 0).edit();
        editor.putBoolean(name, vaule);
        editor.commit();
    }

    public static void saveBoolean(Context context, int resId, boolean vaule) {
        saveBoolean(context, context.getString(resId), vaule);
    }

    public static boolean getBoolean(Context context, String key, boolean def) {
        SharedPreferences shared = context.getSharedPreferences("seeyou_pref", 0);
        return shared.getBoolean(key, def);
    }

    public static boolean getBoolean(Context context, int resId, boolean def) {
        return getBoolean(context, context.getString(resId), def);
    }

    public static boolean containKey(Context context, String key) {
        SharedPreferences shared = context.getSharedPreferences("seeyou_pref", 0);
        return shared.contains(key);
    }

}
