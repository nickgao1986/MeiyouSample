/**
 * ****************************************************************************
 * Copyright (c) 2013, 2015 linggan.com
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * *****************************************************************************
 */
package nickgao.com.meiyousample.utils;

import android.net.Uri;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;

/**
 * URL处理工具类；
 * 新增通过URL获取 文件名
 */
public class UrlUtil {
    public static final String REG_IMG_SUFFER = ".+(.jpg|.png|.gif|.jpeg|.ico|.webp|.bmp).+";//图片
    public static final String REG_IMG_SUFFER_NODOR = ".+(jpg|png|gif|jpeg|ico|webp|bmp).+";//图片

    /**
     * 获取URL中 文件的名字；
     * 使用URLUtil#guessFileName();
     *
     * @param url
     * @return
     */
    @Deprecated
    public static String getFileName(String url) {
        String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
        Pattern pat = Pattern.compile("[\\w]+[\\.](" + suffixes + ")");//正则判断  
        Matcher mc = pat.matcher(url);//条件匹配  
        String fileName = "";
        //截取文件名后缀名
        while (mc.find()) {
            fileName = mc.group();
        }
        return fileName;
    }

    public static int[] getWidthHeightByUrl(String url) {
        try {
            if (url != null) {
                String[] splits = url.split("_");
                if (splits.length >= 3) {
                    String widthStr = splits[splits.length - 2];
                    String heightStr = splits[splits.length - 1];
                    int index = heightStr.indexOf(".");
                    if (index > 0) {
                        heightStr = heightStr.substring(0, index);
                    }
                    int width = Integer.parseInt(widthStr);
                    int height = Integer.parseInt(heightStr);

                    return new int[]{width, height};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static String getParmValueFromUrl(String url, String parmName) {
//        try {
//            List<NameValuePair> parmList = URLEncodedUtils.parse(URI.create(url), "UTF-8");
//            for (NameValuePair nameValuePair : parmList) {
//                if (nameValuePair.getName().equals(parmName)) {
//                    return nameValuePair.getValue();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    /**
     * 普通网页，可以缓存的URL；
     *
     * @param url
     * @return
     */
    public static boolean isResUrl(String url) {
        return url.contains(".js")
                || url.contains(".jpg")
                || url.contains(".png")
                || url.contains(".gif")
                || url.contains(".jpeg")
                || url.contains(".ico")
                || url.contains(".webp")
                || url.contains(".bmp")
                || url.contains(".css")
                || url.contains(".ttf")
                || url.contains(".svg")
                || url.contains(".tif")
                || url.contains(".html")
                || url.contains(".woff")
                || url.contains(".htm");
    }

    /**
     * 是否富媒体；
     *
     * @param url
     * @return
     */
    public static boolean isMediaUrl(String url) {
        return url.contains(".mp4")
                || url.contains(".mp3")
                || url.contains(".avi")
                || url.contains(".3gp")
                || url.contains(".wav");
    }

    public static boolean isPreloadUrl(String url) {
        return true;
//        if (url.contains(".php")) {
//            return true;
//        }
//        return isResUrl(url);
    }

    /**
     * 判断URL是否为图片链接
     *
     * @param url
     * @return
     */
    public static boolean urlIsImg(String url) {
        url = url.toLowerCase();
        Pattern pattern = Pattern.compile(REG_IMG_SUFFER);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    /**
     * 判断URL转换后的Key是否为图片链接
     *
     * @param key
     * @return
     */
    public static boolean keyIsImg(String key) {
        key = key.toLowerCase();
        Pattern pattern = Pattern.compile(REG_IMG_SUFFER_NODOR);
        Matcher matcher = pattern.matcher(key);
        return matcher.matches();
    }

    public static String replaceBlank(String str) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        LogUtils.d("original string:" + str);
        Matcher m = p.matcher(str);
        String after = m.replaceAll("");
        LogUtils.d("after replace:" + after);
        return after;
    }

    /**
     * 获取URI参数
     *
     * @param uri
     * @return
     */
    public static Map<String, String> getParamMapByUri(Uri uri) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        try {
            String query = uri.getQuery();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = pair.substring(0, idx);
                String value = pair.substring(idx + 1);
                queryPairs.put(key, value);
            }
        } catch (Exception e) {
            //   LogUtils.e(e.getLocalizedMessage());
        }
        return queryPairs;
    }

    /**
     * 对url添加或追加某一属性，可处理编码化url
     *
     * @param url
     * @param value 属性值
     * @param name  属性名
     * @return
     */
    public static String addUrlValue(String url, String name, String value) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }

        String a = "";
        if (!url.contains("/")) {//编码后的URL不可能存在/
            //编码化url处理 %3F->? %3D->=
            if (url.contains("%3F") && !url.contains("%3D")) {
                a = "";
            } else if (url.contains("%3F") && url.contains("%3D")) {
                a = "%26";
            } else {
                a = "%3F";
            }
        } else {
            //非编码化url处理
            if (url.contains("?") && !url.contains("=")) {//http://XX.cn?
                a = "";
            } else if (url.contains("?") && url.contains("=")) {//http://XX.cn?a=b
                a = "&";
            } else {
                a = "?";
            }
        }
        return url += (a + name + "=" + value);
    }

    /**
     * 移除URL里面的参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String removeUrlParams(String url, String[] params) {
        String reg = null;
        for (int i = 0; i < params.length; i++) {
            reg = "(?<=[\\?&])" + params[i] + "=[^&]*&?";
            url = url.replaceAll(reg, "");
        }
        url = url.replaceAll("&+$", "");
        return url;
    }

    /**
     * 吧URL转为本地字符串，用于当做文件名，否则url含有特殊字符无法保存
     *
     * @param url
     * @return
     */
    public static String url2FileName(String url) {
        return url.replaceAll("[^\\w]", "");
    }


}
