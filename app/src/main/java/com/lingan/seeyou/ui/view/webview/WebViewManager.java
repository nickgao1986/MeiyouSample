package com.lingan.seeyou.ui.view.webview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;

/**
 * Author: lwh
 * Date: 9/5/16 14:22.
 */
public class WebViewManager {

    private Context mContext;
    private WebViewConfig mWebViewConfig;
    //需要过滤的schema
    private List<String> mListSchema = new ArrayList<>();

    public WebViewManager(Context context) {
        mContext = context;
    }

    public WebViewManager(Context context,WebViewConfig webViewConfig) {
        mContext = context;
        mWebViewConfig = webViewConfig;
    }

    private String [] DomainArray = new String[]{"seeyouyima.com",
            "xixiaoyou.com",
            "meiyou.com",
            "wx.jaeapp.com",
            "upin.com",
            "fanhuan.com",
            "tataquan.com",
            "youzibuy.com",
            "xmmeiyou.com",
            "meetyouhuodong.com"};

    private boolean canAddParams(String strUrl) {
        try {
            if(StringUtils.isNull(strUrl))
                return false;
            for(String domain:DomainArray){
                if(strUrl.contains(domain)){
                    return true;
                }
            }
            /*if (!StringUtils.isNull(strUrl) && (strUrl.contains("seeyouyima.com")
                    || strUrl.contains("xixiaoyou.com")
                    || strUrl.contains("meiyou.com")
                    || strUrl.contains("wx.jaeapp.com")
                    || strUrl.contains("upin.com")
                    || strUrl.contains("fanhuan.com")
                    || strUrl.contains("tataquan.com")
                    || strUrl.contains("youzibuy.com")
                    || strUrl.contains("xmmeiyou.com")
                    || strUrl.contains("meetyouhuodong.com")
            )) {
                return true;
            }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 填充WEB URL 参数
     *
     * @param strUrl
     * @param mode
     * @return
     */
//    public String getWebUrlParams(String strUrl, int mode) {
//        /*try {
//            *//**
//             *
//             [ @".seeyouyima.com",
//             @".xixiaoyou.com",
//             @".meiyou.com",
//             @".wx.jaeapp.com",
//             @".upin.com",
//             @".fanhuan.com",
//             @".tataquan.com",
//             @".youzibuy.com" ]];
//             *//*
//            if (canAddParams(strUrl)) {
//                String deviceId = DeviceUtils.getDeviceId(mContext);
//                String imei = DeviceUtils.getImei(mContext);
//                String version = PackageUtil.getPackageInfo(mContext).versionName;//.getVersionNameForNetwork(context);
//                String umengChannel = ChannelUtil.getChannel(mContext);
//                int userId = BeanManager.getUtilSaver().getUserId(mContext) > 0 ? BeanManager.getUtilSaver().getUserId(mContext) : BeanManager.getUtilSaver().getUserVirtualId(mContext);
//                String token = BeanManager.getUtilSaver().getUserToken(mContext); //EcoController.getInstance(mContext()).getToken();
//                String tokenVirtual = BeanManager.getUtilSaver().getUserVirtualToken(mContext);// EcoController.getInstance(mContext()).getTokenVirtual();\
//
//                String strParams = "device_id=" + deviceId + "&platform=android&v=" + version
//                        + "&imei=" + imei + "&bundleid=" + umengChannel + "&mode=" + mode + ""
//                        + "&app_id=" + BeanManager.getUtilSaver().getPlatFormAppId();
//                //授权头
//                if (!StringUtils.isNull(token)) {
//                    strParams = (strParams + "&auth=" + URLEncoder.encode(token, "utf-8"));
//                }
//                if (!StringUtils.isNull(tokenVirtual)) {
//                    strParams = (strParams + "&v_auth=" + URLEncoder.encode(tokenVirtual, "utf-8"));
//                }
//                String statinfo = ChannelUtil.getStatisticInfo(mContext);
//                if (!StringUtils.isNull(statinfo)) {
//                    strParams = (strParams + "&statinfo=" + statinfo);
//                }
//                if (!StringUtils.isNull(BeanManager.getUtilSaver().getMyClient())) {
//                    strParams = (strParams + "&myclient=" + BeanManager.getUtilSaver().getMyClient());
//                }
//                if(mWebViewConfig!=null && mWebViewConfig.getThemeId()>0){
//                    strParams = (strParams + "&themeid=" + mWebViewConfig.getThemeId());
//                }
//                if (strUrl.contains("?")) {
//                    strParams = ("&myuid=" + userId + "&tbuid=" + BeanManager.getUtilSaver().getTbUserId(mContext) + "&" + strParams);
//                } else {
//                    strParams = ("?myuid=" + userId + "&tbuid=" + BeanManager.getUtilSaver().getTbUserId(mContext) + "&" + strParams);
//                }
//                //孕期针对小工具添加sdk版本
//                strParams += "&sdkversion=" + Build.VERSION.SDK_INT;
//                return strParams;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }*/
//        try {
//            /**
//             *
//             [ @".seeyouyima.com",
//             @".xixiaoyou.com",
//             @".meiyou.com",
//             @".wx.jaeapp.com",
//             @".upin.com",
//             @".fanhuan.com",
//             @".tataquan.com",
//             @".youzibuy.com" ]];
//             */
//            if (canAddParams(strUrl)) {
//                //获取统一参数
//                HashMap<String,String> resultMap = new HashMap<>();
//                HashMap<String,String> commomMap = getUrlCommomParams();
//                String token = BeanManager.getUtilSaver().getUserToken(mContext); //EcoController.getInstance(mContext()).getToken();
//                String tokenVirtual = BeanManager.getUtilSaver().getUserVirtualToken(mContext);// EcoController.getInstance(mContext()).getTokenVirtual();
//                //差异参数
//                if (!StringUtils.isNull(token)) {
//                    commomMap.put("auth", URLEncoder.encode(token, "utf-8"));
//                }
//                if (!StringUtils.isNull(tokenVirtual)) {
//                    commomMap.put("v_auth", URLEncoder.encode(tokenVirtual, "utf-8"));
//                }
//                if(mWebViewConfig!=null && mWebViewConfig.getThemeId()>0){
//                    commomMap.put("themeid", mWebViewConfig.getThemeId()+"");
//                }
//                //URL 携带参数
//                HashMap<String,String> originMap = (HashMap<String,String>)UrlUtil.getParamMapByUri(Uri.parse(strUrl));
//                if(originMap==null)
//                    originMap = new HashMap<>();
//                if(originMap.size()==0){
//                    //加入统一参数
//                    resultMap.putAll(commomMap);
//                }else{
//                    Iterator iter = commomMap.entrySet().iterator();
//                    while (iter.hasNext()) {
//                        HashMap.Entry entry = (HashMap.Entry) iter.next();
//                        String key = (String)entry.getKey();
//                        String value = (String)entry.getValue();
//                        //原始URL有的,不重复添加
//                        if(!originMap.containsKey(key)){
//                            resultMap.put(key,value);
//                        }
//                    }
//                }
//                //组装结果
//                StringBuilder stringBuilder = new StringBuilder();
//                if(resultMap.size()>0){
//                    if(!strUrl.contains("?")){
//                        stringBuilder.append("?");
//                    }else{
//                        stringBuilder.append("&");
//                    }
//                    Iterator iter = resultMap.entrySet().iterator();
//                    while (iter.hasNext()) {
//                        HashMap.Entry entry = (HashMap.Entry) iter.next();
//                        String key = (String)entry.getKey();
//                        String value = (String)entry.getValue();
//                        if(stringBuilder.toString().endsWith("?")){
//                            stringBuilder.append(key).append("=").append(value);
//                            continue;
//                        }
//                        if(!stringBuilder.toString().endsWith("&")){
//                            stringBuilder.append("&");
//                        }
//                        stringBuilder.append(key).append("=").append(value);
//                    }
//                }
//                return stringBuilder.toString();
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return "";
//    }

    //网页添加统一参数
    private String [] UrlCommomParams = new String[]{"device_id","platform","v","imei","bundleid","mode","app_id","statinfo","myclient","myuid","tbuid","sdkversion"};
    //可选参数 "auth","v_auth","themeid"
//    private HashMap<String,String> getUrlCommomParams(){
//        HashMap<String,String> hashMap =new HashMap<>();
//        try {
//            hashMap.put("device_id",DeviceUtils.getDeviceId(mContext));
//            hashMap.put("platform","android");
//            hashMap.put("v",PackageUtil.getPackageInfo(mContext).versionName);
//            hashMap.put("imei",DeviceUtils.getImei(mContext));
//            hashMap.put("bundleid",ChannelUtil.getChannel(mContext));
//            hashMap.put("mode",BeanManager.getUtilSaver().getUserIdentify(mContext) + "");
//            hashMap.put("app_id",BeanManager.getUtilSaver().getPlatFormAppId() + "");
//            hashMap.put("statinfo", ChannelUtil.getStatisticInfo(mContext) + "");
//            hashMap.put("myclient", BeanManager.getUtilSaver().getMyClient() + "");
//            int userId = BeanManager.getUtilSaver().getUserId(mContext) > 0 ? BeanManager.getUtilSaver().getUserId(mContext) : BeanManager.getUtilSaver().getUserVirtualId(mContext);
//            hashMap.put("myuid", userId + "");
//            hashMap.put("tbuid", BeanManager.getUtilSaver().getTbUserId(mContext) + "");
//            hashMap.put("sdkversion", Build.VERSION.SDK_INT + "");
//            return hashMap;
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//        return hashMap;
//    }

    /**
     * 填充URL HEADER 参数
     *
     * @return
     */
//    public HashMap<String, String> getWebRequestHeader(String url) {
//        HashMap<String, String> mapHeader = new HashMap<String, String>();
//        try {
//            if (canAddParams(url)) {
//                //公用头部
//                HashMap<String, String> commomMap = getUrlCommomParams();
//                mapHeader.putAll(commomMap);
//                //授权头
//                String token = BeanManager.getUtilSaver().getUserToken(mContext);
//                String tokenVirtual = BeanManager.getUtilSaver().getUserVirtualToken(mContext);
//                if (!StringUtils.isNull(token)) {
//                    mapHeader.put(LinganProtocol.KEY_AUTH, LinganProtocol.VALUE_AUTH_PREFIX + String.valueOf(token)/*URLEncoder.encode(token, "utf-8")*/);
//                }
//                if (!StringUtils.isNull(tokenVirtual)) {
//                    mapHeader.put(LinganProtocol.KEY_AUTH_V, LinganProtocol.VALUE_AUTH_V_PREFIX + String.valueOf(tokenVirtual)/*URLEncoder.encode(tokenVirtual, "utf-8")*/);
//                }
//                //支持HttpDns
//                /*if(HttpDnsController.getInstance().getHttpDnsCacheManager()!=null){
//                    String domain = HttpDnsController.getInstance().getHttpDnsCacheManager().getDomian(strUrl);
//                    if(!StringUtils.isNull(domain)){
//                        String ip = HttpDnsController.getInstance().getHttpDnsCacheManager().getIpByDomain(domain);
//                        if(!StringUtils.isNull(ip)){
//                            strUrl = HttpDnsController.getInstance().getHttpDnsCacheManager().replaceDomianToIp(strUrl,domain,ip);
//                            mapHeader.put("Host",domain);
//                        }
//                    }
//                }*/
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return mapHeader;
//    }

    public List<String> getListSchema() {
        return mListSchema;
    }

    public void setListSchema(List<String> listSchema) {
        mListSchema = listSchema;
    }
}
