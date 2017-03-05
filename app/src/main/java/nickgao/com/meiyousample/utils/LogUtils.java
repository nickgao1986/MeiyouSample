package nickgao.com.meiyousample.utils;

import android.util.Log;

/**
 * Created by gaoyoujian on 2017/3/5.
 */

public class LogUtils {

    private static final String TAG = "TAG";
    private static String checkEmpty(String content) {
        return StringUtils.isEmpty(content)?"null!!":content;
    }


    public static void e(String content) {
        Log.e(TAG, checkEmpty(content));
    }


    @Deprecated
    public static void d(String content) {
        Log.d(TAG, checkEmpty(content));
    }

    public static void w(String content) {
        Log.w(TAG, checkEmpty(content));
    }

    public static void w(String tag,String content, Throwable tr) {
        Log.w(tag, checkEmpty(content),tr);
    }

    public static void d(String tag,String content, Throwable tr) {
        Log.d(tag, checkEmpty(content),tr);
    }

    public static void e(String tag,String content, Throwable tr) {
        Log.e(tag, checkEmpty(content),tr);
    }

    public static void d(String content, Throwable tr) {
        Log.d(TAG, content, tr);
    }


    public static void w(String content, Throwable tr) {
        Log.e(TAG, content, tr);
    }

    public static void e(String content, Throwable tr) {
        Log.e(TAG, content, tr);
    }

    public static void e(String tag, String content) {
        Log.e(tag, content);
    }

    public static void w(String tag, String content) {
        Log.e(tag, content);
    }

    public static void d(String tag, String content) {
        Log.e(tag, content);
    }
}
