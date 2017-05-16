package com.lingan.seeyou.ui.view.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.skin.CacheDisc;

/**
 * 图片上传辅助工具类
 * Author: lwh
 * Date: 9/20/16 10:29.
 */
public class ImageUploaderUtil {
    public static final String TAG = "ImageUploaderUtil";
    //七牛链接
    public static final String SERVER_PHOTO = "http://sc.seeyouyima.com/";
    

    /**
     * 获取文件路径
     *
     * @return
     */
    public static File getCacheFile(Context context, String fileName) {
        try {
            return new File(CacheDisc.getCacheFile(context), fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    /**
     * 获取头像文件路径
     *
     * @return
     */
    public static File getHeadCacheFile(Context context, String fileName) {
        try {
            return new File(CacheDisc.getCacheFileHide(context), fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 创建SaveFileName
     * @param prefix 业务标识
     * @param bitmap 图片
     * @param userId 用户ID
     * @return
     */
    public static String createSaveFileName(String prefix,Bitmap bitmap, long userId) {
        String suffix = ".jpg";
        try {
//            String suffix = ".b";
            //为了不让系统图库找到,后缀改为.b,可能导致问题,IOS无法读取,放弃,改为修改文件夹名为"。XX"
            int width = 0;
            int height = 0;
            if (bitmap != null) {
                width = bitmap.getWidth();
                height = bitmap.getHeight();
            }
            return getUploadName(prefix,userId, width, height, suffix);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Calendar.getInstance().getTimeInMillis() + suffix;

    }

    public static String createSaveFileName(Bitmap bitmap, long userId) {
        String suffix = ".jpg";
        try {
//            String suffix = ".b";
            //为了不让系统图库找到,后缀改为.b,可能导致问题,IOS无法读取,放弃,改为修改文件夹名为"。XX"
            int width = 0;
            int height = 0;
            if (bitmap != null) {
                width = bitmap.getWidth();
                height = bitmap.getHeight();
            }
            return getUploadName("",userId, width, height, suffix);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Calendar.getInstance().getTimeInMillis() + suffix;

    }
    /**
     * 获取图片上传的文件名
     * @param userId
     * @param width
     * @param height
     * @param suffix
     * @return
     */
    public static String getUploadName(String prefix,long userId, int width, int height, String suffix) {
        long temp = userId;
        if (userId == 0) {
            temp = new Random().nextInt(10);
        }
       /* //String filename = "android-" + temp + "-" + Calendar.getInstance().getTimeInMillis();
        if (width > 0 && height > 0) {
            filename += "_" + width + "_" + height;
        }
        filename += suffix;
        LogUtils.d(TAG, "设置文件名为：" + filename);
        */
        //android标识
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("android-");
        //业务标识
        if(!TextUtils.isEmpty(prefix)){
            stringBuilder.append(prefix).append("-");
        }
        //userid timestamp
        stringBuilder.append(temp).append("-").append(Calendar.getInstance().getTimeInMillis());
        //width ,height
        if (width > 0 && height > 0) {
            stringBuilder.append("_").append(width).append("_").append(height);
        }
        //suffix
        stringBuilder.append(suffix);
        LogUtils.d(TAG, "设置文件名为：" + stringBuilder.toString());
        return stringBuilder.toString();
    }


    /**
     * 获取图片的缓存路径,在 PkgName_bitmapCache  下面
     *
     * @return
     */
    public static String getPicLocalFilePath(Context context, String filename) {
        try {
            if (TextUtils.isEmpty(filename)) {
                return "";
            }
            return getCacheFile(context, filename).getAbsolutePath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    /**
     * 获取头像图片的缓存路径,在 PkgName_bitmapCache  下面
     *
     * @return
     */
    public static String getHeadPicLocalFilePath(Context context, String filename) {
        try {
            if (TextUtils.isEmpty(filename)) {
                return "";
            }
            return getHeadCacheFile(context, filename).getAbsolutePath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public static File getPicLocalFile(Context context, String filename) {
        try {
            if (TextUtils.isEmpty(filename)) {
                return null;
            }
            return getHeadCacheFile(context, filename);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }
    
    public static String getCompressPhotoPath(Context context,String filename){
        try {
            if (TextUtils.isEmpty(filename)) {
                return "";
            }
            File file = new File(CacheDisc.getCacheFileHide(context), filename);
            return file.getAbsolutePath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取心情日记的图片 网络url
     *
     * @param filename
     * @return
     */
    public static String getPicNetUrl(String filename) {
        try {
            return SERVER_PHOTO + filename;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }
}
