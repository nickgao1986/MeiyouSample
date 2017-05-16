package com.meetyou.crsdk.util;

import nickgao.com.framework.utils.StringUtil;

/**
 * Author: lwh
 * Date: 3/25/16 14:57.
 */
public class UrlUtil {
    public static final String SERVER_PHOTO = "http://sc.seeyouyima.com/";


    public static int[] getWidthHeightByUrl(String url) {
        try {
            if(url != null) {
                String[] splits = url.split("_");
                if(splits.length >= 3) {
                    String widthStr = splits[splits.length - 2];
                    String heightStr = splits[splits.length - 1];
                    int index = heightStr.indexOf(".");
                    if(index > 0) {
                        heightStr = heightStr.substring(0, index);
                    }

                    int width = Integer.parseInt(widthStr);
                    int height = Integer.parseInt(heightStr);
                    return new int[]{width, height};
                }
            }
        } catch (Exception var7) {
            ;
        }

        return null;
    }
    /**
     * 根据url获取图片尺寸
     *
     * @param url 图片url
     * @return ImageSize 返回图片尺寸,宽高为0则返回null
     */
    public static ImageSize getImageWHByUrl(String url) {
        int[] size = getWidthHeightByUrl(url);
        if (size == null || size.length < 2) {
            size = getThirdSDKWidthHeightByUrl(url);
            if (size == null || size.length < 2) {
                return null;
            }
        }
        if (size[0] == 0 || size[1] == 0) {
            return null;
        }
        return new ImageSize(size[0], size[1]);
    }

    /**
     * 通过地址获取图片宽高
     *
     * @param url
     * @return
     */
    public static int[] getThirdSDKWidthHeightByUrl(String url) {
        try {
            if (url != null) {
                String[] splits = url.split("&");
                if (splits.length >= 3) {
                    String widthStr = "";
                    for (String item : splits) {
                        if (item.startsWith("w=")) {
                            widthStr = item;
                            break;
                        }
                    }
                    String heightStr = "";
                    for (String item : splits) {
                        if (item.startsWith("h=")) {
                            heightStr = item;
                            break;
                        }
                    }
                    if (!StringUtil.isNull(widthStr) && !StringUtil.isNull(heightStr)) {
                        int index = heightStr.indexOf("=");
                        if (index > 0) {
                            heightStr = heightStr.substring(index + 1, heightStr.length());
                        }
                        index = widthStr.indexOf("=");
                        if (index > 0) {
                            widthStr = widthStr.substring(index + 1, widthStr.length());
                        }
                        int width = Integer.parseInt(widthStr);
                        int height = Integer.parseInt(heightStr);
                        return new int[]{width, height};
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
        }
        return null;
    }
}
