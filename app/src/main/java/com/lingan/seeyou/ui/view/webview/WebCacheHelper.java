package com.lingan.seeyou.ui.view.webview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.utils.UrlUtil;

/**
 * 网页实现预加载功能,
 * 只需要调用 cacheUrl(url)即可缓存网页内的所有资源,网页会下载两次一次，一次是解析网页内可下载资源；
 * 一次是下载并缓存网页；
 * 要测试多次缓存效果，如果禁用WebViewCacheManager#downloadFromNetToCache
 * 使用了2级缓存；
 * 1. 预加载缓存 （手动在进入首页的时候缓存 首页资讯内容）
 * 2. 网页资源缓存（请求过的网页资源，会默认缓存）
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/11/18
 */

public class WebCacheHelper {
    private static final String TAG = "WebCacheHelper";
    private static WebCacheHelper instance;
    final WebViewCacheManager cacheManager;
    /**
     * 进入临时缓存，避免重复URL一致提交；在List上下滑动的时候特别有效
     */
    private ArrayList<String> urlList = new ArrayList<>();
    /**
     * 全局的缓存配置
     */
    private CacheGlobalConfig config = new CacheGlobalConfig();

    public static WebCacheHelper getInstance() {
        if (instance == null) {
            instance = new WebCacheHelper();
        }
        return instance;
    }


    public WebCacheHelper() {
        Context context = SeeyouApplication.getContext();
        cacheManager = WebViewCacheManager.getInstance(context);
    }

    /**
     * 设置Cache全局配置，比如是否开启预加载Cache，是否开启网页Cache等等；
     *
     * @param config
     */
    public void setConfig(@NonNull CacheGlobalConfig config) {
        this.config = config;
    }

    public CacheGlobalConfig getConfig() {
        return this.config;
    }

    public void cacheUrl(final String url) {
        if (!config.enableCachePrefetch) return;
        CacheConfig cacheConfigDefault = CacheConfig.getCacheConfigDefault();
        cacheUrl(url, cacheConfigDefault);
    }

    /**
     * 缓存网页URL,通过Log可以看命中情况
     * URL代表的网页会下载两次，一次是请求分析所以内容；
     * 一次是下载放入Cache；
     *
     * @param url
     */
    public void cacheUrl(final String url, final CacheConfig config) {
        try {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            //防止List界面URl，重复提交
            if (urlList.contains(url)) {
                LogUtils.d(TAG, "111已经在页面临时缓存了，不重新缓存 %s ", url);
                return;
            } else {
                urlList.add(url);
            }

            String key = WebViewCacheManager.getKeyFromUrl(url);
            boolean hasDownload = cacheManager.hasDownload(key, url);
            if (hasDownload) {
                LogUtils.d(TAG, "222已经缓存了，不再解析URL里的资源=> %s ", url);
                return;
            }
            //未下载
//            TaskManager.getInstance().submit(WebViewCacheManager.poolGroup, new Runnable() {
//                @Override
//                public void run() {
//                    ArrayList<String> linkList = getResUrl(url, config);
////                linkList.add(url);
//                    for (String link : linkList) {
//                        cacheManager.requestNetToCache(link);
//                        LogUtils.d(TAG, "cacheUrl => " + link);
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空临时缓存
     */
    public void cleanTempCache() {
        urlList.clear();
    }

    /**
     * Url 是否已经缓存
     *
     * @param url
     * @return
     */
    public boolean hasCache(String url) {
        String key = WebViewCacheManager.getKeyFromUrl(url);
        return cacheManager.hasDownload(key, url);
    }

    /**
     * 清空全部缓存内容,测试过程用，外部不要用
     */
    public void cleanCache() {
        cleanTempCache();
        cacheManager.clear();
    }

    /************************ Private Method ***********************/

    /**
     * 把url，加入 ListList;
     *
     * @param url
     * @param linkList
     */
    private void addList(String url, ArrayList<String> linkList) {
        //过滤非资源类型URL
        if (!UrlUtil.isResUrl(url)) return;
        //非空过滤
        if (TextUtils.isEmpty(url)) return;
        //防止重复做一次过滤；
        if (linkList.contains(url)) return;

        linkList.add(url);
    }

}
