package com.lingan.seeyou.ui.view.skin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.attr.MutableAttr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.utils.FileUtils;
import nickgao.com.meiyousample.utils.PackageUtil;

/**
 * 皮肤管理类
 * 支持换肤
 * <p/>
 * Created by hxd on 15/6/30.
 */
public class SkinManager {

    private static final String SP_NAME_FILE = "skin-info";
    private static final String SP_NAME_PATH = "skin-info-path";
    private static final String SP_NAME_PACKAGE = "skin-info-package-name";
    private static final String SP_NAME_APPLY_FLAG = "is-apply";
    private AssetManager assetManager;
    private SkinResources skinResources;
    private Resources originResources;
    private Context context;
    private AssetManager origAsset;
    private String skinPackageName;
    private String skinApkPath;
    private String origPackageName;
    private boolean apply = false;//应用换肤的开关
    private boolean ready = false; // is init ?

    //private Set<SkinViewChanger> skinChangeCallbackList = new HashSet<>();

    public static SkinManager getInstance() {
        return Holder.instance;
    }

    static class Holder {
        static SkinManager instance = new SkinManager();
    }

    private SkinManager() {
    }
    public void init(Context context, Resources origRes) {
        this.originResources = origRes;
        this.context = context;
    }
    public void init(Context context, Resources origRes, AssetManager origAsset) {
        if (ready) {
            return;
        }
        this.originResources = origRes;
        this.origAsset = origAsset;
        this.context = context;
        this.origPackageName = PackageUtil.getPackageInfo(context).packageName;
        this.apply = fetchApplyFlag();
        if (apply) {
            //TODO need sync the load  process
            loadResources(fetchSkinPath(), fetchSkinPackageName());

            //this need exec in  ui thread
            this.ready = true;
        } else {
            this.ready = true;
        }
    }

    private String getMinePath() {
        PackageManager m = context.getPackageManager();
        String s = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.w("yourtag", "Error Package name not found ", e);
        }
        return s;
    }

    /**
     * 加载换肤资源
     * 可多次调用，保存最后一次的调用结果
     *
     * @param apkPath
     * @param skinPackageName
     */
    public void loadResources(String apkPath, String skinPackageName) {
        try {
            closeLast();
            if (StringUtils.isBlank(apkPath) || StringUtils.isBlank(skinPackageName)) {
                clearSkinInfo(context);
                this.apply = false;
                saveApplyFlag();
                return;
            }
            LogUtils.d("load resources apk " + apkPath);
            if (originResources == null) {
                throw new RuntimeException("need call init first");
            }
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);

            int add = (Integer) addAssetPath.invoke(assetManager, apkPath);
            LogUtils.d("add resources apk result " + add);
            if (add <= 0) {
                throw new RuntimeException("load res apk failed !!----" + apkPath);
            }

            this.skinApkPath = apkPath;
            this.skinPackageName = skinPackageName;
            skinResources = new SkinResources(this, assetManager, originResources.getDisplayMetrics(),
                    originResources.getConfiguration(), skinPackageName);
            skinResources.setClassLoader(getResourcesClassLoader());
            saveSkinInfo();
            saveApplyFlag();
        } catch (InvocationTargetException e) {
            LogUtils.e(e.getLocalizedMessage());
        } catch (NoSuchMethodException e1) {
            LogUtils.e(e1.getLocalizedMessage());
        } catch (InstantiationException e2) {
            LogUtils.e(e2.getLocalizedMessage());
        } catch (IllegalAccessException e3) {
            LogUtils.e(e3.getLocalizedMessage());
        }
    }


    public Resources getOriginResources() {
        return originResources;
    }


    private void closeLast() {
        if (assetManager != null) {
            assetManager.close();
        }
        if (skinResources != null) {
            skinResources = null;
        }
    }

    /**
     * @param apply
     */
    public void setApply(boolean apply) {
        this.apply = apply;
        saveApplyFlag();
    }


    public boolean isApply() {
        return ready && apply && skinResources != null;
    }


    public boolean applyView(Context context) {
        if (this.apply) {
            return ViewFactory.from(context).apply();
        }
        return false;
    }

    public boolean applyView(ViewGroup viewGroup) {
        if (this.apply) {
            return ViewFactory.from(context).apply(viewGroup);
        }
        return false;
    }

    public boolean applyView(View view) {
        if (this.apply) {
            return ViewFactory.from(context).apply(view);
        }
        return false;
    }

    public String getOrigPackageName() {
        return origPackageName;
    }

    public AssetManager getAssets() {
        return assetManager == null ? origAsset : assetManager;
    }

    public boolean ready() {
        return ready;
    }


    private void saveSkinInfo() {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_FILE, Context.MODE_PRIVATE);
        sp.edit().putString(SP_NAME_PATH, this.skinApkPath)
                .putString(SP_NAME_PACKAGE, this.skinPackageName).commit();
    }

    public void clearSkinInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_FILE, Context.MODE_PRIVATE);
        sp.edit().putString(SP_NAME_PATH, "")
                .putString(SP_NAME_PACKAGE, "").commit();
    }

    private String fetchSkinPath() {
        return context.getSharedPreferences(SP_NAME_FILE, Context.MODE_PRIVATE).getString(SP_NAME_PATH, "");
    }

    private String fetchSkinPackageName() {
        return context.getSharedPreferences(SP_NAME_FILE, Context.MODE_PRIVATE).getString(SP_NAME_PACKAGE, "");
    }

    private void saveApplyFlag() {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_FILE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(SP_NAME_APPLY_FLAG, this.apply).commit();
    }

    private boolean fetchApplyFlag() {
        return context.getSharedPreferences(SP_NAME_FILE, Context.MODE_PRIVATE).getBoolean(SP_NAME_APPLY_FLAG, false);
    }

    public DexClassLoader getResourcesClassLoader() {
        String newFile = skinApkPath + ".apk";
        File file = new File(newFile);
        try {
            if (!file.exists()) {
                file.createNewFile();
                FileUtils.copyFile(new File(skinApkPath), file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        DexClassLoader dexClassLoader = new DexClassLoader(newFile,
                context.getCacheDir().getAbsolutePath(), null, this.getClass().getClassLoader());
        try {
            Class skinR = dexClassLoader.loadClass(skinPackageName + ".R");
            return dexClassLoader;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Drawable getAdapterDrawable(int originId) {
        return getAdapterDrawable(null, null, originId);
    }

    public int getAdapterColor(int originId) {
        return getAdapterColor(null, null, originId);
    }

    public ColorStateList getAdapterColorStateList(int originId) {
        return getAdapterColorStateList(null, null, originId);
    }

    public Drawable getAdapterDrawable(String type, String entryName, int originId) {
        if (isApply()) {
            try {
                int newId = skinResources.obtainSkinId(type, entryName, originId);
                if (newId > 0) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        return skinResources.getDrawable(newId);
                    } else {
                        return skinResources.getDrawable(newId, null);
                    }
                }
            } catch (Resources.NotFoundException e) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    return originResources.getDrawable(originId);
                } else {
                    return originResources.getDrawable(originId, null);
                }
            }

        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return originResources.getDrawable(originId);
        } else {
            return originResources.getDrawable(originId, null);
        }
    }


    /**
     * @param type
     * @param entryName
     * @param originId  不是必选 ,如果不传,前两项必填
     * @return
     */
    public int getAdapterColor(String type, String entryName, int originId) {
        if (isApply()) {
            try {
                int newId = skinResources.obtainSkinId(type, entryName, originId);
                if (newId > 0) {
                    return skinResources.getColor(newId);
                }
            } catch (Resources.NotFoundException e) {
                return originResources.getColor(originId);
            }
        }
        return originResources.getColor(originId);
    }

    public ColorDrawable getAdapterColorDrawable(String type, String entryName, int originId) {
        if (isApply()) {
            try {
                int newId = skinResources.obtainSkinIdForColor(type, entryName, originId);
                if (newId > 0) {
                    return new ColorDrawable(newId);

                    //return skinResources.getColor(newId);
                }
            } catch (Resources.NotFoundException e) {
                return null;
            }
        }
        return null;
    }

    @Nullable
    public ColorStateList getAdapterColorStateList(String type, String entryName, int originId) {
        if (isApply()) {
            try {
                int skinId = skinResources.obtainSkinId(type, entryName, originId);
                if (skinId > 0) {
                    return skinResources.getColorStateList(skinId);
                }
            } catch (Resources.NotFoundException e) {
                return originResources.getColorStateList(originId);
            }

        }
        return originResources.getColorStateList(originId);
    }

    /**
     * runtime 修改资源
     *
     * @param view  target view
     * @param resId background res id
     */
    public SkinManager setDrawableBackground(View view, int resId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(SkinManager.getInstance().getAdapterDrawable(resId));
            } else {
                view.setBackgroundDrawable(SkinManager.getInstance().getAdapterDrawable(resId));
            }
            addMutableAttrs(view, MutableAttr.TYPE.BACKGROUND.getRealName(), resId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public SkinManager setDrawable(ImageView view, int resId) {
        try {
            view.setImageDrawable(SkinManager.getInstance().getAdapterDrawable(resId));
            addMutableAttrs(view, MutableAttr.TYPE.SRC.getRealName(), resId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public SkinManager setTextColor(TextView view, int colorId) {
        try {
            view.setTextColor(SkinManager.getInstance().getAdapterColor(colorId));
            addMutableAttrs(view, MutableAttr.TYPE.TEXT_COLOR.getRealName(), colorId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    private SkinManager addMutableAttrs(View view, String attrName, int resId) {
        List<MutableAttr> mutableAttrList = new ArrayList<>();
        ViewFactory factory = ViewFactory.from(context);
        mutableAttrList.add(factory.createMutableAttr(attrName, resId));
        factory.addRuntimeView(view, mutableAttrList);
        return this;
    }

    public SkinManager excludeView(View view) {
        ViewFactory.from(view.getContext()).excludeView(view);
        return this;
    }
    public SkinManager excludeViewAttrs(View view,String...attrs) {
        ViewFactory.from(view.getContext()).excludeViewAttrs(view,attrs);
        return this;
    }
}


