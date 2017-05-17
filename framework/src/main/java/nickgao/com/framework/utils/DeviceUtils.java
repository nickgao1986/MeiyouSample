package nickgao.com.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

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

    public static int getStatusBarHeight(Activity activity) {
        int statusBarHeight = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
            if(statusBarHeight > 0) {
                return statusBarHeight;
            }
        }

        if(statusBarHeight <= 0) {
            Rect rectangle = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            statusBarHeight = rectangle.top;
            if(statusBarHeight > 0) {
                return statusBarHeight;
            }
        }

        return dip2px(activity, 20.0F);
    }

    /**
     * 显示键盘
     *
     * @param activity
     * @param view
     */
    public static void showkeyboard(Activity activity, View view) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
        } catch (Exception e) {
            e.printStackTrace();//
        }
    }


    //是否显示了键盘
    public static boolean isShowkeyboard(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.isActive();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

    }

    public static void hideKeyboard(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        try {
            if (view == null){
                view = activity.getCurrentFocus();
            }
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
