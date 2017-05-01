package nickgao.com.meiyousample.skin;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Administrator on 2014/8/29.
 * 皮肤管理  com.meetyou.skin.dark
 */

public class SkinEngine extends ResourceEngine {
    public static final String TAG = "SkinEngine";
    public static final String SKIN_NIGHT_PACKAGE_NAME = "com.meetyou.skin.night";
    public static final String SKIN_NIGHT_FILE_NAME = "NightSkin.apk";

    private boolean apply = false; //不应用老的换肤

    private SkinEngine() {
    }

    private static class SkinEnginHolder {
        public static final SkinEngine INSTANCE = new SkinEngine();
    }

    public static SkinEngine getInstance() {
        return SkinEnginHolder.INSTANCE;
    }

    /**
     * 重新初始化资源
     */
    public void initResources() {
        resourcesModel = null;
        resources = null;
    }

    /**
     * 设置背景
     *
     * @param context
     * @param view
     * @param resId
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public void setViewBackground(Context context, View view, int resId) {
        if (!apply) {
            return;
        }
        if (null == view)
            return;
        context = context.getApplicationContext();
        int bottom = view.getPaddingBottom();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int left = view.getPaddingLeft();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(getResouceDrawable(context, resId));
        } else {
            view.setBackground(getResouceDrawable(context, resId));
        }
        view.setPadding(left, top, right, bottom); //重新设置padding，处理padding失效的问题
    }

    /**
     * 设置背景颜色
     *
     * @param context
     * @param view
     * @param resId
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public void setViewBackgroundColor(Context context, View view, int resId) {
        if (!apply) {
            return;
        }
        if (null == view)
            return;
        context = context.getApplicationContext();
        int bottom = view.getPaddingBottom();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int left = view.getPaddingLeft();
        view.setBackgroundColor(getResouceColor(context, resId));
        view.setPadding(left, top, right, bottom); //重新设置padding，处理padding失效的问题
    }

    public void setViewBackgroundNoSkin(Context context, View view, int resId) {
        if (!apply) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(context.getResources().getDrawable(resId));
        } else {
            view.setBackground(getResouceDrawable(context, resId));
        }
    }

    /**
     * 设置图片
     *
     * @param context
     * @param view
     * @param resId
     */
    public void setViewImageDrawable(Context context, ImageView view, int resId) {
        if (!apply) {
            return;
        }
        if (null == view)
            return;
        context = context.getApplicationContext();
        view.setImageDrawable(getResouceDrawable(context, resId));
    }


    /**
     * 设置控件文字颜色
     *
     * @param context
     * @param view
     * @param resId
     */
    @SuppressLint("ResourceAsColor")
    public void setViewTextColor(Context context, TextView view, int resId) {
        if (!apply) {
            return;
        }
        try {
            if (null == view)
                return;
            context = context.getApplicationContext();
            view.setTextColor(getResouceColor(context, resId));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    /**
     * 设置控件文字颜色
     *
     * @param context
     * @param view
     * @param resId
     */
    @SuppressLint("ResourceAsColor")
    public void setViewTextColor(Context context, CheckBox view, int resId) {
        try {
            if (null == view)
                return;
            context = context.getApplicationContext();
            view.setTextColor(getResouceColor(context, resId));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @SuppressLint("ResourceAsColor")
    /**
     * 批量设置控件文字颜色
     * resids 资源ID数组
     * ids    视图数组
     *
     * PS:若使用同一颜色则传单个数量的数组（resids），若为每个元素分配不同的资源请确保resids及ids长度相同
     */
    public void setViewTextColorByIds(Activity activity, int[] resids, Object... ids) {
        if (!apply) {
            return;
        }
        if (null == resids || null == ids) {
            return;
        } else if (resids.length != 1 && resids.length != ids.length) {
            return;
        }

        for (int i = 0; i < ids.length; i++) {
            TextView textView;
            if (ids[i] instanceof View) {
                textView = (TextView) ids[i];
            } else {
                textView = (TextView) activity.findViewById(Integer.valueOf(ids[i].toString()));
            }

            if (null == textView) {
                continue;
            } else {
                if (resids.length == 1) {
                    textView.setTextColor(getResouceColor(activity.getApplicationContext(), resids[0]));
                } else {
                    textView.setTextColor(getResouceColor(activity.getApplicationContext(), resids[i]));
                }
            }
        }
    }


    public void setViewTextColorHint(Context context, TextView view, int resId) {
        if (!apply) {
            return;
        }
        if (null == view)
            return;
        context = context.getApplicationContext();
        view.setHintTextColor(getResouceColor(context, resId));
    }


    /**
     * 设置控件文字不同状态颜色
     *
     * @param context
     * @param view
     * @param resId
     */
    public void setViewTextColorStateList(Context context, TextView view, int resId) {
        if (!apply) {
            return;
        }
        if (null == view)
            return;
        context = context.getApplicationContext();
        view.setTextColor(getColorStateList(context, resId));
    }


    /**
     * 获取皮肤资源drawable
     *
     * @param context
     * @param id
     * @return
     */
    public Drawable getResouceDrawable(Context context, int id) {
        if (!apply) {
            return null;
        }
//        try {
//            context = context.getApplicationContext();
//            if (BeanManager.getUtilSaver().getIsNightMode(context)) {
//                return getResouceDrawable(context, id, SKIN_NIGHT_PACKAGE_NAME, BeanManager.getUtilSaver().getNightSkinApkName(context));
//            } else {
//                return getResouceDrawable(context, id, BeanManager.getUtilSaver().getSkinPackageName(context), BeanManager.getUtilSaver().getSkinApkName(context));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return context.getResources().getDrawable(id);
        return null;
    }

    /**
     * 获取皮肤资源color
     *
     * @param context
     * @param id
     * @return
     */
    public int getResouceColor(Context context, int id) {
        if (!apply) {
            return 0;
        }
//        try {
//            context = context.getApplicationContext();
//            if (BeanManager.getUtilSaver().getIsNightMode(context)) {
//                return getResouceColor(context, id, SKIN_NIGHT_PACKAGE_NAME, BeanManager.getUtilSaver().getNightSkinApkName(context));
//            } else {
//                return getResouceColor(context, id, BeanManager.getUtilSaver().getSkinPackageName(context), BeanManager.getUtilSaver().getSkinApkName(context));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return context.getResources().getColor(id);
    }

    /**
     * 获取ColorStateList资源
     *
     * @param context
     * @param id
     * @return
     */
    public ColorStateList getColorStateList(Context context, int id) {
        if (!apply) {
            return null;
        }
//        try {
//            context = context.getApplicationContext();
//            if (BeanManager.getUtilSaver().getIsNightMode(context)) {
//                return getResouceColorStateList(context, id, SKIN_NIGHT_PACKAGE_NAME, BeanManager.getUtilSaver().getNightSkinApkName(context));
//            } else {
//                return getResouceColorStateList(context, id, BeanManager.getUtilSaver().getSkinPackageName(context), BeanManager.getUtilSaver().getSkinApkName(context));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return context.getResources().getColorStateList(id);
    }


    /**
     * 获取皮肤包的文字
     *
     * @param context
     * @param id
     * @return
     */
    public String getResouceText(Context context, int id) {
        if (!apply) {
            return null;
        }
//        try {
//            context = context.getApplicationContext();
//            if (BeanManager.getUtilSaver().getIsNightMode(context)) {
//                return getResouceText(context, id, SKIN_NIGHT_PACKAGE_NAME, BeanManager.getUtilSaver().getNightSkinApkName(context));
//            } else {
//                return getResouceText(context, id, BeanManager.getUtilSaver().getSkinPackageName(context), BeanManager.getUtilSaver().getSkinApkName(context));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return context.getResources().getString(id);
    }

}
