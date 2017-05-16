package com.lingan.seeyou.ui.view.webview;

import android.content.Context;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.utils.UrlUtil;

/**
 * WebView 手动下载缓存管理；
 */
public class WebViewCacheManager {
    private static final String TAG = "WebViewCacheManager";

    private static WebViewCacheManager instance;
    private Context context;
    private WebViewCache mCache;
    private WebViewCacheLoader mWebCacheLoader;
    private String mCurrentUrl;

    /**
     * 线程池标识
     */
    public static final String poolGroup = "webview-cache";


    private WebViewCacheManager(Context context) {
        this.context = context;
        mCache = WebViewCache.get(context);
        mWebCacheLoader = new WebViewCacheLoader(context);
    }

    public static synchronized WebViewCacheManager getInstance(Context context) {
        if (instance == null) {
            instance = new WebViewCacheManager(context);
        }
        return instance;
    }


    /**
     * 读取缓存
     *
     * @param url
     */

    public InputStream readCache(String url) {
        String key = getKeyFromUrl(url);
        byte[] ret = mCache.getAsBinary(key);
        if (ret == null) {
            LogUtils.d(TAG, "读取cache为空: key:" + key + "对应url:" + url);
            return null;
        }
        LogUtils.d(TAG, "读取cache成功: key:" + key + "对应url:" + url);
        return new ByteArrayInputStream(ret);
    }

    /**
     * 从网络下载资源(html、CSS、Image等)，并存入缓存
     *
     * @param url         原始资源URL
     * @param expiredTime 多久后过期
     */
    public void downloadFromNetToCache(String url, int expiredTime) {
        String key = getKeyFromUrl(url);
        //已经下载的过资源，不再下载；如果需要多次测试需要禁用
        boolean hasDownload = hasDownload(key, url);
        if (hasDownload) return;
        byte[] ret = mWebCacheLoader.downloadWebResoucr(url);
        if (ret != null) {
            mCache.put(key, ret, expiredTime);
            LogUtils.d(TAG, "加入cache成功: key:" + key + "对应url:" + url);
        }
    }

    public InputStream downloadForWebview(String url) {
//        String fileName = URLUtil.guessFileName(url);

        ByteArrayInputStream inputStream = null;
        String key = getKeyFromUrl(url);
        //已经下载的过资源，不再下载；如果需要多次测试需要禁用
        boolean hasDownload = hasDownload(key, url);
        byte[] ret = mWebCacheLoader.downloadWebResoucr(url);
        if (ret != null) {
            int expireTime = expiredTime(url);
            mCache.put(key, ret, expireTime);
            LogUtils.d(TAG, "downloadForWebview 加入cache成功: key:" + key + "对应url:" + url);
            inputStream = new ByteArrayInputStream(ret);
        }

        return inputStream;

    }

    /**
     * 拦截图片URL
     * @param url
     * @return
     */
    public static String interceptorUrl(String url) {
        if (UrlUtil.urlIsImg(url)) {
            //图片地址转为Webp
            if (url.contains("?imageView2/")) {
                url += "/format/webp";
            }
        }
        return url;
    }

    /**
     * 已经下载的过资源，不再下载；如果需要多次测试需要禁用
     *
     * @param key
     * @param url
     * @return
     */
    public boolean hasDownload(String key, String url) {
        byte[] value = mCache.getAsBinary(key);
        if (value != null) {
            LogUtils.d(TAG, "资源已经下载过: key:" + key + "对应url:" + url);
            return true;
        }
        return false;
    }

    public void putCache(String url, String data) {
        //remove statinfo
        String key = getKeyFromUrl(url);
        if (!TextUtils.isEmpty(data)) {
            int expiredTime = expiredTime(url);
            mCache.put(key, data, expiredTime);
            LogUtils.d(TAG, "加入cache成功: key:" + key + "对应url:" + url);
        }
    }

    /**
     * 清除所有非图片的缓存
     */
    public void clearNoImage() {
        mCache.clearNoImage();
    }

    /**
     * 清除所有图片的缓存
     */
    public void clearImage() {
        mCache.clearImage();
    }

    /**
     * 清除所有缓存
     */
    public void clear() {
        mCache.clear();
    }

    /**
     * 替换Url中非字母和非数字的字符，这里比较重要，因为我们用Url作为文件名的一部分
     *
     * @return
     */
    public static String getKeyFromUrl(String url) {
        String key = url;
        //资讯页特殊处理；
        String[] param = {"statinfo", "auth"};
        key = UrlUtil.removeUrlParams(key, param);
        //服务器优化配合：浏览器会限制同一域名的并发请求，一般是6个，随机域名可增加并发数量。
        //mycdn+ [number] =>""
        key = key.replaceAll("mycdn\\d?.", "");
        //删除所有的特殊字符： ? & / . 等等
        key = key.replaceAll("[^\\w]", "");

        return key;
    }

    /**
     * 请求下载网络资源并缓存到本地
     */
    public void requestNetToCache(final String url) {
//        TaskManager.getInstance().submit(poolGroup, new Runnable() {
//            @Override
//            public void run() {
//                int expireTime = expiredTime(url);
//                downloadFromNetToCache(url, expireTime);
//            }
//        });
    }

    public static int expiredTime(String url) {
        int expireTime = 128 * WebViewCache.TIME_DAY;
        // html、js和css文件缓存时间超过3小时 则先返回缓存数据,然后重新请求网络资源并覆盖缓存
        if (!UrlUtil.urlIsImg(url)) {
            expireTime = 3 * WebViewCache.TIME_HOUR;
        }
        return expireTime;
    }
}
