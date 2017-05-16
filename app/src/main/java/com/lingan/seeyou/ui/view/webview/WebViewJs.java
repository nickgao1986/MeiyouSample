package com.lingan.seeyou.ui.view.webview;


import android.webkit.JavascriptInterface;

import nickgao.com.framework.utils.LogUtils;

/**
 * Author: lwh
 * Date: 4/26/16 09:48.
 */
public class WebViewJs {

    private static final String TAG ="WebViewJs";
    private JsCallback mJsCallback;
    public WebViewJs(){

    }
    public WebViewJs(JsCallback jsCallback){
        mJsCallback = jsCallback;
    }
    @JavascriptInterface
    public void callback(int value){
        LogUtils.d(TAG, "-->WebViewJs callback:" + value);
        if(mJsCallback!=null){
            mJsCallback.onResult(value+"");
        }
        //ToastUtils.showToast(getApplicationContext(),"JS返回值:"+value);
    }
    @JavascriptInterface
    public void callbackArr(String value){
        LogUtils.d(TAG, "-->WebViewJs callback array result:" + value);
        if(mJsCallback!=null){
            mJsCallback.onResult(value+"");
        }
        //ToastUtils.showToast(getApplicationContext(),"JS返回值:"+value);
    }

    @JavascriptInterface
    public void androidlog(String log){
        LogUtils.d(TAG, "-->WebViewJs log:" + log);
    }
}
