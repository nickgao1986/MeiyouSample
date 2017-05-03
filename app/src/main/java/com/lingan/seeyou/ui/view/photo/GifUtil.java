package com.lingan.seeyou.ui.view.photo;

import android.webkit.URLUtil;

/**
 * GIF工具类
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/12/9 下午12:02
 */
public class GifUtil {
    public static boolean isGif(String url) {
        //返回Download.bin 代表URL不知道
        String fileName = URLUtil.guessFileName(url, null, null);
        return fileName.endsWith(".gif");
    }
}
