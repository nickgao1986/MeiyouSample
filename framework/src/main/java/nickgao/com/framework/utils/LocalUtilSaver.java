package nickgao.com.framework.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public class LocalUtilSaver implements UtilSaver {
    public final int NORMAL = 0;
    public final int PREGNANCY_BABY = 1;
    public final int PREGNANCY_PREPARE = 2;
    public final int MOTHIER = 3;
    public final int WEIGHT_LOSS_MODE = 4;
    public final int IDETITY = -322;
    private static String sina_appkey = "2295430774";
    private static String sina_secret = "25bd0546a6b5b09083bef54f493eb880";
    private static String sina_redirect_uri = "http://www.xixiaoyou.com";
    private static String sina_scope = "email,direct_messages_read,direct_messages _write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
    private static String qzone_client_id = "100422627";
    private static String qzone_client_key = "4d1327bb392cc8c15c96483c187766aa";
    private static String qzone_scope = "get_user_info,add_share,get_app_friends,get_simple_userinfo,report_menstrual,report_pregnancy";
    private static String wx_app_id = "wx8106931044a0ee03";
    private static String wx_app_secret = "68d14d0d922699f0e1fa5d49234bf787";
    private static String wx_public_app_id = "wx1057fb9fdc1bf0dc";
    private static String wx_pay_app_id = "wx8106931044a0ee03";
    private static String wx_pay_app_secret = "68d14d0d922699f0e1fa5d49234bf787";
    private static String xiaomi_app_id = "1006165";
    private static String xiaomi_app_key = "890100631165";
    private static String xiaomi_app_secret = " Rfwee/se7JXWtYTjErSFmLrwZV2xKjNTLyOq/N15Xp4=";
    private static String NativePosID = "1070203564382543";
    private static String app_id = "1";
    private static Context mContext;
    private static int mTcpDeviceType;
    private static boolean isUIVisible = false;
    private static boolean bEnterMain = false;
    private static boolean isbacktomain = false;
    private static int mUnreadCount;
    private static String strPlatform = "android";
    private static String strPlatformAppId = "0";
    private static List<String> listForbidUrl = new ArrayList();
    private static int image_upload_way = 1;
    private static boolean isAppInBackgroud = false;
    private static String myclient = "";
    private static String client = "";
    private static String tabaoid = "mm_57126793_0_0";
    private static String taokeid = "";
    private static String taeid = "";

    public LocalUtilSaver() {
    }

    public String getNativePosID() {
        return NativePosID;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setXiaomiParams(String appid, String appkey, String app_secret) {
        xiaomi_app_id = appid;
        xiaomi_app_key = appkey;
        xiaomi_app_secret = app_secret;
    }

    public String getXiaomiAppKey() {
        return xiaomi_app_key;
    }

    public String getXiaomi_app_id() {
        return xiaomi_app_id;
    }

    public String getXiaomi_app_secret() {
        return xiaomi_app_secret;
    }

    public String getSINA_APPKEY() {
        return sina_appkey;
    }

    public String getSINA_SECRET() {
        return sina_secret;
    }

    public String getSINA_SCOPE() {
        return sina_scope;
    }

    public String getSINA_REDIRECT_URI() {
        return sina_redirect_uri;
    }

    public void setSINA_PARAMS(String appkey, String app_secret, String redirect_uri, String scope) {
        sina_appkey = appkey;
        sina_secret = app_secret;
        sina_redirect_uri = redirect_uri;
        sina_scope = scope;
    }

    public String getQZONE_CLIENT_ID() {
        return qzone_client_id;
    }

    public String getQZONE_SECRET() {
        return qzone_client_key;
    }

    public String getQZONE_SCOPE() {
        return qzone_scope;
    }

    public void setQZONE_PARAMS(String appkey, String app_secret, String redirect_uri, String scope) {
        qzone_client_id = appkey;
        qzone_client_key = app_secret;
        qzone_scope = scope;
    }

    public String getWX_APP_ID() {
        return wx_app_id;
    }

    public String getWX_APP_SECRET() {
        return wx_app_secret;
    }

    public String getWX_PUBLIC_APP_ID() {
        return wx_public_app_id;
    }

    public void setWX_PARAMS(String appkey, String app_secret, String publicid) {
        wx_app_id = appkey;
        wx_app_secret = app_secret;
        wx_public_app_id = publicid;
    }

    public String getWX_PAY_APP_ID() {
        return wx_pay_app_id;
    }

    public String getWX_PAY_APP_SECRET() {
        return wx_pay_app_secret;
    }

    public void setWX_PAY_PARAMS(String appid, String secret) {
        wx_pay_app_id = appid;
        wx_pay_app_secret = secret;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void setTcpDeviceType(int deviceType) {
        mTcpDeviceType = deviceType;
    }

    public int getTcpDeviceType() {
        return mTcpDeviceType;
    }

    public boolean isAppUIVisible() {
        return isUIVisible;
    }

    public void setAppUIVisible(boolean flag) {
        isUIVisible = flag;
    }

    public void setEnteredMain(boolean flag) {
        bEnterMain = flag;
    }

    public boolean isEnterMained() {
        return bEnterMain;
    }

    public boolean isBackToMain() {
        return isbacktomain;
    }

    public void setBackToMain(boolean flag) {
        isbacktomain = flag;
    }

    public void setMeetyouNotifyOpen(Context context, boolean flag) {
        Pref.saveBoolean(context, "meetyou_notify", flag);
    }

    public boolean isMeetyouNotifyOpen(Context context) {
        return Pref.getBoolean(context, "meetyou_notify", false);
    }

    public int getUnreadCount() {
        return mUnreadCount;
    }

    public void setUnreadCount(Context context, int unreadCount) {
        mUnreadCount = unreadCount;
    }

    public int getPictureQuality(Context context) {
        return Pref.getInt("picture_quality", context, 40);
    }

    public void setPictureQuality(Context context, int quality) {
        Pref.saveInt("picture_quality", quality, context);
    }

    public boolean isThumbMode(Context context) {
        return Pref.getBoolean(context, "isThumbMode", true);
    }

    public void setThumbMode(Context context, boolean flag) {
        Pref.saveBoolean(context, "isThumbMode", flag);
    }

    public String getPlatForm() {
        return strPlatform;
    }

    public void setPlatForm(String platForm) {
        strPlatform = platForm;
    }

    public String getPlatFormAppId() {
        return strPlatformAppId;
    }

    public void setPlatFormAppId(String platForm) {
        strPlatformAppId = platForm;
    }

    public boolean isForbidUrl(Context context, String url) {
        boolean isHas = false;
        Iterator var4 = listForbidUrl.iterator();

        String subUrl;
        do {
            if(!var4.hasNext()) {
                return false;
            }

            subUrl = (String)var4.next();
        } while(!url.contains(subUrl));

        return true;
    }

    public int getUploadImageWay() {
        return image_upload_way;
    }

    public void setUploadImageWay(int way) {
        image_upload_way = way;
    }

    public void saveBabyoutDate(Context context, long time) {
        Pref.saveLong("babyout_date", context, time);
    }

    public String getBabyoutDateString(Context context) {
        try {
            long ex = Pref.getLong("babyout_date", context, 0L);
            Calendar calendar = (Calendar)Calendar.getInstance().clone();
            calendar.setTimeInMillis(ex);
            SimpleDateFormat DAY = new SimpleDateFormat("yyyy-M-d");
            String date = DAY.format(calendar.getTime());
            return date;
        } catch (Exception var7) {
            var7.printStackTrace();
            return "";
        }
    }

    public String getPregnancyYuchanTimeString(Context context) {
        String time = Pref.getString("yuchan_time_calendar", context);
        int endIndex = time.indexOf(" ");
        if(endIndex != -1) {
            time = time.substring(0, endIndex);
        }

        return time;
    }

    public void setPregnancyYuchanTimeCalendar(Context context, Calendar calendar) {
        Pref.saveString("yuchan_time_calendar", CalendarUtil.convertCalendarToString(calendar), context);
    }

    public Calendar getPregnancyYuchanTimeCalendar(Context context) {
        if(StringUtils.isNull(this.getPregnancyYuchanTimeString(context))) {
            return null;
        } else {
            Calendar calendar = CalendarUtil.convertStringToCalendar(Pref.getString("yuchan_time_calendar", context));
            return calendar;
        }
    }

    public void setIsYuchanTimeChangeLastOpenSeachCircleActivity(Context context, boolean flag) {
        Pref.saveBoolean(context, "is_change_last_open_seach", flag);
    }

    public boolean getIsYuchanTimeChangeLastOpenSeachCircleActivity(Context context) {
        return Pref.getBoolean(context, "is_change_last_open_seach", true);
    }

    public void setPregnancyStartTime(Context context, Calendar calendar) {
        Pref.saveString("pregnancy_start_time", CalendarUtil.convertCalendarToString(calendar), context);
    }

    public Calendar getPregnancyStartTime(Context context) {
        Calendar calendar = CalendarUtil.convertStringToCalendar(Pref.getString("pregnancy_start_time", context));
        return calendar;
    }

    public void saveUserCircleNickName(Context context, String name) {
        Pref.saveString("circle_nick_name", name, context);
    }

    public String getUserCircleNickName(Context context) {
        return Pref.getString("circle_nick_name", context);
    }

    public void saveUserId(Context context, int userId) {
        Pref.saveString("user_Id", userId + "", context);
    }

    public int getUserId(Context context) {
        String userId = Pref.getString("user_Id", context);
        return StringUtils.getInt(userId);
    }

    public void saveTbUserId(Context context, String userId) {
        Pref.saveString("tb_user_Id", userId + "", context);
    }

    public String getTbUserId(Context context) {
        String userId = Pref.getString("tb_user_Id", context);
        return userId;
    }

    public void saveUserVirtualId(Context context, int userId) {
        Pref.saveString("user_Id_virtual", userId + "", context);
    }

    public int getUserVirtualId(Context context) {
        String userId = Pref.getString("user_Id_virtual", context);
        return StringUtils.getInt(userId);
    }

    public void saveUserVirtualToken(Context context, String token) {
        Pref.saveString("user_Id_virtual_token", token + "", context);
    }

    public String getUserVirtualToken(Context context) {
        String token = Pref.getString("user_Id_virtual_token", context);
        return token;
    }

    public void saveUserToken(Context context, String token) {
        Pref.saveString("user_Id_token", token + "", context);
    }

    public String getUserToken(Context context) {
        String token = Pref.getString("user_Id_token", context);
        return token;
    }

    public void saveUserIdentify(Context context, int value) {
        Pref.saveString("user_identify", value + "", context);
    }

    public int getUserIdentify(Context context) {
        String value = Pref.getString("user_identify", context);
        return StringUtils.getInt(value);
    }

    public void saveSkinPackageName(Context context, String packageName) {
        Pref.saveString("skin_package_name" + this.getUserId(context), packageName, context);
    }

    public String getSkinPackageName(Context context) {
        return Pref.getString("skin_package_name" + this.getUserId(context), context);
    }

    public void saveSkinName(Context context, String skinName) {
        Pref.saveString("skin_name" + this.getUserId(context), skinName, context);
    }

    public String getSkinName(Context context) {
        String name = Pref.getString("skin_name" + this.getUserId(context), context);
        return StringUtils.isNull(name)?"默认":name;
    }

    public void saveSkinNightName(Context context, String skinName) {
        Pref.saveString("skin_night_name" + this.getUserId(context), skinName, context);
    }

    public String getSkinNightName(Context context) {
        String name = Pref.getString("skin_night_name" + this.getUserId(context), context);
        return StringUtils.isNull(name)?"默认":name;
    }

    public void saveSkinApkName(Context context, String apk) {
        Pref.saveString("skin_apk_name" + this.getUserId(context), apk, context);
    }

    public String getSkinApkName(Context context) {
        return Pref.getString("skin_apk_name" + this.getUserId(context), context);
    }

    public void saveNightSkinApkName(Context context, String apk) {
        Pref.saveString("night_skin_apk_name" + this.getUserId(context), apk, context);
    }

    public String getNightSkinApkName(Context context) {
        return Pref.getString("night_skin_apk_name" + this.getUserId(context), context);
    }

    public void setIsNightMode(Context context, boolean isNightMode) {
        Pref.saveBoolean(context, "is_night_mode" + this.getUserId(context), isNightMode);
    }

    public boolean getIsNightMode(Context context) {
        return Pref.getBoolean(context, "is_night_mode" + this.getUserId(context), false);
    }

    public String getPasswords(Context context) {
        return Pref.getString("password", context);
    }

    public void setPasswords(Context context, String password) {
        Pref.saveString("password", password, context);
    }

    public void setAppInBackgroud(boolean flag) {
        isAppInBackgroud = flag;
    }

    public boolean isIsAppInBackgroud() {
        return isAppInBackgroud;
    }

    public boolean isPasswordEmpty(Context context) {
        String password = this.getPasswords(context);
        return StringUtils.isNull(password);
    }

    public boolean isShowPasswordPage(Context context) {
        return !this.isPasswordEmpty(context)?Pref.getBoolean(context, "show_pswd_page", false):false;
    }

    public void setShowPswdPage(Context context, boolean flag) {
        if(!this.isPasswordEmpty(context)) {
            Pref.saveBoolean(context, "show_pswd_page", flag);
        } else {
            Pref.saveBoolean(context, "show_pswd_page", false);
        }

    }

    public String getEBTabName(Context context) {
//        String tabName = Pref.getString("eb_tab_name", context);
//        return StringUtils.isNull(tabName)?context.getString(string.today_sale_tab):tabName;
        return null;
    }

    public void setEBTabName(Context context, String ebTabName) {
        Pref.saveString("eb_tab_name", ebTabName, context);
    }

    public void setBrandTabName(Context context, String ebTabName) {
    }

    public String getBrandtabName(Context context) {
        return null;
    }

    public String getEBTitleName(Context context) {
//        String titleName = Pref.getString("eb_title_name", context);
//        return StringUtils.isNull(titleName)?context.getString(string.today_sale_tab):titleName;
        return null;
    }

    public void setEBTitleName(Context context, String ebTitleName) {
        Pref.saveString("eb_title_name", ebTitleName, context);
    }

    public String getEBMyCartUrl(Context context) {
        return Pref.getString("eb_cart_url", context);
    }

    public void setEBMyCartUrl(Context context, String ebCartUrl) {
        Pref.saveString("eb_cart_url", ebCartUrl, context);
    }

    public String getEBOrderUrl(Context context) {
        return Pref.getString("eb_order_url", context);
    }

    public void setEBOrderUrl(Context context, String ebCartUrl) {
        Pref.saveString("eb_order_url", ebCartUrl, context);
    }

    public String getEBFavUrl(Context context) {
        return Pref.getString("eb_fav_url", context);
    }

    public void setEBFavUrl(Context context, String ebFavUrl) {
        Pref.saveString("eb_fav_url", ebFavUrl, context);
    }

    public void setEBHomeUrl(Context context, String ebHomeUrl) {
        Pref.saveString("eb_home_url", ebHomeUrl, context);
    }

    public String getEBHomeUrl(Context context) {
        return Pref.getString("eb_home_url", context);
    }

    public String getMyClient() {
        return myclient;
    }

    public void setMyClient(String client) {
        myclient = client;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String tmp) {
        client = tmp;
    }

    public String getTaobaoId() {
        return tabaoid;
    }

    public void setTaobaoId(String id) {
        tabaoid = id;
    }

    public String getTaokeId() {
        return taokeid;
    }

    public void setTaokeId(String id) {
        taokeid = id;
    }

    public String getTaeId() {
        return taeid;
    }

    public void setTaeId(String id) {
        taeid = id;
    }

    public void setSearchPhraseTime(Context context, long time) {
        Pref.saveLong("isSearchPhraseFirstTime" + this.getUserId(context), context, time);
    }

    public long getSearchPhraseTime(Context context) {
        return Pref.getLong("isSearchPhraseFirstTime" + this.getUserId(context), context, 0L);
    }

    public void setCurrentGestationState(Context context, int state) {
        Pref.saveInt("CurrentGestationState" + this.getUserId(context), state, context);
    }

    public int getCurrentGestationState(Context context) {
        return Pref.getInt("CurrentGestationState" + this.getUserId(context), context, 0);
    }

    public String toString() {
        return super.toString();
    }
}
