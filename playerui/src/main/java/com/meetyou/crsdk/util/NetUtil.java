package com.meetyou.crsdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gaoyoujian on 2017/5/11.
 */

public class NetUtil {

    public NetUtil() {
    }

//    public static HttpURLConnection buildConnection(String url) throws IOException {
//        return buildConnection(url, false);
//    }
//
//    public static HttpURLConnection buildConnection(String url, boolean isAlive) throws IOException {
//        HttpURLConnection connection = (HttpURLConnection)(new URL(url)).openConnection();
//        connection.setRequestProperty("Accept-Encoding", "identity");
//        connection.setConnectTimeout(Integer.parseInt(HttpConnPars.CONNECT_TIMEOUT.content));
//        if(isAlive) {
//            connection.setRequestProperty(HttpConnPars.KEEP_CONNECT.header, HttpConnPars.KEEP_CONNECT.content);
//        }
//
//        return connection;
//    }

    public static int getNetWorkType(Context context) {
        int type = 0;
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService("connectivity");
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            String typeName = networkInfo.getTypeName();
            if(typeName.equalsIgnoreCase("WIFI")) {
                type = 4;
            } else if(typeName.equalsIgnoreCase("MOBILE")) {
                String proxyHost = Proxy.getDefaultHost();
                type = TextUtils.isEmpty(proxyHost)?(isFastMobileNetwork(context)?3:2):1;
            }
        }

        return type;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        switch(telephonyManager.getNetworkType()) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return true;
            case 4:
                return false;
            case 5:
                return true;
            case 6:
                return true;
            case 7:
                return false;
            case 8:
                return true;
            case 9:
                return true;
            case 10:
                return true;
            case 11:
                return false;
            case 12:
                return true;
            case 13:
                return true;
            case 14:
                return true;
            case 15:
                return true;
            default:
                return false;
        }
    }

    public static boolean isWifiConnect(Context mContext) {
        ConnectivityManager conManager = (ConnectivityManager)mContext.getSystemService("connectivity");
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.getType() == 1;
    }

    public static String getProvidersName(Context mContext) {
        TelephonyManager mTelephonyManager = (TelephonyManager)mContext.getSystemService("phone");
        String str = "";

        try {
            String localException = mTelephonyManager.getSubscriberId();
            if(localException == null) {
                return "未知";
            }

            if(localException.startsWith("46000")) {
                str = "中国移动";
            } else if(localException.startsWith("46002")) {
                str = "中国移动";
            } else if(localException.startsWith("46001")) {
                str = "中国联通";
            } else if(localException.startsWith("46003")) {
                str = "中国电信";
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return str;
    }

}
