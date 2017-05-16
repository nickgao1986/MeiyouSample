package biz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;

/**
 * 保存文件类
 * 底层还是用SharePreference 来存; 如果内容比较多推荐这种,如果内容少推荐Pref.save()-- by zxb
 */
public class FastPersistenceDAO {

    private static String PRE_FILE_NAME = "serializable-";

    /**
     * 会被JSON格式化一次，注意不要重复序列化，
     *
     * @param context    context
     * @param obj        序列号类
     * @param uniqueName 序列化后的名字  如果需要 请注意自己拼接userId
     */
    public static boolean saveObject(@NonNull Context context, @NonNull Serializable obj,
                                     @NonNull String uniqueName) {
        String objStr = JSON.toJSONString(obj);
        return saveString(context, objStr, uniqueName);
    }

    /**
     * 保存String 到文件
     *
     * @param context
     * @param String
     * @param uniqueName
     * @return
     */
    public static boolean saveString(@NonNull Context context, @NonNull String String,
                                     @NonNull String uniqueName) {
        try {
            if (StringUtils.isEmpty(uniqueName)) {
                return false;
            }
            SharedPreferences prefs = context.getSharedPreferences(PRE_FILE_NAME + uniqueName, 0);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(uniqueName, String);
            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 直接获取String,-by zxb
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static String getString(@NonNull Context context, @NonNull String uniqueName) {
        if (StringUtils.isEmpty(uniqueName)) {
            return null;
        }
        SharedPreferences prefs = context.getSharedPreferences(
                PRE_FILE_NAME + uniqueName, 0);
        String objStr = prefs.getString(uniqueName, "");
        return objStr;
    }

    @Nullable
    public static <T> T getObject(@NonNull Context context,
                                  @NonNull String uniqueName, @NonNull Class<T> clazz) {
        String objStr = getString(context, uniqueName);
        if (!StringUtils.isEmpty(objStr)) {
            return JSON.parseObject(objStr, clazz);
        }
        return null;
    }

    @Nullable
    public static <T> List<T> getObjectList(@NonNull Context context,
                                            @NonNull String uniqueName, @NonNull Class<T> clazz) {
        if (StringUtils.isEmpty(uniqueName)) {
            return null;
        }
        SharedPreferences prefs = context.getSharedPreferences(
                PRE_FILE_NAME + uniqueName, 0);
        String objStr = prefs.getString(uniqueName, "");
        if (!StringUtils.isEmpty(objStr)) {
            return JSON.parseArray(objStr, clazz);
        }
        return null;
    }

}
