package com.lingan.seeyou.ui.view.dialog.bottomdialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import java.util.HashMap;
import java.util.Map;


/**
 * 系统进度条对话框
 *
 * @author Administrator
 */
public class PhoneProgressDialog {


    private static boolean bProcess = false;

    private static HashMap<Integer, ProgressDialog> dialogWeakHashMap = new HashMap<>();

    public PhoneProgressDialog() {
        //super(context);
        // TODO Auto-generated constructor stub
    }

    /**
     * 显示圆形 进度条对话框
     *
     * @param activity
     * @param msg
     * @param listener
     */
    public static void showRoundDialog(Activity activity, String msg,
                                       DialogInterface.OnCancelListener listener) {

        try {

            ProgressDialog dialog = creatProgressDialog(activity);
            bProcess = false;
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCanceledOnTouchOutside(false);
            //dialog.setTitle("提示");
            dialog.setMessage(msg);
            if (listener == null) {
                dialog.setCancelable(false);
            } else {
                dialog.setOnCancelListener(listener);
            }
            dialog.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 设置进度信息
     *
     * @param strMsg
     */
    public static void setMsg(Activity activity, String strMsg) {
        try {
            ProgressDialog dialog = getProgressDialog(activity);
            if (dialog != null) {
                dialog.setMessage(strMsg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 取消
     */
    public static void dismissDialog(Activity activity) {
        try {
            ProgressDialog dialog = getProgressDialog(activity);
            if (dialog != null) {
                dialog.dismiss();
                dialogWeakHashMap.remove(getHashCode(activity));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 显示 长方形 进度条对话框
     *
     * @param activity
     * @param msg
     * @param listener
     */
    public static void showRectDialog(Activity activity, String msg, DialogInterface.OnCancelListener listener) {
        try {
            ProgressDialog dialog = creatProgressDialog(activity);
            bProcess = true;
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.setTitle("提示");
            dialog.setMessage(msg);
            dialog.setOnCancelListener(listener);
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 设置进度
     *
     * @param value
     */
    public static void setProcess(Activity activity, int value) {
        if (!bProcess)
            return;
        ProgressDialog dialog = getProgressDialog(activity);
        if (dialog != null) {
            dialog.setProgress(value);
        }
    }

    //创建dialog
    private static ProgressDialog creatProgressDialog(Activity activity) {
        removeAllProgressDialog();
        ProgressDialog dialog = new ProgressDialog(activity);
        dialogWeakHashMap.put(getHashCode(activity), dialog);
        return dialog;
    }

    //根据传入的activity获取对应的dialog
    private static ProgressDialog getProgressDialog(Activity activity) {
        if (activity != null) {
            int hashCode = getHashCode(activity);
            if (dialogWeakHashMap.containsKey(hashCode)) {//保证一个activity就只有一个dialog
                return dialogWeakHashMap.get(hashCode);
            }
        }
        return null;
    }

    //移除已经打开的dialog
    private static void removeAllProgressDialog() {
        for (Map.Entry<Integer, ProgressDialog> entry : dialogWeakHashMap.entrySet()) {
            entry.getValue().dismiss();
        }
        dialogWeakHashMap.clear();
    }

    private static int getHashCode(Activity activity) {
        return activity.hashCode();
    }


}
