package nickgao.com.framework.utils;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public interface UtilSaver {
    int NORMAL = 0;
    int PREGNANCY_BABY = 1;
    int PREGNANCY_PREPARE = 2;
    int MOTHIER = 3;
    int WEIGHT_LOSS_MODE = 4;
    int IDETITY = -322;

    String getNativePosID();

    String getApp_id();

    void setXiaomiParams(String var1, String var2, String var3);

    String getXiaomiAppKey();

    String getXiaomi_app_id();

    String getXiaomi_app_secret();

    String getSINA_APPKEY();

    String getSINA_SECRET();

    String getSINA_SCOPE();

    String getSINA_REDIRECT_URI();

    void setSINA_PARAMS(String var1, String var2, String var3, String var4);

    String getQZONE_CLIENT_ID();

    String getQZONE_SECRET();

    String getQZONE_SCOPE();

    void setQZONE_PARAMS(String var1, String var2, String var3, String var4);

    String getWX_APP_ID();

    String getWX_APP_SECRET();

    String getWX_PUBLIC_APP_ID();

    void setWX_PARAMS(String var1, String var2, String var3);

    String getWX_PAY_APP_ID();

    String getWX_PAY_APP_SECRET();

    void setWX_PAY_PARAMS(String var1, String var2);

    void setContext(Context var1);

    Context getContext();

    void setTcpDeviceType(int var1);

    int getTcpDeviceType();

    boolean isAppUIVisible();

    void setAppUIVisible(boolean var1);

    void setEnteredMain(boolean var1);

    boolean isEnterMained();

    boolean isBackToMain();

    void setBackToMain(boolean var1);

    void setMeetyouNotifyOpen(Context var1, boolean var2);

    boolean isMeetyouNotifyOpen(Context var1);

    int getUnreadCount();

    void setUnreadCount(Context var1, int var2);

    int getPictureQuality(Context var1);

    void setPictureQuality(Context var1, int var2);

    boolean isThumbMode(Context var1);

    void setThumbMode(Context var1, boolean var2);

    String getPlatForm();

    void setPlatForm(String var1);

    String getPlatFormAppId();

    void setPlatFormAppId(String var1);

    boolean isForbidUrl(Context var1, String var2);

    int getUploadImageWay();

    void setUploadImageWay(int var1);

    void saveBabyoutDate(Context var1, long var2);

    String getBabyoutDateString(Context var1);

    String getPregnancyYuchanTimeString(Context var1);

    void setPregnancyYuchanTimeCalendar(Context var1, Calendar var2);

    Calendar getPregnancyYuchanTimeCalendar(Context var1);

    void setIsYuchanTimeChangeLastOpenSeachCircleActivity(Context var1, boolean var2);

    boolean getIsYuchanTimeChangeLastOpenSeachCircleActivity(Context var1);

    void setPregnancyStartTime(Context var1, Calendar var2);

    Calendar getPregnancyStartTime(Context var1);

    void saveUserCircleNickName(Context var1, String var2);

    String getUserCircleNickName(Context var1);

    void saveUserId(Context var1, int var2);

    int getUserId(Context var1);

    void saveTbUserId(Context var1, String var2);

    String getTbUserId(Context var1);

    void saveUserVirtualId(Context var1, int var2);

    int getUserVirtualId(Context var1);

    void saveUserVirtualToken(Context var1, String var2);

    String getUserVirtualToken(Context var1);

    void saveUserToken(Context var1, String var2);

    String getUserToken(Context var1);

    void saveUserIdentify(Context var1, int var2);

    int getUserIdentify(Context var1);

    void saveSkinPackageName(Context var1, String var2);

    String getSkinPackageName(Context var1);

    void saveSkinName(Context var1, String var2);

    String getSkinName(Context var1);

    void saveSkinNightName(Context var1, String var2);

    String getSkinNightName(Context var1);

    void saveSkinApkName(Context var1, String var2);

    String getSkinApkName(Context var1);

    void saveNightSkinApkName(Context var1, String var2);

    String getNightSkinApkName(Context var1);

    void setIsNightMode(Context var1, boolean var2);

    boolean getIsNightMode(Context var1);

    String getPasswords(Context var1);

    void setPasswords(Context var1, String var2);

    void setAppInBackgroud(boolean var1);

    boolean isIsAppInBackgroud();

    boolean isPasswordEmpty(Context var1);

    boolean isShowPasswordPage(Context var1);

    void setShowPswdPage(Context var1, boolean var2);

    String getMyClient();

    void setMyClient(String var1);

    String getClient();

    void setClient(String var1);

    String getTaobaoId();

    void setTaobaoId(String var1);

    String getTaokeId();

    void setTaokeId(String var1);

    String getTaeId();

    void setTaeId(String var1);

    void setSearchPhraseTime(Context var1, long var2);

    long getSearchPhraseTime(Context var1);

    void setCurrentGestationState(Context var1, int var2);

    int getCurrentGestationState(Context var1);

    String getEBTabName(Context var1);

    void setEBTabName(Context var1, String var2);

    void setBrandTabName(Context var1, String var2);

    String getBrandtabName(Context var1);

    String getEBTitleName(Context var1);

    void setEBTitleName(Context var1, String var2);

    String getEBMyCartUrl(Context var1);

    void setEBMyCartUrl(Context var1, String var2);

    String getEBOrderUrl(Context var1);

    void setEBOrderUrl(Context var1, String var2);

    String getEBFavUrl(Context var1);

    void setEBFavUrl(Context var1, String var2);

    void setEBHomeUrl(Context var1, String var2);

    String getEBHomeUrl(Context var1);

    public static class PregnancyState {
        public static final int PREGNANCY_EARLY = 101;
        public static final int PREGNANCY_MID = 102;
        public static final int PREGNANCY_LATE = 103;
        public static final int PREGNANCY_NONE = 104;
        public static final int Gestation_MONTH = 10;
        public static final int MOTHER_NEW_BORN = 105;
        public static final int MOTHER_BABY = 106;
        public static final int MOTHER_CHILD = 107;

        public PregnancyState() {
        }
    }
}
