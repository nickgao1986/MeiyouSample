package com.lingan.seeyou.ui.view.webview;

import android.content.Context;

import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;
import java.util.List;

import nickgao.com.meiyousample.utils.LogUtils;

/**
 * Created by lwh on 2015/11/3.
 */
public class WebViewController {
    private static WebViewController instance;

    public static WebViewController getInstance() {
        if (instance == null) {
            instance = new WebViewController();
        }
        return instance;
    }

    private WebViewConfig webViewConfig;
    private WebViewListener webViewListener;
    private WebViewManager mWebViewManager;

    /**
     * 初始化
     *
     * @param config
     * @param webViewListener
     */
    public void init(Context context, WebViewConfig config, WebViewListener webViewListener) {
        try {
            this.webViewConfig = config;
            this.webViewListener = webViewListener;
            this.mWebViewManager = new WebViewManager(context,webViewConfig);

           /* if(ConfigManager.from(context).isDebug() || ConfigManager.from(context).isTest()){
                QbSdk.forceSysWebView();
            }*/
            QbSdk.initX5Environment(context, /*QbSdk.WebviewInitType.PRELOAD_ONLY,*/ null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否安装Android System WebView,有的话,不启用X5
     * com.google.android.webview;
     * @return
     */
    private boolean isInstallSysWebView(Context context){
//        try {
//            return PackageUtil.isAppInstalled(context,"com.google.android.webview");
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
        return false;
    }


    public WebViewListener getWebViewListener() {
        return webViewListener;
    }

    public WebViewManager getWebViewManager() {
        return mWebViewManager;
    }

    /**
     * 填充WEB URL 参数
     *
     * @param strUrl
     * @param mode
     * @return
     */
    public String getWebUrlParams(String strUrl, int mode) {
//        try {
//
//            if (mWebViewManager == null)
//                return "";
//            return mWebViewManager.getWebUrlParams(strUrl, mode);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return "";
    }

    /**
     * 填充URL HEADER 参数
     *
     * @return
     */

    public HashMap<String, String> getWebRequestHeader(String url) {
//        if (mWebViewManager != null)
//            return mWebViewManager.getWebRequestHeader(url);
        return new HashMap<>();
    }

    /**
     * 设置服务端配置拦截的schema
     *
     * @param list
     */

    public void setWebIntercepterSchemaList(List<String> list) {
        if (mWebViewManager != null) {
            mWebViewManager.setListSchema(list);
        }
    }


   public WebViewConfig getWebViewConfig() {
       if(webViewConfig==null){
           webViewConfig = WebViewConfig.newBuilder().build();
           LogUtils.e("WebViewConfig","为了防止NPE 设置了默认的Config,请检查代码!!!");
       }
       return webViewConfig;
    }

 /*
    public void setWebViewConfig(WebViewConfig webViewConfig) {
        this.webViewConfig = webViewConfig;
    }
    public void setWebViewListener(WebViewListener webViewListener) {
        this.webViewListener = webViewListener;
    }

  public void handleAutoPlayVideo(WebView webView){
        try {
            webView.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('video'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String getResultString(int shareType, boolean success) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", shareType);
            jsonObject.put("success", success ? 1 : 0);
            return jsonObject.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }*/
}
