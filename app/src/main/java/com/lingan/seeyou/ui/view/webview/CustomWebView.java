package com.lingan.seeyou.ui.view.webview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.tencent.smtt.sdk.WebSettings;

import java.util.Map;

import nickgao.com.meiyousample.utils.LogUtils;

/**
 * 自定义webview，用于监听滑动的事件
 * Created by Administrator on 2015/5/19.
 */
public class CustomWebView extends com.tencent.smtt.sdk.WebView {
    private static final String TAG = "CustomWebView";
    public ScrollInterface mScrollInterface;

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollInterface != null) {
            mScrollInterface.onSChanged(l, t, oldl, oldt);
        }
    }

    /**
     * 手动调用
     */
    public void init(WebViewConfig webConfig) {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        // FIXME: 17/2/21 zxb 测试webView自动缓存；
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //webSettings.setDomStorageEnabled(true);
        //以下两个设置会导致 资讯页 高度变长
        //webSettings.setUseWideViewPort(true);
        //webSettings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT > 16) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            webSettings.setPluginState(WebSettings.PluginState.ON);
        }
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        //追加修改WebView UA
        if (webConfig != null && webConfig.isAppendUserAgent()) {
            String ua = webSettings.getUserAgentString();
            String uaNew = ua + " MeetYouClient/1.0.0 ()";
            webSettings.setUserAgentString(uaNew);
            LogUtils.d(TAG, "ua:" + ua + "==>uaNew:" + uaNew + "==>webSettings.getUserAgentString:" + webSettings
                    .getUserAgentString());
        }


        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    public void setOnCustomScroolChangeListener(ScrollInterface scrollInterface) {
        this.mScrollInterface = scrollInterface;
    }

    public interface ScrollInterface {
        public void onSChanged(int l, int t, int oldl, int oldt);
    }

    boolean isFirst = true;

    @Override
    public void loadUrl(String url) {
        try {
            if (isFirst) {
                firstUrl = url;
                isFirst = false;
            }

            if (!url.equals(firstUrl)) {
                interruptGoBack = false;
            }
            super.loadUrl(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    String firstUrl = "";

    @Override
    public void loadUrl(String strUrl, Map<String, String> map) {
        boolean loadCache = isLoadCache(strUrl, this.getContext());
        if (!loadCache) {
            super.loadUrl(strUrl, map);
        }
    }

    /**
     * 资讯页是否直接从缓存里读取
     *
     * @param strUrl
     * @param mContext
     * @return
     */
    public boolean isLoadCache(String strUrl, Context mContext) {
        boolean isLoadData = false;
//        try {
//            String newsUrl = "news-node.seeyouyima.com";
//            URL url = null;
//
//            url = new URL(strUrl);
//
//            String host = url.getHost();
//            String protocol = url.getProtocol();
//            String baseUrl = protocol + "://" + host;
////                有缓存使用LoadData;
//            if (WebCacheHelper.getInstance().hasCache(strUrl) && host.contains(newsUrl)) {
//                InputStream inputStream = WebViewCacheManager.getInstance(mContext)
//                                                             .readCache(strUrl);
//                if (inputStream != null) {
//                    String string = null;
//                    try {
//                        int identify = BeanManager.getUtilSaver()
//                                                  .getUserIdentify(mContext);
//                        String urlParams = WebViewController.getInstance()
//                                                            .getWebUrlParams(baseUrl, identify);
//                        baseUrl = baseUrl + urlParams;
//                        // FIXME: 17/2/22 不用拼接BaseURL，直接使用StrURl，网络加载也能渠道资源；
//                        string = IOUtils.readStreamAsString(inputStream, "UTF-8");
//                        loadDataWithBaseURL(baseUrl, string, "text/html", "UTF-8", null);
//                        isLoadData = true;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return isLoadData;
    }

    /**
     * 返回堆栈最上一级
     */
    public void loadTopStackUrl() {
        //清除记录
        clearHistory();
        interruptGoBack = true;
        loadUrl(firstUrl);
    }

    /**
     * 拦截返回事件，当设置为栈顶则拦截返回
     */
    boolean interruptGoBack = false;

    @Override
    public boolean canGoBack() {

        if (interruptGoBack) {
            return false;
        } else
            return super.canGoBack();
    }

    public String getTopStackUrl() {
        return firstUrl;
    }

}
