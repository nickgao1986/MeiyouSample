package nickgao.com.meiyousample.skin;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;

/**
 * Created by Administrator on 2014/9/23.
 */
public class ResourceEngine {
    public static final String TAG = "ResourceEngin";
    public static final String PACKAGE_NAME = "com.lingan.seeyou";
    protected HashMap<Object, Object> resouceMap = new HashMap<Object, Object>();
    protected ResourcesModel resourcesModel;
    protected Resources resources;


    protected Drawable getResouceDrawable(Context context, int id, String skinPackageName, String apkName) {
        try {
            Drawable drawable = null;
            LogUtils.d(TAG, "skinPackageName:" + skinPackageName + "resources is" + (resourcesModel == null));
            if (!StringUtils.isNull(skinPackageName)) {
                Resources resources;
                if (null == resourcesModel || !resourcesModel.packageName.equals(skinPackageName) || !resourcesModel.apkName.equals(apkName)) {
                    resources = getApkFileResources(context, apkName);
                    resourcesModel = new ResourcesModel(resources, skinPackageName,apkName);
                } else {
                    resources = resourcesModel.resources;
                }
                if (null != resources) {
                    int resId = 0;
                    resId = getPakResId(context, resources, id, skinPackageName);
                    if (resId == 0) {
                        return context.getResources().getDrawable(id);
                    }
                    drawable = resources.getDrawable(resId);
                }
            }
            if (null == drawable) {
                return context.getResources().getDrawable(id);
            } else {
                return drawable;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getResources().getDrawable(id);
    }

    protected int getResouceColor(Context context, int id, String skinPackageName, String apkName) {
        try {
            context = context.getApplicationContext();
            int color = context.getResources().getColor(id);
            LogUtils.d(TAG, "skinPackageName:" + skinPackageName);
            if (!StringUtils.isNull(skinPackageName)) {
                Resources resources;
                if (null == resourcesModel || !resourcesModel.packageName.equals(skinPackageName) || !resourcesModel.apkName.equals(apkName)) {
                    resources = getApkFileResources(context, apkName);
                    resourcesModel = new ResourcesModel(resources, skinPackageName,apkName);
                } else {
                    resources = resourcesModel.resources;
                }
                if (null != resources) {
                    int resId = 0;
                    resId = getPakResId(context, resources, id, skinPackageName);
                    if (resId == 0) {
                        return context.getResources().getColor(id);
                    }
                    color = resources.getColor(resId);
                }
            }
            return color;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getResources().getColor(id);
    }

    protected ColorStateList getResouceColorStateList(Context context, int id, String skinPackageName, String apkName) {
        try {
            context = context.getApplicationContext();
            LogUtils.d(TAG, "skinPackageName:" + skinPackageName);
            if (!StringUtils.isNull(skinPackageName)) {
                Resources resources;
                if (null == resourcesModel || !resourcesModel.packageName.equals(skinPackageName) || !resourcesModel.apkName.equals(apkName)) {
                    resources = getApkFileResources(context, apkName);
                    resourcesModel = new ResourcesModel(resources, skinPackageName,apkName);
                } else {
                    resources = resourcesModel.resources;
                }
                if (null != resources) {
                    int resId = 0;
                    resId = getPakResId(context, resources, id, skinPackageName);
                    if (resId == 0) {
                        return context.getResources().getColorStateList(id);
                    }
                    return resources.getColorStateList(resId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getResources().getColorStateList(id);
    }

    protected String getResouceText(Context context, int id, String skinPackageName, String apkName) {
        try {
            context = context.getApplicationContext();
            String text = context.getResources().getString(id);
            LogUtils.d(TAG, "skinPackageName:" + skinPackageName);
            if (!StringUtils.isNull(skinPackageName)) {
                Resources resources;
                if (null == resourcesModel || !resourcesModel.packageName.equals(skinPackageName) || !resourcesModel.apkName.equals(apkName)) {
                    resources = getApkFileResources(context, apkName);
                    resourcesModel = new ResourcesModel(resources, skinPackageName,apkName);
                } else {
                    resources = resourcesModel.resources;
                }
                if (null != resources) {
                    int resId = 0;
                    resId = getPakResId(context, resources, id, skinPackageName);
                    if (resId == 0) {
                        return context.getResources().getString(id);
                    }
                    text = resources.getString(resId);
                }
            }
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getResources().getString(id);
    }

    protected String[] getResouceStringArray(Context context, int id, String skinPackageName, String apkName) {
        try {
            context = context.getApplicationContext();
            String[] array = context.getResources().getStringArray(id);
            LogUtils.d(TAG, "skinPackageName:" + skinPackageName);
            if (!StringUtils.isNull(skinPackageName)) {
                Resources resources;
                if (null == resourcesModel || !resourcesModel.packageName.equals(skinPackageName) || !resourcesModel.apkName.equals(apkName)) {
                    resources = getApkFileResources(context, apkName);
                    resourcesModel = new ResourcesModel(resources, skinPackageName,apkName);
                } else {
                    resources = resourcesModel.resources;
                }
                if (null != resources) {
                    int resId = 0;
                    resId = getPakResId(context, resources, id, skinPackageName);
                    if (resId == 0) {
                        return context.getResources().getStringArray(id);
                    }
                    array = resources.getStringArray(resId);
                }
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getResources().getStringArray(id);
    }

    protected int getPakResId(Context context, Resources resources, int resId, String packageName) {
        int id = 0;
        try {
            context = context.getApplicationContext();
            String resName = context.getResources().getResourceName(resId);
            resName = resName.replace(/*PACKAGE_NAME*/context.getPackageName(), packageName);
            id = resources.getIdentifier(resName, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    /**
     * 获取apk中的resoucres
     *
     * @param context
     * @param apkFileName
     * @return
     */
    public Resources getApkFileResources(Context context, String apkFileName) {
        context = context.getApplicationContext();
        File dir = context.getDir(SkinUtil.SKIN_FOLDER, Activity.MODE_PRIVATE);
        String apkPath = dir.getAbsolutePath() + File.separator + apkFileName;
        LogUtils.d("getApkFileResources:" + apkPath);
        return getApkResources(context, apkPath);
    }

    private Resources getApkResources(Context context, String apkPath) {
        LogUtils.d(TAG, apkPath);
        context = context.getApplicationContext();
        File apkFile = new File(apkPath);
//        if (!apkFile.exists() || !apkPath.toLowerCase().endsWith(".apk")) {
        if (!apkFile.exists()) {
            LogUtils.d(TAG, "file path is not correct");
            return null;
        }
        String PATH_ASSETMANAGER = "android.content.res.AssetManager";
        try {

            Class<?>[] typeArgs = {String.class};
            Object[] valueArgs = {apkPath};

            Class<?> assetMagCls = Class.forName(PATH_ASSETMANAGER);
            Object assetMag = assetMagCls.newInstance();

            typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Method assetMagaddAssetPathMtd = assetMagCls.getDeclaredMethod(
                    "addAssetPath", typeArgs);
            valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            assetMagaddAssetPathMtd.invoke(assetMag, valueArgs);

            Resources res = context.getResources();
            typeArgs = new Class[3];
            typeArgs[0] = assetMag.getClass();
            typeArgs[1] = res.getDisplayMetrics().getClass();
            typeArgs[2] = res.getConfiguration().getClass();
            Constructor<Resources> resCt = Resources.class
                    .getConstructor(typeArgs);
            valueArgs = new Object[3];
            valueArgs[0] = assetMag;
            valueArgs[1] = res.getDisplayMetrics();
            valueArgs[2] = res.getConfiguration();
            res = (Resources) resCt.newInstance(valueArgs);

            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
