package nickgao.com.meiyousample.statusbar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lingan.seeyou.ui.view.skin.SkinUpdateEvent;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import de.greenrobot.event.EventBus;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/5/2.
 */

public class StatusBarController {

    private static final java.lang.String TAG = "StatusBarController";
    private static StatusBarController instance;
    public static synchronized  StatusBarController getInstance(){
        if(instance==null){
            synchronized (StatusBarController.class){
                if (instance==null){
                    instance = new StatusBarController();
                }
            }
        }
        return instance;
    }

    private StatusbarConfig mStatusbarConfig;

    /**
     * 初始化
     * @param statusbarConfig
     */
    public void init(StatusbarConfig statusbarConfig){
        try {
            mStatusbarConfig = statusbarConfig;
            EventBus.getDefault().post(new SkinUpdateEvent(null));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 是否初始化
     * @return
     */
    public boolean isInited(){
        return mStatusbarConfig==null?false:true;
    }

    public int getDefaultColor(){
        try {
            if(!isInited()){
                return -1;
            }
            return mStatusbarConfig.getDefaultColor();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return -1;
    }
    /**
     * 是否enable了
     * @return
     */
    public boolean isEnableStatusBar(){
        try {
            if(!isInited()){
                return false;
            }
            return mStatusbarConfig.isEnableStatusBar();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    /**
     * 是否在忽略列表里
     * @param classname ,全路径名称
     * @return
     */
    public boolean isInIgnorePageList(String classname){
        try {
            if(StringUtils.isNull(classname))
                return false;
            if(!isInited()){
                return false;
            }
            if(mStatusbarConfig.getIgnorePageList()==null)
                return false;
            if(mStatusbarConfig.getIgnorePageList().contains(classname)){
                return true;
            }
            return false;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 是否在特殊配置列表里,如果有,返回颜色值,如果没有,返回<=0
     * @param classname 全路径名称
     * @return
     */
    public int isInSpecialPageList(String classname){
        try {
            if(StringUtils.isNull(classname))
                return 0;
            if(!isInited()){
                return 0;
            }
            if(mStatusbarConfig.getSpecailPageMap()==null)
                return 0;

            Iterator iter = mStatusbarConfig.getSpecailPageMap().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String keyClassName = (String)entry.getKey();
                int valueColo = (Integer)entry.getValue();
                if(keyClassName.equals(classname)){
                    return valueColo;
                }
            }
            return 0;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return -1;
    }

    public boolean isInSpecialStatusList(String classname){
        try {
            if(StringUtils.isNull(classname))
                return false;
            if(!isInited()){
                return false;
            }
            if(mStatusbarConfig.getSpecailStatusMap()==null)
                return false;

            Iterator iter = mStatusbarConfig.getSpecailStatusMap().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String keyClassName = (String)entry.getKey();
                boolean isDark = (boolean)entry.getValue();
                if(keyClassName.equals(classname)){
                    return isDark;
                }
            }
            return false;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 设置color
     * @param activity
     */
    public void handleSetStatusColor(Activity activity){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if(!StatusBarController.getInstance().isInited()){
                    return;
                }
                if(!StatusBarController.getInstance().isEnableStatusBar()){
                    return;
                }
                String nowClassName = activity.getClass().getName();
                if(StatusBarController.getInstance().isInIgnorePageList(nowClassName)){
                    return;
                }
                //设置状态栏颜色
                int showColor=0;
                int spcialColor  = StatusBarController.getInstance().isInSpecialPageList(nowClassName);
                if(spcialColor!=0){
                    showColor = spcialColor;
                }else{
                    int defaultColor =  StatusBarController.getInstance().getDefaultColor();
                    if(defaultColor!=0){
                        showColor = defaultColor;
                    }else{
                        showColor = activity.getResources().getColor(R.color.trans_color);
                    }
                }
                if(showColor!=0){
                    StatusBarUtil.setColor(activity,showColor,0);
                }
                //设置状态栏暗色
                if(mStatusbarConfig!=null && mStatusbarConfig.getSpecailStatusMap()!=null &&mStatusbarConfig.getSpecailStatusMap().size()>0){
                    if(isInSpecialStatusList(nowClassName)){
                        if(isMiui(activity.getApplicationContext())){
                            setMiuiStatusBarDarkMode(activity,true);
                        }else  if(isFlyme(activity.getApplicationContext())){
                            setMeizuStatusBarDarkIcon(activity,true);
                        }else{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            }
                        }
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }




    public boolean setStatusTextColor(Activity activity,boolean flag){
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return false;
            }
            if(isMiui(activity.getApplicationContext())){
                return setMiuiStatusBarDarkMode(activity,flag);
            }else  if(isFlyme(activity.getApplicationContext())){
                return setMeizuStatusBarDarkIcon(activity,flag);
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(flag)
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    else
                        activity.getWindow().getDecorView().setSystemUiVisibility(0);
                    return true;
                }else{
                    return false;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 手动设置状态栏颜色
     */
    public boolean setStatusBarColor(Activity activity, int color){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if(color != 0){
                    return StatusBarUtil.setColor(activity, color, 0);
                }
            }
            return false;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 重新设置状态栏回默认颜色
     */
    public void setStatusBarDefaultColor(Activity activity){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int defaultColor =  StatusBarController.getInstance().getDefaultColor();
                setStatusBarColor(activity, defaultColor);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * 是否是miui
     * @return
     */
    private  boolean isMiui(Context context){
        try{
            if(!Pref.containKey(context,"is_miui")){
                Properties prop= new Properties();
                prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
                boolean isMIUI= prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                        || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                        || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
                Pref.saveBoolean(context,"is_miui",isMIUI);
                return isMIUI;
            }
            return Pref.getBoolean(context,"is_miui",false);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 是否是魅族
     * @return
     */
    private boolean isFlyme(Context context) {
        try {
            // Invoke Build.hasSmartBar()
            if(!Pref.containKey(context,"is_meizu")){
                final Method method = Build.class.getMethod("hasSmartBar");
                Pref.saveBoolean(context,"is_meizu",method != null);
                return method != null;
            }
            return Pref.getBoolean(context,"is_meizu",false);
        } catch (final Exception e) {
            return false;
        }
    }

    //miui
    private  boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private  boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }


}
