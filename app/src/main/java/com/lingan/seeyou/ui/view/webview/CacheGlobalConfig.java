package com.lingan.seeyou.ui.view.webview;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存需求功能，全局的备注，数据从Door接口来；
 * https://worktile.com/project/f7653e575700422b868e9079096bcda4/task/b321121b7b914a15af92f610d07803a2
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/12/30
 */

public class CacheGlobalConfig {
    /**
     * 是否开启预加载的缓存
     */
    public boolean enableCachePrefetch = true;
    /**
     * 是否开启 网页的缓存，默认开启，调试的时候可以设置
     */
    public boolean enableCacheWebView = true;
    

    /**
     * 4. 支持请求黑名单
     */
    public List<String> blackList = new ArrayList<>();

    /**
     * MAP 
     * 支持 忽略缓存名单
     */
    public List<String> ignoreList = new ArrayList<>();

    /**
     * 7. 支持 缓存名单
     */
    public List<String> whiteList = new ArrayList<>();

}
