package com.lingan.seeyou.ui.view.webview;

/**
 * URL Cache 参数配置
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/12/28
 */

public class CacheConfig {
    /**
     * 是否新增预加载参数
     */
    public boolean addPrefetch;
    /**
     * 是否允许在蜂窝下载图片；
     */
    public boolean canDownloadImageInCellularNetwork;
    /**
     * 最大允许的图片下载数量
     */
    public int canDownloadImageCount = 100;
    /**
     * 是否删除statinfo
     */
    public boolean removeStatinfo;

    /**
     * 获取默认参数配置，默认跟资讯页缓存一致
     *
     * @return
     */
    public static CacheConfig getCacheConfigDefault() {
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.addPrefetch = true;
        cacheConfig.canDownloadImageInCellularNetwork = false;
        cacheConfig.removeStatinfo = true;
        cacheConfig.canDownloadImageCount = 3;
        return cacheConfig;
    }
}



