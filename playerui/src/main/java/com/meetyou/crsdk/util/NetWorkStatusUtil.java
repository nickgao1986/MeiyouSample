package com.meetyou.crsdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by gaoyoujian on 2017/5/11.
 */

public class NetWorkStatusUtil {

    public static final String CMWAP = "cmwap";
    public static final String CMNET = "cmnet";
    public static final String GWAP_3 = "3gwap";
    public static final String GNET_3 = "3gnet";
    public static final String UNIWAP = "uniwap";
    public static final String UNINET = "uninet";
    public static final String CTWAP = "ctwap";
    public static final String CTNET = "ctnet";
    public static final String CTC = "CTC";
    private static final int BLUETOOTH = 4;
    private static final int BRIGHTNESS = 1;
    private static final int GPS = 3;
    private static final int SYNC = 2;
    private static final int WIFI = 0;
    private static final String TAG = "NetWorkStatusUtil";
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    public static final int NETWORK_TYPE_GPRS = 1;
    public static final int NETWORK_TYPE_EDGE = 2;
    public static final int NETWORK_TYPE_UMTS = 3;
    public static final int NETWORK_TYPE_CDMA = 4;
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    public static final int NETWORK_TYPE_EVDO_A = 6;
    public static final int NETWORK_TYPE_1xRTT = 7;
    public static final int NETWORK_TYPE_HSDPA = 8;
    public static final int NETWORK_TYPE_HSUPA = 9;
    public static final int NETWORK_TYPE_HSPA = 10;
    public static final int NETWORK_TYPE_IDEN = 11;
    public static final int NETWORK_TYPE_EVDO_B = 12;
    public static final int NETWORK_TYPE_LTE = 13;
    public static final int NETWORK_TYPE_EHRPD = 14;
    public static final int NETWORK_TYPE_HSPAP = 15;
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final int NETWORK_CLASS_2_G = 1;
    public static final int NETWORK_CLASS_3_G = 2;
    public static final int NETWORK_CLASS_4_G = 3;
    public static final int NETWORK_CLASS_WIFI = 4;

    public static boolean isWifi(Context ctx) {
        ConnectivityManager conManager = (ConnectivityManager)ctx.getApplicationContext().getSystemService("connectivity");
        NetworkInfo ni = conManager.getActiveNetworkInfo();
        return ni != null && ni.getTypeName() != null?ni.getTypeName().compareToIgnoreCase("WIFI") == 0:false;
    }


    public static boolean queryNetWork(Context ctx) {
        if(ctx == null) {
            throw new NullPointerException("Context == null");
        } else {
            try {
                boolean e = getNetworkConnectionStatus(ctx);
                return e;
            } catch (Exception var2) {
                return true;
            }
        }
    }

    public static boolean getNetworkConnectionStatus(Context context) {
        try {
            ConnectivityManager e = (ConnectivityManager)context.getSystemService("connectivity");
            if(e == null) {
                return false;
            } else {
                NetworkInfo info = e.getActiveNetworkInfo();
                if(info == null) {
                    return false;
                } else {
                    TelephonyManager tm = (TelephonyManager)context.getSystemService("phone");
                    return tm == null?false:(tm.getDataState() == 2 || tm.getDataState() == 0) && info != null;
                }
            }
        } catch (Exception var4) {
            return true;
        }
    }


    public static int getNetType(Context context) {
        try {
            if(isWifi(context)) {
                return 4;
            } else {
                TelephonyManager ex = (TelephonyManager)context.getSystemService("phone");
                switch(ex.getNetworkType()) {
                    case 1:
                    case 2:
                    case 4:
                        return 1;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 12:
                        return 2;
                    case 7:
                    case 9:
                    case 10:
                    case 11:
                    default:
                        return 0;
                    case 13:
                        return 3;
                }
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            return 0;
        }
    }

}
